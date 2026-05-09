package WayofTime.alchemicalWizardry.common.entity.mob;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;

public class EntityElemental extends EntityDemon {

    private final EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(
            this,
            EntityPlayer.class,
            1.2D,
            false);

    private static final float maxTamedHealth = 100.0F;
    private static final float maxUntamedHealth = 100.0F;

    public EntityElemental(World world, String entityAirElementalID) {
        super(world, entityAirElementalID);
        this.setSize(0.5F, 1.0F);
        this.setAggro(false);
        this.setTamed(false);

        if (world != null && !world.isRemote) {
            this.setCombatTask();
        }
    }

    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private Entity targetedEntity;

    /**
     * Cooldown time between target loss and new target aquirement.
     */
    private int aggroCooldown;

    public int prevAttackCounter;
    public int attackCounter;

    @Override
    protected void dropFewItems(boolean par1, int par2) {
        if (worldObj.rand.nextFloat() < (1 - Math.pow(0.6f, par2 + 1))) {
            this.entityDropItem(new ItemStack(ModItems.demonBloodShard, 1, 0), 0.0f);
        }
    }

    @Override
    protected void fall(float par1) {}

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground. Args: distanceFallenThisTick, onGround
     */
    @Override
    protected void updateFallState(double par1, boolean par3) {}

