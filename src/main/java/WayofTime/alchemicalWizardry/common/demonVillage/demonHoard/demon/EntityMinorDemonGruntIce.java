package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;

public class EntityMinorDemonGruntIce extends EntityMinorDemonGrunt {

    public EntityMinorDemonGruntIce(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntIceID);
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new IceProjectile(worldObj, this, target, 1.8f, 0f, 15, 600);
    }
}
