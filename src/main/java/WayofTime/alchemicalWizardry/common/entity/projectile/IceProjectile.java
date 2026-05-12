package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class IceProjectile extends EnergyBlastProjectile {

    public IceProjectile(World world) {
        super(world);
    }

    public IceProjectile(World world, EntityLivingBase player, int damage) {
        super(world, player, damage);
    }

    public IceProjectile(World world, EntityLivingBase player, int damage, int maxTicksInAir, double posX, double posY,
            double posZ, float rotationYaw, float rotationPitch) {
        super(world, player, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public IceProjectile(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity,
            float inaccuracy, int damage, int maxTicksInAir) {
        super(world, shooter, target, velocity, inaccuracy, damage, maxTicksInAir);
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        }
        this.setDead();
    }

    @Override
    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else if (target instanceof EntityLivingBase entity) {
            if (target.isImmuneToFire()) {
                doDamage(projectileDamage * 2, target);
                entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 2));
            } else {
                doDamage(projectileDamage, target);
                entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 1));
            }
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    @Override
    public void doFiringParticles() {
        SpellHelper.sendParticleToAllAround(
                worldObj,
                posX,
                posY,
                posZ,
                30,
                worldObj.provider.dimensionId,
                "mobSpellAmbient",
                posX + smallGauss(0.1D),
                posY + smallGauss(0.1D),
                posZ + smallGauss(0.1D),
                0.5D,
                0.5D,
                0.5D);
        SpellHelper.sendParticleToAllAround(
                worldObj,
                posX,
                posY,
                posZ,
                30,
                worldObj.provider.dimensionId,
                "explode",
                posX,
                posY,
                posZ,
                gaussian(motionX),
                gaussian(motionY),
                gaussian(motionZ));
    }
}
