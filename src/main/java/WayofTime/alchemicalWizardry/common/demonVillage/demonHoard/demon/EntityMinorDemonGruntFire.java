package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;

public class EntityMinorDemonGruntFire extends EntityMinorDemonGrunt {

    public EntityMinorDemonGruntFire(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntFireID);
        this.isImmuneToFire = true;
    }

    @Override
    public void causeEffect(Entity entity) {
        entity.setFire(10);
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new FireProjectile(worldObj, this, target, 1.8f, 0f, 15, 600);
    }
}
