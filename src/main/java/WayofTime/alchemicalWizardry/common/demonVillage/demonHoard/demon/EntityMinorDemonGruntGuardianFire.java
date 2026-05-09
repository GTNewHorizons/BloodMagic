package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;

public class EntityMinorDemonGruntGuardianFire extends EntityMinorDemonGruntGuardian {

    public EntityMinorDemonGruntGuardianFire(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntGuardianFireID);
        this.isImmuneToFire = true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (friendlyDemon(entity)) {
            return false;
        }

        entity.setFire(15);

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) 25);
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new FireProjectile(worldObj, this, target, 1.8f, 0f, 20, 600);
    }
}
