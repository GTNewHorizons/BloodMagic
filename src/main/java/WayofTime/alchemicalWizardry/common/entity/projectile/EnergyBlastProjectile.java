package WayofTime.alchemicalWizardry.common.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Shamelessly ripped off from x3n0ph0b3
public class EnergyBlastProjectile extends Entity implements IProjectile, IThrowableEntity {

    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected int inTile = 0;
    protected int inData = 0;
    protected boolean inGround = false;
    /**
     * The owner of this arrow.
     */
    public EntityLivingBase shootingEntity;

    protected int ticksInAir = 0;
    protected int maxTicksInAir = 600;
    protected int ricochetCounter = 0;
    protected boolean scheduledForDeath = false;
    protected int projectileDamage;

    public EnergyBlastProjectile(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.maxTicksInAir = 600;
    }

    public EnergyBlastProjectile(World world, double x, double y, double z) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        yOffset = 0.0F;
        this.maxTicksInAir = 600;
    }

    public EnergyBlastProjectile(World world, EntityLivingBase player, int damage) {
        super(world);
        shootingEntity = player;
        float par3 = 0.8F;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(
                player.posX,
                player.posY + player.getEyeHeight(),
                player.posZ,
                player.rotationYaw,
                player.rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        this.setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
        this.projectileDamage = damage;
        this.maxTicksInAir = 600;
    }

    public EnergyBlastProjectile(World world, EntityLivingBase player, int damage, int maxTicksInAir, double posX,
            double posY, double posZ, float rotationYaw, float rotationPitch) {
        super(world);
        shootingEntity = player;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        this.setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI)
                * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        this.setThrowableHeading(motionX, motionY, motionZ, 1.2f, 1.0F);
        this.projectileDamage = damage;
        this.maxTicksInAir = maxTicksInAir;
    }

    public EnergyBlastProjectile(World world, EntityLivingBase shooter, EntityLivingBase target, float velocity,
            float inaccuracy, int damage, int maxTicksInAir) {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = shooter;
        this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.1D;
        double deltaX = target.posX - shooter.posX;
        double deltaY = target.boundingBox.minY + (double) (target.height / 1.5F) - this.posY;
        double deltaZ = target.posZ - shooter.posZ;
        double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        if (horizontalDistance >= 1.0E-7D) {
            float yaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float) (-(Math.atan2(deltaY, horizontalDistance) * 180.0D / Math.PI));
            double normalizedX = deltaX / horizontalDistance;
            double normalizedZ = deltaZ / horizontalDistance;
            this.setLocationAndAngles(shooter.posX + normalizedX, this.posY, shooter.posZ + normalizedZ, yaw, pitch);
            this.yOffset = 0.0F;
            this.setThrowableHeading(deltaX, deltaY, deltaZ, velocity, inaccuracy);
        }

        this.projectileDamage = damage;
        this.maxTicksInAir = maxTicksInAir;
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(16, (byte) 0);
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float magnitude = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;

        double spread = 0.0075D;
        x += rand.nextGaussian() * spread * inaccuracy;
        y += rand.nextGaussian() * spread * inaccuracy;
        z += rand.nextGaussian() * spread * inaccuracy;

        x *= velocity;
        y *= velocity;
        z *= velocity;

        motionX = x;
        motionY = y;
        motionZ = z;

        float horizontalSpeed = MathHelper.sqrt_double(x * x + z * z);

        prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
        prevRotationPitch = rotationPitch = (float) (Math.atan2(y, horizontalSpeed) * 180.0D / Math.PI);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int rotationIncrements) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {
        motionX = x;
        motionY = y;
        motionZ = z;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            float horizontalSpeed = MathHelper.sqrt_double(x * x + z * z);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(y, horizontalSpeed) * 180.0D / Math.PI);
            prevRotationYaw = rotationYaw;
            this.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (ticksInAir > maxTicksInAir) {
            this.setDead();
        }

        if (shootingEntity == null) {
            List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(posX - 1, posY - 1, posZ - 1, posX + 1, posY + 1, posZ + 1));

            double closestDistance = Double.MAX_VALUE;
            EntityPlayer closestPlayer = null;

            for (EntityPlayer player : players) {
                double distance = player.getDistanceToEntity(this);

                if (distance < closestDistance) {
                    closestPlayer = player;
                    closestDistance = distance;
                }
            }

            if (closestPlayer != null) {
                shootingEntity = closestPlayer;
            }
        }

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float horizontalSpeed = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, horizontalSpeed) * 180.0D / Math.PI);
        }

        Block block = worldObj.getBlock(xTile, yTile, zTile);

        if (block != null) {
            block.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB collisionBox = block.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);

            if (collisionBox != null && collisionBox.isVecInside(SpellHelper.createVec3(posX, posY, posZ))) {
                inGround = true;
            }
        }

        if (!inGround) {
            ++ticksInAir;

            if (ticksInAir == 2) {
                for (int i = 0; i < 3; i++) {
                    this.doFiringParticles();
                }
            }

            Vec3 startVec = SpellHelper.createVec3(posX, posY, posZ);
            Vec3 endVec = SpellHelper.createVec3(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition blockHit = worldObj.func_147447_a(startVec, endVec, true, false, false);
            startVec = SpellHelper.createVec3(posX, posY, posZ);
            endVec = SpellHelper.createVec3(posX + motionX, posY + motionY, posZ + motionZ);

            if (blockHit != null) {
                endVec = SpellHelper.createVec3(blockHit.hitVec.xCoord, blockHit.hitVec.yCoord, blockHit.hitVec.zCoord);
            }

            Entity closestHitEntity = null;
            List<Entity> nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(
                    this,
                    boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));

            double closestHitDistance = 0.0D;

            for (Entity entity : nearbyEntities) {
                if (!entity.canBeCollidedWith() || (entity == shootingEntity && ticksInAir < 5)) {
                    continue;
                }

                AxisAlignedBB expandedBoundingBox = entity.boundingBox.expand(0.3F, 0.3F, 0.3F);

                MovingObjectPosition entityIntercept = expandedBoundingBox.calculateIntercept(startVec, endVec);
                if (entityIntercept != null) {
                    double hitDistance = startVec.distanceTo(entityIntercept.hitVec);
                    if (hitDistance < closestHitDistance || closestHitDistance == 0.0D) {
                        closestHitEntity = entity;
                        closestHitDistance = hitDistance;
                    }
                }
            }

            if (closestHitEntity != null) {
                blockHit = new MovingObjectPosition(closestHitEntity);
            }

            if (blockHit != null) {
                this.onImpact(blockHit);

                if (scheduledForDeath) {
                    this.setDead();
                }
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            this.setPosition(posX, posY, posZ);
        }
    }

    public void doFiringParticles() {
        worldObj.spawnParticle(
                "mobSpellAmbient",
                posX + smallGauss(0.1D),
                posY + smallGauss(0.1D),
                posZ + smallGauss(0.1D),
                0.5D,
                0.5D,
                0.5D);
        worldObj.spawnParticle("flame", posX, posY, posZ, gaussian(motionX), gaussian(motionY), gaussian(motionZ));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        tag.setShort("xTile", (short) xTile);
        tag.setShort("yTile", (short) yTile);
        tag.setShort("zTile", (short) zTile);
        tag.setByte("inTile", (byte) inTile);
        tag.setByte("inData", (byte) inData);
        tag.setByte("inGround", (byte) (inGround ? 1 : 0));
        tag.setInteger("ticksInAir", ticksInAir);
        tag.setInteger("maxTicksInAir", maxTicksInAir);
        tag.setInteger("projectileDamage", this.projectileDamage);
        tag.setInteger("ricochetCounter", this.ricochetCounter);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        xTile = tag.getShort("xTile");
        yTile = tag.getShort("yTile");
        zTile = tag.getShort("zTile");
        inTile = tag.getByte("inTile") & 255;
        inData = tag.getByte("inData") & 255;
        inGround = tag.getByte("inGround") == 1;
        ticksInAir = tag.getInteger("ticksInAir");
        maxTicksInAir = tag.getInteger("maxTicksInAir");
        projectileDamage = tag.getInteger("projectileDamage");
        ricochetCounter = tag.getInteger("ricochetCounter");
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            worldObj.createExplosion(shootingEntity, this.posX, this.posY, this.posZ, (float) (0.1), true);
            this.setDead();
        }
    }

    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else {
            if (target instanceof EntityLivingBase entity) {
                entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
            }
            doDamage(projectileDamage, target);
            worldObj.createExplosion(shootingEntity, this.posX, this.posY, this.posZ, (float) (0.1), true);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }

    protected void spawnHitParticles(String string, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
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

    protected void doDamage(int i, Entity mop) {
        mop.attackEntityFrom(this.getDamageSource(), i);
    }

    public DamageSource getDamageSource() {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    public double smallGauss(double d) {
        return (worldObj.rand.nextFloat() - 0.5D) * d;
    }

    public double gaussian(double d) {
        return d + d * ((rand.nextFloat() - 0.5D) / 4);
    }

    @Override
    public Entity getThrower() {
        return this.shootingEntity;
    }

    @Override
    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase e) this.shootingEntity = e;
    }
}
