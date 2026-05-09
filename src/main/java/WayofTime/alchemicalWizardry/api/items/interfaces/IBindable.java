package WayofTime.alchemicalWizardry.api.items.interfaces;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public interface IBindable {

    /**
     * Used by bound tools and the energy blaster/bazooka to check if they should skip their right click function.
     * Drains the soul network by the amount returned by rightClickCost.
     */
    default boolean checkRightClick(ItemStack item, World world, EntityPlayer player) {
        if (toggle(item, world, player)) {
            return true;
        }

        if (world.isRemote) {
            return true;
        }

        if (!isActive(item) || SpellHelper.isFakePlayer(world, player)) {
            return true;
        }

        if (isBoundTool() && AlchemicalWizardry.disableBoundToolsRightClick) {
            return true;
        }

        if (player.isPotionActive(AlchemicalWizardry.customPotionInhibit)) {
            return true;
        }

        if (!(player.capabilities.isCreativeMode || EnergyItems.syphonBatteries(item, player, rightClickCost()))) {
            return true;
        }

        return false;
    }

    /**
     * Adds the active state and owner information to an item if it has the relevant tags.
     */
    default void addBindingInformation(ItemStack item, List<String> tooltip) {
        NBTTagCompound itemTag = getTag(item);
        if (itemTag.getBoolean("isActive")) {
            tooltip.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
        } else if (itemTag.hasKey("isActive")) { // Only if present and set to false rather than not present at all
            tooltip.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
        }

        if (!itemTag.getString("ownerName").isEmpty()) {
            tooltip.add(
                    StatCollector.translateToLocal("tooltip.owner.currentowner") + " "
                            + itemTag.getString("ownerName"));
        }
    }

    static void setActive(ItemStack item, boolean state) {
        NBTTagCompound itemTag = getTag(item);
        itemTag.setBoolean("isActive", state);
    }

    static boolean isActive(ItemStack item) {
        NBTTagCompound itemTag = getTag(item);
        return itemTag.getBoolean("isActive");
    }

    static NBTTagCompound getTag(ItemStack item) {
        NBTTagCompound itemTag = item.getTagCompound();

        if (itemTag == null) {
            item.setTagCompound(new NBTTagCompound());
            itemTag = item.getTagCompound();
        }
        return itemTag;
    }

    default boolean toggle(ItemStack item, World world, EntityPlayer player) {
        if (!checkAndSetItemOwner(item, player) || player.isSneaking()) {
            setActive(item, !isActive(item));
            setDrainTick(item, world);
            return true;
        }
        return false;
    }

    /**
     * Checks if the current tick is the proper tick to do a passive drain based on getDrainTicks. If it is, it runs
     * doPassiveDrain and returns true. Otherwise, returns false.
     */
    default boolean checkPassiveDrain(ItemStack item, World world, EntityPlayer player) {
        NBTTagCompound itemTag = getTag(item);
        if (world.getTotalWorldTime() % this.drainTicks() == itemTag.getInteger("worldTimeDelay")
                && itemTag.getBoolean("isActive")) {
            this.doPassiveDrain(item, player);
            return true;
        }
        return false;
    }

    /**
     * The action to take when checkPassiveDrain is run on the proper tick based on getDrainTicks.
     */
    default void doPassiveDrain(ItemStack item, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            if (!EnergyItems.syphonBatteries(item, player, drainCost())) {
                setActive(item, false);
            }
        }
    }

    /**
     * Sets the drain tick delay based on the current world age and getDrainTicks().
     */
    default void setDrainTick(ItemStack item, World world) {
        getTag(item).setInteger("worldTimeDelay", (int) (world.getTotalWorldTime() - 1) % this.drainTicks());
    }

    static boolean checkAndSetItemOwner(ItemStack item, EntityPlayer player) {
        return !SpellHelper.isFakePlayer(player) && SoulNetworkHandler.checkAndSetItemPlayer(item, player);
    }

    /**
     * Sets the item's owner even if it already has one.
     */
    static void setItemOwner(ItemStack item, String ownerName) {
        NBTTagCompound tag = IBindable.getTag(item);
        tag.setString("ownerName", ownerName);
    }

    /**
     * Sets the item's owner only if it does not already have one.
     */
    static void checkAndSetItemOwner(ItemStack item, String ownerName) {
        NBTTagCompound tag = IBindable.getTag(item);
        if (tag.getString("ownerName").isEmpty()) {
            tag.setString("ownerName", ownerName);
        }
    }

    static String getOwnerName(ItemStack item) {
        NBTTagCompound tag = IBindable.getTag(item);
        return tag.getString("ownerName");
    }

    /**
     * The number of ticks between passive drains from the soul network while the item is active.
     */
    default int drainTicks() {
        return 200;
    }

    /**
     * Return true if the right click function should be disabled with the config disableBoundToolsRightClick.
     */
    default boolean isBoundTool() {
        return false;
    }

    /**
     * The cost to use the right click function of this item. Used by bound tools and the Energy Blaster/Bazooka.
     */
    default int rightClickCost() {
        return 0;
    }

    /**
     * The amount of LP drained from the soul network for each passive drain while the item is active.
     */
    default int drainCost() {
        return 0;
    }
}