    @Override
    public void moveEntityWithHeading(float par1, float par2) {
        if (this.isInWater()) {
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8D;
            this.motionY *= 0.8D;
            this.motionZ *= 0.8D;
        } else if (this.handleLavaMovement()) {
            this.moveFlying(par1, par2, 0.02F);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.546F;
                Block i = this.worldObj.getBlock(
                        MathHelper.floor_double(this.posX),
                        MathHelper.floor_double(this.boundingBox.minY) - 1,
                        MathHelper.floor_double(this.posZ));

                if (i != null) {
                    f2 = i.slipperiness * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveFlying(par1, par2, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.546F;
                Block j = this.worldObj.getBlock(
                        MathHelper.floor_double(this.posX),
                        MathHelper.floor_double(this.boundingBox.minY) - 1,
                        MathHelper.floor_double(this.posZ));

                if (j != null) {
                    f2 = j.slipperiness * 0.91F;
                }
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= f2;
            this.motionY *= f2;
            this.motionZ *= f2;
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    protected void updateEntityActionState() {
        if (this.getHealth() <= this.getMaxHealth() / 2.0f && worldObj.rand.nextInt(200) == 0) {
            this.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionReciprocation.id, 100, 1));
        }

        this.prevAttackCounter = this.attackCounter;
        double dx = this.waypointX - this.posX;
        double dy = this.waypointY - this.posY;
        double dz = this.waypointZ - this.posZ;
        double dist = dx * dx + dy * dy + dz * dz;

        if (dist < 1.0D || dist > 3600.0D) {
            this.waypointX = this.posX + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointY = this.posY + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.waypointZ = this.posZ + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            dist = MathHelper.sqrt_double(dist);

            if (this.isCourseTraversable(dist)) {
                this.motionX += dx / dist * 0.1D;
                this.motionY += dy / dist * 0.1D;
                this.motionZ += dz / dist * 0.1D;
            } else {
                this.waypointX = this.posX + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.waypointY = this.posY + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.waypointZ = this.posZ + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
            }
        }

        if (this.targetedEntity != null && this.targetedEntity.isDead) {
            this.targetedEntity = null;
        }

        if (this.targetedEntity == null || this.aggroCooldown-- <= 0) {
            this.targetedEntity = getClosestVulnerableMonsterToEntity(this, 100.0D);

            if (this.targetedEntity != null) {
                this.aggroCooldown = 20;
            }
        }

        if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < 4096D) {
            double x = this.targetedEntity.posX - this.posX;
            double y = this.targetedEntity.boundingBox.minY + (double) (this.targetedEntity.height / 2.0F)
                    - (this.posY + (double) (this.height / 2.0F));
            double z = this.targetedEntity.posZ - this.posZ;
            this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(x, z)) * 180.0F / (float) Math.PI;

            double distance = Math.sqrt(x * x + y * y + z * z);
            if (this.courseChangeCooldown <= 0) {
                if (isCourseTraversable(distance)) {
                    this.waypointX = this.targetedEntity.posX;
                    this.waypointY = this.targetedEntity.posY;
                    this.waypointZ = this.targetedEntity.posZ;
                } else {
                    this.waypointX = this.posX + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    this.waypointY = this.posY + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    this.waypointZ = this.posZ + (double) ((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
                }
                this.motionX += x / dist * 0.1D;
                this.motionY += y / dist * 0.1D;
                this.motionZ += z / dist * 0.1D;
            }

            if (this.canEntityBeSeen(this.targetedEntity)) {
                if (distance < 4) {
                    ++this.attackCounter;

                    if (this.attackCounter >= 10) {
                        this.worldObj
                                .playAuxSFXAtEntity(null, 1008, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
                        this.inflictEffectOnEntity(this.targetedEntity);
                        this.attackCounter = -40;
                    }
                }
            } else if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        } else {
            this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(motionX, this.motionZ)) * 180.0F
                    / (float) Math.PI;

            if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        }
    }

    /**
     * True if the ghast has an unobstructed line of travel to the waypoint.
     */
    private boolean isCourseTraversable(double par7) {
        double d4 = (this.waypointX - this.posX) / par7;
        double d5 = (this.waypointY - this.posY) / par7;
        double d6 = (this.waypointZ - this.posZ) / par7;
        AxisAlignedBB axisalignedbb = this.boundingBox.copy();

        for (int i = 1; (double) i < par7; ++i) {
            axisalignedbb.offset(d4, d5, d6);

            if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setCombatTask();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
    public boolean isAIEnabled() {
        return false;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    @Override
    public void setAttackTarget(EntityLivingBase entityLivingBase) {
        super.setAttackTarget(entityLivingBase);
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    @Override
    protected void updateAITick() {
        this.dataWatcher.updateObject(18, this.getHealth());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, this.getHealth());
        this.dataWatcher.addObject(19, 0);
    }

    protected void playStepSound(int par1, int par2, int par3, int par4) {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    @Override
    protected String getLivingSound() {
        // TODO change sounds
        return "none";
    }

    @Override
    protected String getHurtSound() {
        return "none";
    }

    @Override
    protected String getDeathSound() {
        return "none";
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        int i = this.isTamed() ? 6 : 7;
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) i);
    }

    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.addTask(4, this.aiAttackOnCollide);
    }

    public void inflictEffectOnEntity(Entity target) {
        if (target instanceof EntityLivingBase) {
            ((EntityLivingBase) target)
                    .addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 100, 0));
        }
    }

    public static Entity getClosestVulnerableMonsterToEntity(Entity entity, double par2) {
        double d4 = -1.0D;
        double par1 = entity.posX;
        double par3 = entity.posY;
        double par5 = entity.posZ;

        EntityLivingBase entityLiving = null;
        World world = entity.worldObj;

        double range = Math.sqrt(par2);
        double verticalRange = Math.sqrt(par2);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(
                EntityLivingBase.class,
                AxisAlignedBB
                        .getBoundingBox(par1 - 0.5f, par3 - 0.5f, par5 - 0.5f, par1 + 0.5f, par3 + 0.5f, par5 + 0.5f)
                        .expand(range, verticalRange, range));
        if (entities == null) {
            return null;
        }

        for (EntityLivingBase entityLiving1 : entities) {
            if (!(entityLiving1 instanceof EntityPlayer player && player.capabilities.disableDamage)
                    && entityLiving1.isEntityAlive()) {
                double d5 = entityLiving1.getDistanceSq(par1, par3, par5);
                double d6 = par2;

                if (entityLiving1.isSneaking()) {
                    d6 = par2 * 0.8D;
                }

                if (entityLiving1.isInvisible()) {
                    float f = entityLiving1 instanceof EntityPlayer
                            ? ((EntityPlayer) entityLiving1).getArmorVisibility()
                            : 1.0f;

                    if (f < 0.1F) {
                        f = 0.1F;
                    }

                    d6 *= 0.7F * f;
                }

                if ((par2 < 0.0D || d5 < d6 * d6) && (d4 == -1.0D || d5 < d4)) {
                    if (entity != entityLiving1) {
                        d4 = d5;
                        entityLiving = entityLiving1;
                    }
                }
            }
        }

        return entityLiving;
    }

    @Override
    public int getTotalArmorValue() {
        return 10;
    }

    @Override
    protected float maxTamedHealth() {
        return maxTamedHealth;
    }

    @Override
    protected float maxUntamedHealth() {
        return maxUntamedHealth;
    }
}
