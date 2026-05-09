package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistry;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DemonPlacer extends Item {

    public DemonPlacer() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.maxStackSize = 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack item, int par2) {
        EntityEggInfo entityegginfo = EntityList.entityEggs.get(item.getItemDamage());
        return entityegginfo != null ? (par2 == 0 ? entityegginfo.primaryColor : entityegginfo.secondaryColor)
                : 0xffffff;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int par4, int par5, int par6, int par7,
            float par8, float par9, float par10) {
        if (world.isRemote) {
            return true;
        }
        Block i1 = world.getBlock(par4, par5, par6);
        par4 += Facing.offsetsXForSide[par7];
        par5 += Facing.offsetsYForSide[par7];
        par6 += Facing.offsetsZForSide[par7];
        double d0 = 0.0D;

        if (par7 == 1 && i1 != null && i1.getRenderType() == 11) {
            d0 = 0.5D;
        }

        String demonName = DemonPlacer.getDemonString(item);
        Entity entity = spawnCreature(
                world,
                demonName,
                (double) par4 + 0.5D,
                (double) par5 + d0,
                (double) par6 + 0.5D,
                item);

        if (entity != null) {
            if (entity instanceof EntityLivingBase && item.hasDisplayName()) {
                ((EntityLiving) entity).setCustomNameTag(item.getDisplayName());
            }

            if (!player.capabilities.isCreativeMode) {
                --item.stackSize;
            }
        }

        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (world.isRemote) {
            return item;
        }
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

        if (movingobjectposition == null) {
            return item;
        } else {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k)) {
                    return item;
                }

                if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, item)) {
                    return item;
                }

                if (world.getBlock(i, j, k).getMaterial() == Material.water) {
                    String demonName = DemonPlacer.getDemonString(item);
                    Entity entity = spawnCreature(world, demonName, i, j, k, item);

                    if (entity != null) {
                        if (entity instanceof EntityLivingBase && item.hasDisplayName()) {
                            ((EntityLiving) entity).setCustomNameTag(item.getDisplayName());
                        }

                        if (!player.capabilities.isCreativeMode) {
                            --item.stackSize;
                        }
                    }
                }
            }

            return item;
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World world, String par1, double par2, double par4, double par6,
            ItemStack itemStack) {
        Entity entity = null;

        for (int j = 0; j < 1; ++j) {
            entity = SummoningRegistry.getEntityWithID(world, par1);

            if (entity instanceof EntityLivingBase) {
                EntityLiving entityliving = (EntityLiving) entity;
                entity.setLocationAndAngles(
                        par2,
                        par4,
                        par6,
                        MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F),
                        0.0F);
                entityliving.rotationYawHead = entityliving.rotationYaw;
                entityliving.renderYawOffset = entityliving.rotationYaw;
                if (entityliving instanceof EntityDemon) {
                    Entity owner = SpellHelper.getPlayerForUsername(DemonPlacer.getOwnerName(itemStack));
                    if (owner != null) {
                        ((EntityDemon) entityliving).func_152115_b(owner.getPersistentID().toString());

                        if (!DemonPlacer.getOwnerName(itemStack).isEmpty()) {
                            ((EntityDemon) entityliving).setTamed(true);
                        }
                    }
                }

                world.spawnEntityInWorld(entity);
                entityliving.playLivingSound();
            }
        }
        return entity;
    }

    public static void setOwnerName(ItemStack item, String ownerName) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        item.getTagCompound().setString("ownerName", ownerName);
    }

    public static String getOwnerName(ItemStack item) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        return item.getTagCompound().getString("ownerName");
    }

    public static void setDemonString(ItemStack itemStack, String demonName) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setString("demonName", demonName);
    }

    public static String getDemonString(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getString("demonName");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.demonplacer.desc"));

        if (!(item.getTagCompound() == null)) {
            if (!item.getTagCompound().getString("ownerName").isEmpty()) {
                tooltip.add(
                        StatCollector.translateToLocal("tooltip.owner.demonsowner") + " "
                                + item.getTagCompound().getString("ownerName"));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DemonPlacer");
    }
}
