package WayofTime.alchemicalWizardry.api.util;

import cpw.mods.fml.common.registry.GameRegistry;
//import gregtech.api.GregTech_API;
//import gregtech.api.util.GT_Utility;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

//contains code borrowed from GT
//BTW did I need all of these? What's the replacement thing for?

public class BMModHandler {

    /**
     * Gets an Item from RailCraft
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount) {
        return getModItem(aModID, aItem, aAmount, null);
    }

    /**
     * Gets an Item from RailCraft, and returns a Replacement Item if not possible
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount, ItemStack aReplacement) {
        if (isStringInvalid(aItem) /*|| !GregTech_API.sPreloadStarted*/) return null; //what did this do? Is it important?
        return copyAmount(aAmount, GameRegistry.findItemStack(aModID, aItem, (int) aAmount), aReplacement);
    }

    /**
     * Gets an Item from RailCraft, but the Damage Value can be specified
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount, int aMeta) {
        ItemStack rStack = getModItem(aModID, aItem, aAmount);
        if (rStack == null) return null;
        Items.feather.setDamage(rStack, aMeta);
        return rStack;
    }

    /**
     * Gets an Item from RailCraft, but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
     */
    public static ItemStack getModItem(String aModID, String aItem, long aAmount, int aMeta, ItemStack aReplacement) {
        ItemStack rStack = getModItem(aModID, aItem, aAmount, aReplacement);
        if (rStack == null) return null;
        Items.feather.setDamage(rStack, aMeta);
        return rStack;
    }
    
    public static boolean isStringInvalid(Object aString) {
        return aString == null || aString.toString().isEmpty();
    }
        
    public static boolean isStackValid(Object aStack) {
        return (aStack instanceof ItemStack) && ((ItemStack) aStack).getItem() != null && ((ItemStack) aStack).stackSize >= 0;
    }
    
    public static boolean isStackInvalid(Object aStack) {
        return aStack == null || !(aStack instanceof ItemStack) || ((ItemStack) aStack).getItem() == null || ((ItemStack) aStack).stackSize < 0;
    }
    
    public static ItemStack copy(Object... aStacks) {
        for (Object tStack : aStacks) if (isStackValid(tStack)) return ((ItemStack) tStack).copy();
        return null;
    }
    
    public static ItemStack copyAmount(long aAmount, Object... aStacks) {
        ItemStack rStack = copy(aStacks);
        if (isStackInvalid(rStack)) return null;
        if (aAmount > 64) aAmount = 64;
        else if (aAmount == -1) aAmount = 111;
        else if (aAmount < 0) aAmount = 0;
        rStack.stackSize = (byte) aAmount;
        return rStack;
    }
}
