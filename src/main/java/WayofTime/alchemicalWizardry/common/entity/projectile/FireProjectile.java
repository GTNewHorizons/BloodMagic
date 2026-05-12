package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class FireProjectile extends EnergyBlastProjectile {

    public FireProjectile(World world) {
        super(world);
    }

    public FireProjectile(World world, EntityLivingBase player, int damage) {
        super(world, player, damage);
    }

    public FireProjectile(World world, EntityLivingBase player, int damage, int maxTicksInAir, double posX, double posY,
            double posZ, float rotationYaw, float rotationPitch) {
        super(world, player, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public FireProjectile(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity,
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
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        if (worldObj.isAirBlock((int) this.posX + i, (int) this.posY + j, (int) this.posZ + k)) {
                            worldObj.setBlock(
                                    (int) this.posX + i,
                                    (int) this.posY + j,
                                    (int) this.posZ + k,
                                    Blocks.fire);
                        }
                    }
                }
            }
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else if (target instanceof EntityLivingBase entity) {
            target.setFire(10 * this.projectileDamage);
            entity.setRevengeTarget(shootingEntity);

            if (entity.isPotionActive(Potion.fireResistance) || target.isImmuneToFire()) {
                target.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            } else {
                doDamage(projectileDamage, target);
                target.hurtResistantTime = 0;
            }
        }

        if (worldObj.isAirBlock((int) this.posX, (int) this.posY, (int) this.posZ)) {
            worldObj.setBlock((int) this.posX, (int) this.posY, (int) this.posZ, Blocks.fire);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }
}
