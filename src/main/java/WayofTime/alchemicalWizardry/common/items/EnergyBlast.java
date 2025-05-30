package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergyBlast extends EnergyItems {

    @SideOnly(Side.CLIENT)
    public IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    public IIcon activeIconTier2;

    @SideOnly(Side.CLIENT)
    public IIcon activeIconTier3;

    @SideOnly(Side.CLIENT)
    public IIcon passiveIcon;

    public int tier;
    public int damage;

    public EnergyBlast(int tier) {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("energyBlaster");
        setFull3D();
        setMaxDamage(250);
        this.tier = tier;
        switch (this.tier) {
            case 1:
                this.setEnergyUsed(AlchemicalWizardry.energyBlastLPPerShot);
                this.damage = AlchemicalWizardry.energyBlastDamage;
                break;
            case 2:
                this.setEnergyUsed(AlchemicalWizardry.energyBlastSecondTierLPPerShot);
                this.damage = AlchemicalWizardry.energyBlastSecondTierDamage;
                break;
            case 3:
                this.setEnergyUsed(AlchemicalWizardry.energyBlastThirdTierLPPerShot);
                this.damage = AlchemicalWizardry.energyBlastThirdTierDamage;
                break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBlaster_activated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBlaster_activated");
        this.activeIconTier2 = iconRegister.registerIcon("AlchemicalWizardry:EnergyBlaster2_activated");
        this.activeIconTier3 = iconRegister.registerIcon("AlchemicalWizardry:EnergyBlaster3_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (IBindable.isActive(stack)) {
            return switch (this.tier) {
                case 2 -> this.activeIconTier2;
                case 3 -> this.activeIconTier3;
                default -> this.activeIcon;
            };
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (getDelay(par1ItemStack) > 0 && IBindable.isActive(par1ItemStack) && !par3EntityPlayer.isSneaking()) {
            return par1ItemStack;
        }

        if (checkRightClick(par1ItemStack, par2World, par3EntityPlayer)) {
            setDelay(par1ItemStack, drainTicks());
            return par1ItemStack;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote) {
            shoot(par2World, par3EntityPlayer);
            this.setDelay(par1ItemStack, getShotDelay());
        }

        return par1ItemStack;
    }

    public void shoot(World par2World, EntityPlayer par3EntityPlayer) {
        par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, this.damage));
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        int delay = this.getDelay(par1ItemStack);

        if (!par2World.isRemote && delay > 0) {
            this.setDelay(par1ItemStack, delay - 1);
        }

        checkPassiveDrain(par1ItemStack, par2World, par3EntityPlayer);

        par1ItemStack.setItemDamage(0);
    }

    /**
     * The delay between firing multiple shots.
     */
    public int getShotDelay() {
        switch (this.tier) {
            case 1:
                return AlchemicalWizardry.energyBlastMaxDelay;
            case 2:
                return AlchemicalWizardry.energyBlastSecondTierMaxDelay;
            case 3:
                return AlchemicalWizardry.energyBlastThirdTierMaxDelay;
        }
        return 1;
    }

    /**
     * Used for the warmup delay as well as for passive draining.
     */
    @Override
    public int drainTicks() {
        switch (this.tier) {
            case 1:
                return AlchemicalWizardry.energyBlastMaxDelayAfterActivation;
            case 2:
                return AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation;
            case 3:
                return AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation;
        }
        return 1;
    }

    @Override
    public int drainCost() {
        switch (this.tier) {
            case 1:
                return AlchemicalWizardry.energyBlastLPPerActivation;
            case 2:
                return AlchemicalWizardry.energyBlastSecondTierLPPerActivation;
            case 3:
                return AlchemicalWizardry.energyBlastThirdTierLPPerActivation;
        }
        return 0;
    }

    @Override
    public int rightClickCost() {
        return getEnergyUsed();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.energyblast.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.energyblast.desc2"));
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.damage") + " " + this.damage);
        addBindingInformation(par1ItemStack, par3List);
    }

    public void setDelay(ItemStack par1ItemStack, int delay) {
        IBindable.getTag(par1ItemStack).setInteger("delay", delay);
    }

    public int getDelay(ItemStack par1ItemStack) {
        return IBindable.getTag(par1ItemStack).getInteger("delay");
    }
}
