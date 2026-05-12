package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class WindGustProjectile extends EnergyBlastProjectile {

    public WindGustProjectile(World world) {
        super(world);
    }

    public WindGustProjectile(World world, EntityLivingBase player, int damage) {
        super(world, player, damage);
    }

    public WindGustProjectile(World world, EntityLivingBase shooter, EntityLivingBase target, float par4, float par5,
            int damage, int maxTicksInAir) {
        super(world, shooter, target, par4, par5, damage, maxTicksInAir);
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
        if (target == shootingEntity && ticksInAir > 3 || target == this.shootingEntity) {
            this.setDead();
        } else if (target instanceof EntityPlayer player) {
            SpellHelper.setPlayerSpeedFromServer(
                    player,
                    this.motionX * 0.25 * this.projectileDamage,
                    1.5,
                    this.motionZ * 0.25 * this.projectileDamage);
        } else if (target instanceof EntityLivingBase) {
            target.motionX += this.motionX * 0.25 * this.projectileDamage;
            target.motionY += 1.5;
            target.motionZ += this.motionZ * 0.25 * this.projectileDamage;
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
                "mobSpell",
                posX,
                posY,
                posZ,
                1.0F,
                1.0F,
                1.0F);
    }
}
