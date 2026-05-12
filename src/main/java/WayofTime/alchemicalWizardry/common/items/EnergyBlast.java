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
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (getDelay(item) > 0 && IBindable.isActive(item) && !player.isSneaking()) {
            return item;
        }

        if (checkRightClick(item, world, player)) {
            setDelay(item, drainTicks());
            return item;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            shoot(world, player);
            this.setDelay(item, getShotDelay());
        }

        return item;
    }

    public void shoot(World world, EntityPlayer player) {
        world.spawnEntityInWorld(new EnergyBlastProjectile(world, player, this.damage));
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int par4, boolean par5) {
        if (!(entity instanceof EntityPlayer player)) {
            return;
        }

        int delay = this.getDelay(item);

        if (!world.isRemote && delay > 0) {
            this.setDelay(item, delay - 1);
        }

        checkPassiveDrain(item, world, player);

        item.setItemDamage(0);
    }

    /**
     * The delay between firing multiple shots.
     */
    public int getShotDelay() {
        return switch (this.tier) {
            case 1 -> AlchemicalWizardry.energyBlastMaxDelay;
            case 2 -> AlchemicalWizardry.energyBlastSecondTierMaxDelay;
            case 3 -> AlchemicalWizardry.energyBlastThirdTierMaxDelay;
            default -> 1;
        };
    }

    /**
     * Used for the warmup delay as well as for passive draining.
     */
    @Override
    public int drainTicks() {
        return switch (this.tier) {
            case 1 -> AlchemicalWizardry.energyBlastMaxDelayAfterActivation;
            case 2 -> AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation;
            case 3 -> AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation;
            default -> 1;
        };
    }

    @Override
    public int drainCost() {
        return switch (this.tier) {
            case 1 -> AlchemicalWizardry.energyBlastLPPerActivation;
            case 2 -> AlchemicalWizardry.energyBlastSecondTierLPPerActivation;
            case 3 -> AlchemicalWizardry.energyBlastThirdTierLPPerActivation;
            default -> 0;
        };
    }

    @Override
    public int rightClickCost() {
        return getEnergyUsed();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.energyblast.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.energyblast.desc2"));
        tooltip.add(StatCollector.translateToLocal("tooltip.alchemy.damage") + " " + this.damage);
        addBindingInformation(item, tooltip);
    }

    public void setDelay(ItemStack item, int delay) {
        IBindable.getTag(item).setInteger("delay", delay);
    }

    public int getDelay(ItemStack item) {
        return IBindable.getTag(item).getInteger("delay");
    }
}
