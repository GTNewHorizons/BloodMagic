package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class MudProjectile extends EnergyBlastProjectile {

    private boolean doesBlindness; // True for when it applies blindness, false for slowness

    public MudProjectile(World world) {
        super(world);
    }

    public MudProjectile(World world, EntityLivingBase player, int damage, boolean doesBlindness) {
        super(world, player, damage);
        this.doesBlindness = doesBlindness;
    }

    public MudProjectile(World world, EntityLivingBase player, int damage, int maxTicksInAir, double posX, double posY,
            double posZ, float rotationYaw, float rotationPitch, boolean flag) {
        super(world, player, damage, maxTicksInAir, posX, posY, posZ, rotationYaw, rotationPitch);
        doesBlindness = flag;
    }

    public MudProjectile(World world, EntityLivingBase shooter, EntityLivingBase target, float par4, float par5,
            int damage, int maxTicksInAir, boolean flag) {
        super(world, shooter, target, par4, par5, damage, maxTicksInAir);
        doesBlindness = flag;
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
            if (doesBlindness) {
                entity.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
            } else {
                entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 2));
            }
            doDamage(projectileDamage, target);
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
                0.5F,
                0.297F,
                0.0664F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("doesBlindness", doesBlindness);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        doesBlindness = tag.getBoolean("doesBlindness");
    }
}
