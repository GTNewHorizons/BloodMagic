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
            case 1:
                this.setEnergyUsed(AlchemicalWizardry.energyBazookaLPPerShot);
                this.damage = AlchemicalWizardry.energyBazookaDamage;
                break;
            case 2:
                this.setEnergyUsed(AlchemicalWizardry.energyBazookaSecondTierLPPerShot);
                this.damage = AlchemicalWizardry.energyBazookaSecondTierDamage;
                break;
            case 3:
                this.setEnergyUsed(AlchemicalWizardry.energyBazookaThirdTierLPPerShot);
                this.damage = AlchemicalWizardry.energyBazookaThirdTierDamage;
                break;
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
    public void shoot(World par2World, EntityPlayer par3EntityPlayer) {
        par2World.spawnEntityInWorld(new EntityEnergyBazookaMainProjectile(par2World, par3EntityPlayer, this.damage));

        Vec3 vec = par3EntityPlayer.getLookVec();
        double wantedVelocity = this.tier * 2.0D;
        par3EntityPlayer.motionX = -vec.xCoord * wantedVelocity;
        par3EntityPlayer.motionY = -vec.yCoord * wantedVelocity;
        par3EntityPlayer.motionZ = -vec.zCoord * wantedVelocity;
        par2World.playSoundEffect(
                par3EntityPlayer.posX + 0.5F,
                par3EntityPlayer.posY + 0.5F,
                par3EntityPlayer.posZ + 0.5F,
                "random.fizz",
                0.5F,
                2.6F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.8F);
        par3EntityPlayer.fallDistance = 0;
    }

    @Override
    public int getShotDelay() {
        switch (this.tier) {
            case 1:
                return AlchemicalWizardry.energyBazookaMaxDelay;
            case 2:
                return AlchemicalWizardry.energyBazookaSecondTierMaxDelay;
            case 3:
                return AlchemicalWizardry.energyBazookaThirdTierMaxDelay;
        }
        return 1;
    }

    @Override
    public int drainTicks() {
        switch (this.tier) {
            case 1:
                return AlchemicalWizardry.energyBazookaMaxDelayAfterActivation;
            case 2:
                return AlchemicalWizardry.energyBazookaSecondTierMaxDelayAfterActivation;
            case 3:
                return AlchemicalWizardry.energyBazookaThirdTierMaxDelayAfterActivation;
        }
        return 1;
    }

    @Override
    public int drainCost() {
        switch (this.tier) {
            case 1:
                return AlchemicalWizardry.energyBazookaLPPerActivation;
            case 2:
                return AlchemicalWizardry.energyBazookaSecondTierLPPerActivation;
            case 3:
                return AlchemicalWizardry.energyBazookaThirdTierLPPerActivation;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.energybazooka.desc"));
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.damage") + " " + this.damage);
        addBindingInformation(par1ItemStack, par3List);
    }
}
