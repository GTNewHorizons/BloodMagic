package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;

public class EntityMinorDemonGruntGuardian extends EntityMinorDemonGrunt {

    public EntityMinorDemonGruntGuardian(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntGuardianID);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (friendlyDemon((entity))) {
            return false;
        }

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) 25);
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new HolyProjectile(worldObj, this, target, 1.8f, 0f, 20, 600);
    }
}
