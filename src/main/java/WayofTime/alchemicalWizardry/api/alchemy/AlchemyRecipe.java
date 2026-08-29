package WayofTime.alchemicalWizardry.api.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AlchemyRecipe {

    public static final int MAX_INPUT_SLOTS = 5;

    private final ItemStack output;
    private final ItemStack[] recipe;
    private final int bloodOrbLevel;
    private final int amountNeeded;

    public AlchemyRecipe(ItemStack output, int amountNeeded, ItemStack[] recipe, int bloodOrbLevel) {
        this.output = output;
        this.recipe = recipe;
        this.amountNeeded = amountNeeded;
        this.bloodOrbLevel = bloodOrbLevel;
    }

    public boolean doesRecipeMatch(ItemStack[] items, int slottedBloodOrbLevel) {
        return getSlotUsage(items, slottedBloodOrbLevel) != null;
    }

    /**
     * Matches the recipe against the input slots and returns how many items have to be taken from each slot, or null if
     * the recipe does not match. Duplicate ingredients may come from a single slot holding a large enough stack, and
     * every non-empty slot has to be part of the recipe.
     */
    public int[] getSlotUsage(ItemStack[] items, int slottedBloodOrbLevel) {
        if (slottedBloodOrbLevel < bloodOrbLevel) {
            return null;
        }

        if (items.length < MAX_INPUT_SLOTS) {
            return null;
        }

        int[] usage = new int[MAX_INPUT_SLOTS];

        // ponytail: wildcard ingredients are assigned last so they cannot steal a slot an exact ingredient needs.
        // A full assignment search would only be needed for ingredients overlapping in more complex ways.
        for (int pass = 0; pass < 2; pass++) {
            for (ItemStack ingredient : recipe) {
                if (ingredient == null) {
                    continue;
                }

                boolean isWildcard = ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE;

                if (isWildcard != (pass == 1)) {
                    continue;
                }

                if (!assignIngredient(items, usage, ingredient)) {
                    return null;
                }
            }
        }

        for (int i = 0; i < MAX_INPUT_SLOTS; i++) {
            if (items[i] != null && usage[i] == 0) {
                return null;
            }
        }

        return usage;
    }

    private static boolean assignIngredient(ItemStack[] items, int[] usage, ItemStack ingredient) {
        for (int i = 0; i < MAX_INPUT_SLOTS; i++) {
            ItemStack slotStack = items[i];

            if (slotStack == null || slotStack.stackSize - usage[i] <= 0) {
                continue;
            }

            if (matches(slotStack, ingredient)) {
                usage[i]++;
                return true;
            }
        }

        return false;
    }

    private static boolean matches(ItemStack slotStack, ItemStack ingredient) {
        return slotStack.getItem() == ingredient.getItem()
                && (ingredient.getItemDamage() == OreDictionary.WILDCARD_VALUE
                        || slotStack.getItemDamage() == ingredient.getItemDamage());
    }

    public ItemStack getResult() {
        return output.copy();
    }

    public int getAmountNeeded() {
        return this.amountNeeded;
    }

    public ItemStack[] getRecipe() {
        return this.recipe;
    }

    public int getOrbLevel() {
        return this.bloodOrbLevel;
    }
}
