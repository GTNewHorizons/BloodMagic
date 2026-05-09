package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnergyBazookaSecondaryProjectile extends EnergyBlastProjectile implements IProjectile {

    public EntityLivingBase shootingEntity;

    public int damage;

    public EntityEnergyBazookaSecondaryProjectile(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
        damage = 5;
    }

    public EntityEnergyBazookaSecondaryProjectile(World world, double x, double y, double z, int damage) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        yOffset = 0.0F;
        this.damage = damage;
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            this.groundImpact(mop.sideHit);
            worldObj.createExplosion(shootingEntity, posX, posY, posZ, 2, false);
        }
    }

    @Override
    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else {
            doDamage(this.damage + d6(), target);
            worldObj.createExplosion(shootingEntity, posX, posY, posZ, 2, false);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    private int d6() {
        return rand.nextInt(6) + 1;
    }

    @Override
    public void spawnHitParticles(String string, int particleCount) {
        for (int particles = 0; particles < particleCount; particles++) {
            worldObj.spawnParticle(
                    string,
                    posX,
                    posY - (string.equals("portal") ? 1 : 0),
                    posZ,
                    gaussian(motionX),
                    gaussian(motionY),
                    gaussian(motionZ));
        }
    }

    @Override
    public void doDamage(int i, Entity mop) {
        mop.attackEntityFrom(this.getDamageSource(), i);
    }

    @Override
    public DamageSource getDamageSource() {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    public void groundImpact(int sideHit) {
        this.ricochet(sideHit);
    }

    private void ricochet(int sideHit) {
        switch (sideHit) {
            case 0, 1 -> motionY = motionY * -1; // topHit, bottomHit, reflect Y
            case 2, 3 -> motionZ = motionZ * -1; // westHit, eastHit, reflect Z
            case 4, 5 -> motionX = motionX * -1; // southHit, northHit, reflect X
        }

        ricochetCounter++;

        if (ricochetCounter > this.getRicochetMax()) {
            scheduledForDeath = true;

            for (int particles = 0; particles < 4; particles++) {
                switch (sideHit) {
                    case 0 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), -gaussian(0.1D), gaussian(0.1D));
                    case 1, 5, 3 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                    case 2 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), -gaussian(0.1D));
                    case 4 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, -gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                }
            }
        }
    }

    private int getRicochetMax() {
        return 3;
    }
}
