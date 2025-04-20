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
    default boolean toggleSigil(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        IBindable.setActive(par1ItemStack, !IBindable.isActive(par1ItemStack));

        if (!par3EntityPlayer.capabilities.isCreativeMode
                && !EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, this.drainCost())) {
            IBindable.setActive(par1ItemStack, false);
        }

        if (IBindable.isActive(par1ItemStack)) {
            par1ItemStack.setItemDamage(1);
            this.setDrainTick(par1ItemStack, par2World);
            return true;
        } else {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
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
