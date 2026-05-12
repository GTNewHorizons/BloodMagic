package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;

public class EntityMinorDemonGruntGuardianEarth extends EntityMinorDemonGruntGuardian {

    public EntityMinorDemonGruntGuardianEarth(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntGuardianEarthID);
    }

    @Override
    public void causeEffect(Entity entity) {
        if (entity instanceof EntityLivingBase e) {
            e.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionHeavyHeartID, 200, 4));
        }
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new ExplosionProjectile(worldObj, this, target, 1.8f, 0f, 20, 600, false);
    }
}
