package WayofTime.alchemicalWizardry.api.spell;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntitySpellProjectile extends Entity implements IProjectile {

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private int inTile = 0;
    private int inData = 0;
    private boolean inGround = false;
    /**
     * The owner of this arrow.
     */
    public EntityPlayer shootingEntity;

    private int ticksInAir = 0;
    private int ricochetCounter = 0;
    private boolean scheduledForDeath = false;
    private boolean isSilkTouch = false;

    // Custom variables
    private int maxRicochet = 0;
    private float damage = 1;
    public List<IProjectileImpactEffect> impactList = new ArrayList<>();
    private boolean penetration = false;
    public List<IProjectileUpdateEffect> updateEffectList = new ArrayList<>();
    public List<SpellEffect> spellEffectList = new LinkedList<>();
    private int blocksBroken = 0;

    public EntitySpellProjectile(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntitySpellProjectile(World world, EntityPlayer player) {
        super(world);
        shootingEntity = player;
        this.setSize(0.1F, 0.1F);
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
        this.setThrowableHeading(motionX, motionY, motionZ, 1.2f, 1.0F);
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(16, 0);
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float distance = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= distance;
        y /= distance;
        z /= distance;
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

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.performUpdateEffects();
        if (ticksInAir > 600) {
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
                }
            }
            if (closestPlayer != null) {
                shootingEntity = closestPlayer;
            }
        }
        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float horizontalDistance = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, horizontalDistance) * 180.0D / Math.PI);
        }
        Block block = worldObj.getBlock(xTile, yTile, zTile);

        if (block != null) {
            block.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB collision = block.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);

            if (collision != null && collision.isVecInside(APISpellHelper.createVec3(posX, posY, posZ))) {
                inGround = true;
            }
        }

        if (!inGround) {
            ++ticksInAir;

            if (ticksInAir == 2) {
                for (int particles = 0; particles < 3; particles++) {
                    this.doFiringParticles();
                }
            }

            Vec3 start = APISpellHelper.createVec3(posX, posY, posZ);
            Vec3 end = APISpellHelper.createVec3(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition movement = worldObj.func_147447_a(start, end, true, false, false);
            start = APISpellHelper.createVec3(posX, posY, posZ);
            end = APISpellHelper.createVec3(posX + motionX, posY + motionY, posZ + motionZ);

            if (movement != null) {
                end = APISpellHelper.createVec3(movement.hitVec.xCoord, movement.hitVec.yCoord, movement.hitVec.zCoord);
            }

            Entity closestEntity = null;
            List<Entity> entitiesInRange = worldObj.getEntitiesWithinAABBExcludingEntity(
                    this,
                    boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double closestDistance = 0.0D;

            for (Entity entity : entitiesInRange) {

                if (entity.canBeCollidedWith() && (entity != shootingEntity || ticksInAir >= 5)) {
                    AxisAlignedBB box = entity.boundingBox.expand(0.3F, 0.3F, 0.3F);
                    MovingObjectPosition intercept = box.calculateIntercept(start, end);

                    if (intercept != null) {
                        double distance = start.distanceTo(intercept.hitVec);

                        if (distance < closestDistance || closestDistance == 0.0D) {
                            closestEntity = entity;
                            closestDistance = distance;
                        }
                    }
                }
            }

            if (closestEntity != null) {
                movement = new MovingObjectPosition(closestEntity);
            }

            if (movement != null) {
                this.onImpact(movement);

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

    private void doFiringParticles() {
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

        NBTTagList effectList = new NBTTagList();

        for (SpellEffect eff : spellEffectList) {
            effectList.appendTag(eff.getTag());
        }

        tag.setTag("Effects", effectList);
        tag.setInteger("blocksBroken", blocksBroken);
        tag.setBoolean("isSilkTouch", isSilkTouch);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        xTile = tag.getShort("xTile");
        yTile = tag.getShort("yTile");
        zTile = tag.getShort("zTile");
        inTile = tag.getByte("inTile") & 255;
        inData = tag.getByte("inData") & 255;
        inGround = tag.getByte("inGround") == 1;
        blocksBroken = tag.getInteger("blocksBroken");
        isSilkTouch = tag.getBoolean("isSilkTouch");

        NBTTagList tagList = tag.getTagList("Effects", Constants.NBT.TAG_COMPOUND);

        List<SpellEffect> spellEffectList = new LinkedList<>();
        for (int i = 0; i < tagList.tagCount(); i++) {
            SpellEffect eff = SpellEffect.getEffectFromTag(tagList.getCompoundTagAt(i));
            if (eff != null) {
                spellEffectList.add(eff);
            }
        }
        this.spellEffectList = spellEffectList;

        // this.effectList = new LinkedList<>();
        // for (int i = 0; i < tagList.tagCount(); i++)
        // {
        // NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
        //
        // this.effectList.add(tag.getString("Class"));
        // }

        // SpellParadigmProjectile parad = SpellParadigmProjectile.getParadigmForStringArray(effectList);
        SpellParadigmProjectile parad = SpellParadigmProjectile.getParadigmForEffectArray(spellEffectList);
        parad.applyAllSpellEffects();
        parad.prepareProjectile(this);
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

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    private void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) return;
            this.onImpact(mop.entityHit);
            this.performEntityImpactEffects(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (!this.penetration) {
                this.groundImpact(mop.sideHit);
                this.performTileImpactEffects(mop);
            }
        }
    }

    private void onImpact(Entity mop) // TODO
    {
        if (mop == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causePlayerDamage(shootingEntity), 1);
            this.setDead();
        } else {
            doDamage(this.damage, mop);
        }
        spawnHitParticles("exorcism", 8);
        this.setDead();
    }

    private void spawnHitParticles(String string, int i) {
        for (int particles = 0; particles < i; particles++) {
            worldObj.spawnParticle(
                    "mobSpellAmbient",
                    posX + smallGauss(0.1D),
                    posY + smallGauss(0.1D),
                    posZ + smallGauss(0.1D),
                    posGauss(1.0F),
                    posGauss(1.0F),
                    0.0F);
        }
    }

    private void doDamage(float f, Entity mop) {
        mop.attackEntityFrom(this.getDamageSource(), f);
    }

    private DamageSource getDamageSource() {
        return DamageSource.causePlayerDamage(shootingEntity);
    }

    private void groundImpact(int sideHit) {
        this.ricochet(sideHit);
    }

    private double smallGauss(double d) {
        return (worldObj.rand.nextFloat() - 0.5D) * d;
    }

    private double posGauss(double d) {
        return rand.nextFloat() * 0.5D * d;
    }

    private double gaussian(double d) {
        return d + d * ((rand.nextFloat() - 0.5D) / 4);
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
                    case 1, 3, 5 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                    case 2 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), -gaussian(0.1D));
                    case 4 -> worldObj
                            .spawnParticle("smoke", posX, posY, posZ, -gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                }
            }
        }
    }

    // Custom stuff
    public int getRicochetMax() {
        return this.maxRicochet;
    }

    public void setRicochetMax(int ricochet) {
        this.maxRicochet = ricochet;
    }

    public void setImpactList(List<IProjectileImpactEffect> list) {
        this.impactList = list;
    }

    public void setUpdateEffectList(List<IProjectileUpdateEffect> list) {
        this.updateEffectList = list;
    }

    private void performEntityImpactEffects(Entity mop) {
        if (impactList != null) {
            for (IProjectileImpactEffect impactEffect : impactList) {
                impactEffect.onEntityImpact(mop, this);
            }
        }
    }

    private void performTileImpactEffects(MovingObjectPosition mop) {
        if (impactList != null) {
            for (IProjectileImpactEffect impactEffect : impactList) {
                impactEffect.onTileImpact(worldObj, mop);
            }
        }
    }

    private void performUpdateEffects() {
        if (updateEffectList != null) {
            for (IProjectileUpdateEffect updateEffect : updateEffectList) {
                updateEffect.onUpdateEffect(this);
            }
        }
    }

    public void setPenetration(boolean penetration) {
        this.penetration = penetration;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setSpellEffectList(List<SpellEffect> list) {
        this.spellEffectList = list;
    }

    public int getBlocksBroken() {
        return this.blocksBroken;
    }

    public void setBlocksBroken(int blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public boolean getIsSilkTouch() {
        return this.isSilkTouch;
    }

    public void setIsSilkTouch(boolean bool) {
        this.isSilkTouch = bool;
    }
}
