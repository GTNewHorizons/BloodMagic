package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellTeleport;

public class TeleportProjectile extends EnergyBlastProjectile {

    private boolean isEntityTeleport;

    public TeleportProjectile(World world) {
        super(world);
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    public TeleportProjectile(World world, EntityLivingBase player, int damage, boolean isEntityTeleport) {
        super(world, player, damage);
        this.isEntityTeleport = isEntityTeleport;
        this.motionX *= 3;
        this.motionY *= 3;
        this.motionZ *= 3;
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (isEntityTeleport && shootingEntity instanceof EntityPlayerMP entityplayermp
                    && entityplayermp.worldObj == this.worldObj) {
                EnderTeleportEvent event = new EnderTeleportEvent(
                        entityplayermp,
                        this.posX,
                        this.posY,
                        this.posZ,
                        5.0F);

                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    if (shootingEntity.isRiding()) {
                        shootingEntity.mountEntity(null);
                    }
                    shootingEntity.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                }
            }
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            this.setDead();
            spawnHitParticles("magicCrit", 8);
            return;
        }
        if (target instanceof EntityLivingBase) {
            if (isEntityTeleport) {
                if (shootingEntity != null && shootingEntity instanceof EntityPlayerMP player) {
                    if (player.worldObj == this.worldObj) {
                        EnderTeleportEvent event = new EnderTeleportEvent(
                                player,
                                this.posX,
                                this.posY,
                                this.posZ,
                                5.0F);
                        if (!MinecraftForge.EVENT_BUS.post(event)) {
                            if (shootingEntity.isRiding()) {
                                shootingEntity.mountEntity(null);
                            }

                            shootingEntity.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                        }
                    }
                }
            } else {
                SpellTeleport.teleportRandomly((EntityLivingBase) target, 64);
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
                "portal",
                posX,
                posY,
                posZ,
                -motionX,
                -motionY,
                -motionZ);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("isEntityTeleport", isEntityTeleport);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        isEntityTeleport = tag.getBoolean("isEntityTeleport");
    }
}
