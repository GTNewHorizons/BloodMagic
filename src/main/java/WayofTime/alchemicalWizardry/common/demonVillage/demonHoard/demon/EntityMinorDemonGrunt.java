package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.LocalRitualStorage;
import WayofTime.alchemicalWizardry.common.EntityAITargetAggroCloaking;
import WayofTime.alchemicalWizardry.common.demonVillage.ai.EntityAIOccasionalRangedAttack;
import WayofTime.alchemicalWizardry.common.demonVillage.ai.EntityDemonAIHurtByTarget;
import WayofTime.alchemicalWizardry.common.demonVillage.ai.IOccasionalRangedAttackMob;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;
import WayofTime.alchemicalWizardry.common.rituals.LocalStorageAlphaPact;

public class EntityMinorDemonGrunt extends EntityDemon implements IOccasionalRangedAttackMob, IHoardDemon {

    private final EntityAIOccasionalRangedAttack aiArrowAttack = new EntityAIOccasionalRangedAttack(
            this,
            1.0D,
            40,
            40,
            15.0F,
            5);
    private final EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(
            this,
            EntityPlayer.class,
            1.2D,
            false);
    private Int3 demonPortal;

    private static final float maxTamedHealth = 200.0F;
    private static final float maxUntamedHealth = 200.0F;

    private boolean enthralled = false;

    public EntityMinorDemonGrunt(World world) {
        super(world, AlchemicalWizardry.entityMinorDemonGruntID);
        this.setSize(0.7F, 1.8F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityDemonAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetAggroCloaking(this, EntityPlayer.class, 0, false, 0));
        this.setAggro(false);
        this.setTamed(false);

        demonPortal = new Int3(0, 0, 0);

        if (world != null && !world.isRemote) {
            this.setCombatTask();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    }

    @Override
    protected void dropFewItems(boolean par1, int par2) {
        if (this.getDoesDropCrystal()) {
            super.dropFewItems(par1, par2);
            return;
        }
        ItemStack lifeShardStack = new ItemStack(ModItems.baseItems, 1, 28);
        ItemStack soulShardStack = new ItemStack(ModItems.baseItems, 1, 29);

        int dropAmount = 0;

        for (int i = 0; i <= par2; i++) {
            dropAmount += this.worldObj.rand.nextFloat() < 0.6f ? 1 : 0;
        }

        ItemStack drop = this.worldObj.rand.nextBoolean() ? lifeShardStack : soulShardStack;
        drop.stackSize = dropAmount;

        if (dropAmount > 0) {
            this.entityDropItem(drop, 0.0f);
        }
    }

    @Override
    public void setPortalLocation(Int3 position) {
        this.demonPortal = position;
    }

    @Override
    public Int3 getPortalLocation() {
        return this.demonPortal;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
    public boolean isAIEnabled() {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        this.demonPortal.writeToNBT(tag);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.demonPortal = Int3.readFromNBT(tag);
        this.setCombatTask();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        // TODO change sounds
        return "none";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "none";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        if (!this.enthralled) {
            TileEntity tile = this.worldObj
                    .getTileEntity(this.demonPortal.xCoord, this.demonPortal.yCoord, this.demonPortal.zCoord);
            if (tile instanceof TEDemonPortal) {
                ((TEDemonPortal) tile).enthrallDemon(this);
                this.enthralled = true;
            } else if (tile instanceof IMasterRitualStone stone) {
                LocalRitualStorage stor = stone.getLocalStorage();
                if (stor instanceof LocalStorageAlphaPact storage) {
                    storage.thrallDemon(this);
                }
            }
        }
        super.onUpdate();
    }

    @Override
    public boolean canMateWith(EntityAnimal entityAnimal) {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (friendlyDemon(entity)) {
            return false;
        }

        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) 20);
    }

    protected boolean friendlyDemon(Entity entity) {
        return entity instanceof IHoardDemon demon && demon.isSamePortal(this);
    }

    /**
     * Attack the specified entity using a ranged attack.
     */
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float par2) {
        if (friendlyDemon(entity)) {
            return;
        }
        this.worldObj.spawnEntityInWorld(attackProjectile(entity));
    }

    protected EnergyBlastProjectile attackProjectile(EntityLivingBase entity) {
        return new HolyProjectile(worldObj, this, entity, 1.8f, 0f, 15, 600);
    }

    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        this.tasks.addTask(4, this.aiArrowAttack);
        this.tasks.addTask(5, this.aiAttackOnCollide);
    }

    @Override
    public boolean shouldUseRangedAttack() {
        return true;
    }

    @Override
    public boolean thrallDemon(Int3 location) {
        this.setPortalLocation(location);
        return true;
    }

    @Override
    public boolean isSamePortal(IHoardDemon demon) {
        Int3 position = demon.getPortalLocation();
        TileEntity portal = worldObj
                .getTileEntity(this.demonPortal.xCoord, this.demonPortal.yCoord, this.demonPortal.zCoord);

        return portal instanceof TEDemonPortal
                && portal == worldObj.getTileEntity(position.xCoord, position.yCoord, position.zCoord);
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
