package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Shamelessly ripped off from x3n0ph0b3
public class EntityParticleBeam extends Entity implements IProjectile, IThrowableEntity {

    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected int inTile = 0;
    protected int inData = 0;
    protected float colourRed = 0f;
    protected float colourGreen = 0f;
    protected float colourBlue = 0f;
    protected int xDest = 0;
    protected int yDest = 0;
    protected int zDest = 0;
    protected boolean inGround = false;
    /**
     * The owner of this arrow.
     */
    public EntityLivingBase shootingEntity;

    protected int ticksInAir = 0;
    protected int maxTicksInAir = 600;
    private boolean scheduledForDeath = false;
    protected int projectileDamage;

    public EntityParticleBeam(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.maxTicksInAir = 600;
    }

    public EntityParticleBeam(World world, double x, double y, double z) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        yOffset = 0.0F;
        this.maxTicksInAir = 600;
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
        float var9 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= var9;
        y /= var9;
        z /= var9;
        x += rand.nextGaussian() * 0.0075D * inaccuracy;
        y += rand.nextGaussian() * 0.0075D * inaccuracy;
        z += rand.nextGaussian() * 0.0075D * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        motionX = x;
        motionY = y;
        motionZ = z;
        float horizontalDistance = MathHelper.sqrt_double(x * x + z * z);
        prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
        prevRotationPitch = rotationPitch = (float) (Math.atan2(y, horizontalDistance) * 180.0D / Math.PI);
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
            float horizontalDistance = MathHelper.sqrt_double(x * x + z * z);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(y, horizontalDistance) * 180.0D / Math.PI);
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

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        this.setPosition(posX, posY, posZ);

        this.doFiringParticles();

        if (Math.pow(posX - xDest, 2) + Math.pow(posY - yDest, 2) + Math.pow(posZ - zDest, 2) <= 1) {
            this.scheduledForDeath = true;
        }

        if (this.scheduledForDeath) {
            this.setDead();
        }
    }

    public void doFiringParticles() {
        if (!worldObj.isRemote) {
            return;
        }
        EntityFX particle = new EntityCloudFX(worldObj, posX, posY, posZ, 0, 0, 0);
        particle.setRBGColorF(
                colourRed + 0.15f * (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()),
                colourGreen + 0.15f * (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()),
                colourBlue + 0.15f * (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()));
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
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
        tag.setFloat("colourRed", colourRed);
        tag.setFloat("colourGreen", colourGreen);
        tag.setFloat("colourBlue", colourBlue);
        tag.setInteger("xDest", xDest);
        tag.setInteger("yDest", yDest);
        tag.setInteger("zDest", zDest);
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
        colourRed = tag.getFloat("colourRed");
        colourGreen = tag.getFloat("colourGreen");
        colourBlue = tag.getFloat("colourBlue");
        xDest = tag.getInteger("xDest");
        yDest = tag.getInteger("yDest");
        zDest = tag.getInteger("zDest");
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    protected void spawnHitParticles(String string, int i) {
        for (int particles = 0; particles < i; particles++) {
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
        // TODO Auto-generated method stub
        return this.shootingEntity;
    }

    @Override
    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase) this.shootingEntity = (EntityLivingBase) entity;
    }

    public void setColour(float red, float green, float blue) {
        this.colourRed = red;
        this.colourGreen = green;
        this.colourBlue = blue;
    }

    public void setDestination(int xDest, int yDest, int zDest) {
        this.xDest = xDest;
        this.yDest = yDest;
        this.zDest = zDest;
    }
}
