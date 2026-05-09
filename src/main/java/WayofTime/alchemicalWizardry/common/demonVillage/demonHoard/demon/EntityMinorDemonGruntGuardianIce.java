package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;

public class EntityMinorDemonGruntGuardianIce extends EntityMinorDemonGruntGuardian {

    public EntityMinorDemonGruntGuardianIce(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntGuardianIceID);
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new IceProjectile(worldObj, this, target, 1.8f, 0f, 20, 600);
    }
}
