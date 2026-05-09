package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.WindGustProjectile;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class EntityMinorDemonGruntWind extends EntityMinorDemonGrunt {

    public EntityMinorDemonGruntWind(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntWindID);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (friendlyDemon(entity)) {
            return false;
        }

        if (entity instanceof EntityPlayer player) {
            SpellHelper.setPlayerSpeedFromServer(player, entity.motionX, entity.motionY + 3, entity.motionZ);
        } else if (entity instanceof EntityLivingBase) {
            entity.motionY += 3.0D;
        }

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 20F);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.fallDistance = 0;
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new WindGustProjectile(worldObj, this, target, 1.8f, 0f, 15, 600);
    }
}
