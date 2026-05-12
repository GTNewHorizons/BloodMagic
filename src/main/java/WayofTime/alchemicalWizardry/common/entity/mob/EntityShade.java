package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.EntityAITargetAggro;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;

public class EntityShade extends EntityDemon {

    private final EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(
            this,
            EntityPlayer.class,
            1.2D,
            false);

    private static final float maxTamedHealth = 50.0F;
    private static final float maxUntamedHealth = 100.0F;

    public EntityShade(World world) {
        super(world, AlchemicalWizardry.entityShadeID);
        this.setSize(0.8F, 2.0F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(4, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new EntityAITargetAggro(this, EntityPlayer.class, 0, false));
        this.setAggro(false);
        this.setTamed(false);

        if (world != null && !world.isRemote) {
            this.setCombatTask();
        }
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
        return true;
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
    }

    protected void playStepSound(int par1, int par2, int par3, int par4) {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("Angry", this.isAngry());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setAngry(tag.getBoolean("Angry"));
        this.setCombatTask();
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

    public void attackEntityWithRangedAttack(EntityLivingBase entityLivingBase, float par2) {
        HolyProjectile hol = new HolyProjectile(worldObj, this, entityLivingBase, 1.8f, 0f, 5, 600);
        this.worldObj.spawnEntityInWorld(hol);
    }

    /**
     * sets this entity's combat AI.
     */
    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        // this.tasks.removeTask(this.aiArrowAttack);
        this.tasks.addTask(4, this.aiAttackOnCollide);
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
