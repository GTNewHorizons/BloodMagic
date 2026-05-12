package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class EntityEnergyBazookaMainProjectile extends EnergyBlastProjectile {

    public EntityEnergyBazookaMainProjectile(World world) {
        super(world);
    }

    public EntityEnergyBazookaMainProjectile(World world, EntityLivingBase player, int damage) {
        super(world, player, damage);
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 5.0f, false);
            this.spawnSecondaryProjectiles();
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
                spawnSecondaryProjectiles();
            }

            worldObj.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 5.0f, false);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    public void spawnSecondaryProjectiles() {
        int secondaryDamage = getSecondaryDamage();
        for (int i = 0; i < 20; i++) {
            EntityEnergyBazookaSecondaryProjectile secProj = new EntityEnergyBazookaSecondaryProjectile(
                    worldObj,
                    this.posX,
                    this.posY,
                    this.posZ,
                    secondaryDamage);
            secProj.shootingEntity = this.shootingEntity;
            float xVel = rand.nextFloat() - rand.nextFloat();
            float yVel = rand.nextFloat() - rand.nextFloat();
            float zVel = rand.nextFloat() - rand.nextFloat();
            float wantedVel = 0.5f;
            secProj.motionX = xVel * wantedVel;
            secProj.motionY = yVel * wantedVel;
            secProj.motionZ = zVel * wantedVel;
            worldObj.spawnEntityInWorld(secProj);
        }
    }

    private int getSecondaryDamage() {
        if (this.projectileDamage == AlchemicalWizardry.energyBazookaDamage) {
            return AlchemicalWizardry.energyBazookaSecondaryDamage;
        }
        if (this.projectileDamage == AlchemicalWizardry.energyBazookaSecondTierDamage) {
            return AlchemicalWizardry.energyBazookaSecondTierSecondaryDamage;
        }
        if (this.projectileDamage == AlchemicalWizardry.energyBazookaThirdTierDamage) {
            return AlchemicalWizardry.energyBazookaThirdTierSecondaryDamage;
        }
        return 0;
    }
}
