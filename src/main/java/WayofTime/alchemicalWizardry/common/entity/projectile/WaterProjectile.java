package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class WaterProjectile extends EnergyBlastProjectile {

    public WaterProjectile(World world) {
        super(world);
    }

    public WaterProjectile(World world, EntityLivingBase player, int damage) {
        super(world, player, damage);
    }

    public WaterProjectile(World world, EntityLivingBase player, int damage, int maxTicksInAir, double posX,
            double posY, double posZ, float rotationYaw, float rotationPitch) {
        super(world, player, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
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
            this.setDead();
        } else if (target instanceof EntityLivingBase entity) {
            if (target.isImmuneToFire()) {
                doDamage(projectileDamage * 2, target);
                entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 80, 1));
            } else {
                doDamage(projectileDamage, target);
                entity.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 80, 0));
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
                "portal",
                posX,
                posY,
                posZ,
                -motionX,
                -motionY,
                -motionZ);
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
    }
}
