package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.common.IDemon;
import WayofTime.alchemicalWizardry.common.items.DemonPlacer;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class EntityDemon extends EntityTameable implements IDemon {

    private boolean isAggro;
    private String demonID;

    protected boolean dropCrystal = true;

    public EntityDemon(World world, String demonID) {
        super(world);
        this.demonID = demonID;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxTamedHealth());
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxUntamedHealth());
        }
    }

    @Override
    public boolean getDoesDropCrystal() {
        return dropCrystal;
    }

    @Override
    public void setDropCrystal(boolean crystal) {
        this.dropCrystal = crystal;
    }

    @Override
    public void setSummonedConditions() {
        this.setAggro(true);
    }

    @Override
    public boolean isAggro() {
        return this.isAggro;
    }

    @Override
    public void setAggro(boolean aggro) {
        this.isAggro = aggro;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable entityageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("dropCrystal", this.getDoesDropCrystal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.setDropCrystal(tag.getBoolean("dropCrystal"));
    }

    @Override
    protected void dropFewItems(boolean par1, int par2) {
        if (!this.getDoesDropCrystal()) {
            return;
        }
        ItemStack drop = new ItemStack(ModItems.demonPlacer);

        DemonPlacer.setDemonString(drop, this.getDemonID());

        if ((this.getOwner() instanceof EntityPlayer)) {
            DemonPlacer.setOwnerName(drop, SpellHelper.getUsername((EntityPlayer) this.getOwner()));
        }

        if (this.hasCustomNameTag()) {
            drop.setStackDisplayName(this.getCustomNameTag());
        }

        this.entityDropItem(drop, 0.0f);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.isAggro() && worldObj.getWorldTime() % 100 == 0) {
            this.heal(1);
        }
    }

    public void sendSittingMessageToPlayer(EntityPlayer owner, boolean isSitting) {
        if (owner != null && owner.worldObj.isRemote) {
            ChatComponentTranslation chatmessagecomponent;

            if (isSitting) {
                chatmessagecomponent = new ChatComponentTranslation("message.demon.willstay");
            } else {
                chatmessagecomponent = new ChatComponentTranslation("message.demon.shallfollow");
            }

            owner.addChatComponentMessage(chatmessagecomponent);
        }
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack item = player.inventory.getCurrentItem();

        if (this.isTamed()) {
            if (item != null) {
                if (item.getItem() instanceof ItemFood itemfood) {

                    if (itemfood.isWolfsFavoriteMeat()
                            && this.dataWatcher.getWatchableObjectFloat(18) < maxTamedHealth()) {
                        if (!player.capabilities.isCreativeMode) {
                            --item.stackSize;
                        }

                        this.heal((float) itemfood.func_150905_g(item));

                        if (item.stackSize <= 0) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }

                        return true;
                    }
                }
            }

            if (this.getOwner() instanceof EntityPlayer && SpellHelper.getUsername(player).equalsIgnoreCase(
                    SpellHelper.getUsername((EntityPlayer) this.getOwner())) && !this.isBreedingItem(item)) {
                if (!this.worldObj.isRemote) {
                    this.aiSit.setSitting(!this.isSitting());
                    this.isJumping = false;
                    this.setPathToEntity(null);
                    this.setTarget(null);
                    this.setAttackTarget(null);
                }

                this.sendSittingMessageToPlayer(player, !this.isSitting());
            }
        } else if (item != null && item.getItem() != null
                && item.getItem().equals(ModItems.weakBloodOrb)
                && !this.isAngry()) {
                    if (!player.capabilities.isCreativeMode) {
                        --item.stackSize;
                    }

                    if (item.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }

                    if (!this.worldObj.isRemote) {
                        this.rand.nextInt(1);
                        this.setTamed(true);
                        this.setPathToEntity(null);
                        this.setAttackTarget(null);
                        this.aiSit.setSitting(true);
                        this.setHealth(maxTamedHealth());
                        this.func_152115_b(player.getUniqueID().toString());
                        this.playTameEffect(true);
                        this.worldObj.setEntityState(this, (byte) 7);
                    }

                    return true;
                }

        return super.interact(player);
    }

    protected float maxTamedHealth() {
        return 1;
    }

    protected float maxUntamedHealth() {
        return 1;
    }

    @Override
    public boolean func_142018_a(EntityLivingBase target, EntityLivingBase owner) { // shouldAttackEntity
        if (target instanceof EntityCreeper || target instanceof EntityGhast) {
            return false;
        }
        if (target instanceof EntityTameable tameable) {
            if (tameable.isTamed() && tameable.getOwner() == owner) {
                return false;
            }
        }

        return (!(target instanceof EntityPlayer tPlayer) || !(owner instanceof EntityPlayer oPlayer)
                || oPlayer.canAttackPlayer(tPlayer))
                && (!(target instanceof EntityHorse) || !((EntityHorse) target).isTame());
    }

    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public void setAngry(boolean par1) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);

        if (par1) {
            this.dataWatcher.updateObject(16, (byte) (b0 | 2));
        } else {
            this.dataWatcher.updateObject(16, (byte) (b0 & -3));
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            Entity entity = par1DamageSource.getEntity();
            this.aiSit.setSitting(false);

            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                par2 = (par2 + 1.0F) / 2.0F;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    @Override
    public void setTamed(boolean par1) {
        super.setTamed(par1);

        if (par1) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxTamedHealth());
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxUntamedHealth());
        }
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    @Override
    public void setAttackTarget(EntityLivingBase entityLivingBase) {
        super.setAttackTarget(entityLivingBase);

        if (entityLivingBase == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack item) {
        return false;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    public String getDemonID() {
        return this.demonID;
    }

    protected void setDemonID(String id) {
        this.demonID = id;
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
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8F;
    }
}
