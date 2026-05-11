package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.EntityLivingBase;
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
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new HolyProjectile(worldObj, this, target, 1.8f, 0f, 20, 600);
    }

    @Override
    protected float meleeDamage() {
        return 25f;
    }
}
