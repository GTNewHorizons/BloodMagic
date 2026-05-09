package WayofTime.alchemicalWizardry.api.items.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public interface ISigil extends IBindable {

    /**
     * Toggles the state of the sigil. Returns true if the sigil is active after the toggle or false if it is inactive
     * after the toggle.
     */
    default boolean toggleSigil(ItemStack item, World world, EntityPlayer player) {
        IBindable.setActive(item, !IBindable.isActive(item));

        if (!player.capabilities.isCreativeMode && !EnergyItems.syphonBatteries(item, player, this.drainCost())) {
            IBindable.setActive(item, false);
        }

        if (IBindable.isActive(item)) {
            item.setItemDamage(1);
            this.setDrainTick(item, world);
            return true;
        } else {
            item.setItemDamage(item.getMaxDamage());
            return false;
        }
    }

    /**
     * Return false if this sigil should not be able to be put in the Sigil of Holding.
     */
    default boolean canBeStored() {
        return true;
    }
}
