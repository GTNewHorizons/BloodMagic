package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityEnergyBazookaMainProjectile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergyBazooka extends EnergyBlast {

    public EnergyBazooka(int tier) {
        super(tier);
        switch (this.tier) {
            case 1 -> {
                this.setEnergyUsed(AlchemicalWizardry.energyBazookaLPPerShot);
                this.damage = AlchemicalWizardry.energyBazookaDamage;
            }
            case 2 -> {
                this.setEnergyUsed(AlchemicalWizardry.energyBazookaSecondTierLPPerShot);
                this.damage = AlchemicalWizardry.energyBazookaSecondTierDamage;
            }
            case 3 -> {
                this.setEnergyUsed(AlchemicalWizardry.energyBazookaThirdTierLPPerShot);
                this.damage = AlchemicalWizardry.energyBazookaThirdTierDamage;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBazooka_activated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBazooka_activated");
        this.activeIconTier2 = iconRegister.registerIcon("AlchemicalWizardry:EnergyBazooka2_activated");
        this.activeIconTier3 = iconRegister.registerIcon("AlchemicalWizardry:EnergyBazooka3_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
    }

    @Override
    public void shoot(World world, EntityPlayer player) {
        world.spawnEntityInWorld(new EntityEnergyBazookaMainProjectile(world, player, this.damage));

        Vec3 vec = player.getLookVec();
        double wantedVelocity = this.tier * 2.0D;
        player.motionX = -vec.xCoord * wantedVelocity;
        player.motionY = -vec.yCoord * wantedVelocity;
        player.motionZ = -vec.zCoord * wantedVelocity;
        world.playSoundEffect(
                player.posX + 0.5F,
                player.posY + 0.5F,
                player.posZ + 0.5F,
                "random.fizz",
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        player.fallDistance = 0;
    }

    @Override
    public int getShotDelay() {
        return switch (this.tier) {
            case 1 -> AlchemicalWizardry.energyBazookaMaxDelay;
            case 2 -> AlchemicalWizardry.energyBazookaSecondTierMaxDelay;
            case 3 -> AlchemicalWizardry.energyBazookaThirdTierMaxDelay;
            default -> 1;
        };
    }

    @Override
    public int drainTicks() {
        return switch (this.tier) {
            case 1 -> AlchemicalWizardry.energyBazookaMaxDelayAfterActivation;
            case 2 -> AlchemicalWizardry.energyBazookaSecondTierMaxDelayAfterActivation;
            case 3 -> AlchemicalWizardry.energyBazookaThirdTierMaxDelayAfterActivation;
            default -> 1;
        };
    }

    @Override
    public int drainCost() {
        return switch (this.tier) {
            case 1 -> AlchemicalWizardry.energyBazookaLPPerActivation;
            case 2 -> AlchemicalWizardry.energyBazookaSecondTierLPPerActivation;
            case 3 -> AlchemicalWizardry.energyBazookaThirdTierLPPerActivation;
            default -> 0;
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.energybazooka.desc"));
        tooltip.add(StatCollector.translateToLocal("tooltip.alchemy.damage") + " " + this.damage);
        addBindingInformation(item, tooltip);
    }
}
