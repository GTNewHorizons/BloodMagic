package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ExplosionProjectile extends EnergyBlastProjectile {

    protected boolean causesEnvDamage;

    public ExplosionProjectile(World world) {
        super(world);
    }

    public ExplosionProjectile(World world, EntityLivingBase player, int damage, boolean flag) {
        super(world, player, damage);
        causesEnvDamage = flag;
    }

    public ExplosionProjectile(World world, EntityLivingBase shooter, EntityLivingBase target, float par4, float par5,
            int damage, int maxTicksInAir, boolean flag) {
        super(world, shooter, target, par4, par5, damage, maxTicksInAir);
        causesEnvDamage = flag;
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);

            worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) (2), causesEnvDamage);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) (2), causesEnvDamage);
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else {
            if (target instanceof EntityLivingBase) {
                doDamage(projectileDamage, target);
            }
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles() {
        worldObj.spawnParticle(
                "mobSpellAmbient",
                posX + smallGauss(0.1D),
                posY + smallGauss(0.1D),
                posZ + smallGauss(0.1D),
                0.5D,
                0.5D,
                0.5D);
        worldObj.spawnParticle("explode", posX, posY, posZ, gaussian(motionX), gaussian(motionY), gaussian(motionZ));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("causesEnvDamage", causesEnvDamage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        causesEnvDamage = tag.getBoolean("causesEnvDamage");
    }
}
