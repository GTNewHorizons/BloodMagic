package WayofTime.alchemicalWizardry.api.alchemy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;

public class AlchemyRecipeRegistry {

    public static List<AlchemyRecipe> recipes = new ArrayList<>();

    public static void registerRecipe(ItemStack output, int amountNeeded, ItemStack[] recipe, int bloodOrbLevel) {
        recipes.add(new AlchemyRecipe(output, amountNeeded, recipe, bloodOrbLevel));
    }

    public static AlchemyRecipe findRecipe(ItemStack[] recipe, ItemStack bloodOrb) {
        if (bloodOrb == null) {
            return null;
        }

        if (!(bloodOrb.getItem() instanceof IBloodOrb)) {
            return null;
        }

        int bloodOrbLevel = ((IBloodOrb) bloodOrb.getItem()).getOrbLevel();

        for (AlchemyRecipe ar : recipes) {
            if (ar.doesRecipeMatch(recipe, bloodOrbLevel)) {
                return ar;
            }
        }

        return null;
    }

    public static ItemStack getResult(ItemStack[] recipe, ItemStack bloodOrb) {
        AlchemyRecipe ar = findRecipe(recipe, bloodOrb);

        return ar == null ? null : ar.getResult();
    }

    public static int getAmountNeeded(ItemStack[] recipe, ItemStack bloodOrb) {
        AlchemyRecipe ar = findRecipe(recipe, bloodOrb);

        return ar == null ? 0 : ar.getAmountNeeded();
    }

    public static ItemStack[] getRecipeForItemStack(ItemStack itemStack) {
        for (AlchemyRecipe ar : recipes) {
            ItemStack result = ar.getResult();

            if (result != null) {
                if (result.isItemEqual(itemStack)) {
                    return ar.getRecipe();
                }
            }
        }

        return null;
    }
}
