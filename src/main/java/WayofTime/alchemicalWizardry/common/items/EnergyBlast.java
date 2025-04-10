package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergyBlast extends EnergyItems {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon activeIconTier2;

    @SideOnly(Side.CLIENT)
    private IIcon activeIconTier3;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    private int tier;
    private int damage;

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
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        if (tag.getBoolean("isActive")) {
            switch (this.tier) {
                case 1:
                    return this.activeIcon;
                case 2:
                    return this.activeIconTier2;
                case 3:
                    return this.activeIconTier3;
            }
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        int maxDelay = 1;
        int maxDelayAfterActivation = 1;
        switch (this.tier) {
            case 1:
                maxDelay = AlchemicalWizardry.energyBlastMaxDelay;
                maxDelayAfterActivation = AlchemicalWizardry.energyBlastMaxDelayAfterActivation + 1;
                break;
            case 2:
                maxDelay = AlchemicalWizardry.energyBlastSecondTierMaxDelay;
                maxDelayAfterActivation = AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation + 1;
                break;
            case 3:
                maxDelay = AlchemicalWizardry.energyBlastThirdTierMaxDelay;
                maxDelayAfterActivation = AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation + 1;
                break;
        }
        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking()) {
            this.setActivated(par1ItemStack, !getActivated(par1ItemStack));
            par1ItemStack.getTagCompound()
                    .setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % maxDelayAfterActivation);
            return par1ItemStack;
        }

        if (!getActivated(par1ItemStack)) {
            return par1ItemStack;
        }

        if (this.getDelay(par1ItemStack) > 0) {
            return par1ItemStack;
        }

        if (!par3EntityPlayer.capabilities.isCreativeMode) {
            if (!syphonBatteries(par1ItemStack, par3EntityPlayer, this.getEnergyUsed())) {
                return par1ItemStack;
            }
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote) {
            par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, this.damage));
            this.setDelay(par1ItemStack, maxDelay);
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }
        int delay = this.getDelay(par1ItemStack);

        if (!par2World.isRemote && delay > 0) {
            this.setDelay(par1ItemStack, delay - 1);
        }
        int lpPerActivation = 0;
        int maxDelayAfterActivation = 1;
        switch (this.tier) {
            case 1:
                lpPerActivation = AlchemicalWizardry.energyBlastLPPerActivation;
                maxDelayAfterActivation = AlchemicalWizardry.energyBlastMaxDelayAfterActivation + 1;
                break;
            case 2:
                lpPerActivation = AlchemicalWizardry.energyBlastSecondTierLPPerActivation;
                maxDelayAfterActivation = AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation + 1;
                break;
            case 3:
                lpPerActivation = AlchemicalWizardry.energyBlastThirdTierLPPerActivation;
                maxDelayAfterActivation = AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation + 1;
                break;
        }
        if (par2World.getWorldTime() % maxDelayAfterActivation
                == par1ItemStack.getTagCompound().getInteger("worldTimeDelay")
                && par1ItemStack.getTagCompound().getBoolean("isActive")) {
            if (!par3EntityPlayer.capabilities.isCreativeMode) {
                if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, lpPerActivation)) {
                    this.setActivated(par1ItemStack, false);
                }
            }
        }

        par1ItemStack.setItemDamage(0);
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
}
