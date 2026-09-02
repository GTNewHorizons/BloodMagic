package WayofTime.alchemicalWizardry.api.alchemy;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AlchemyRecipe {

    private static final int MAX_INPUT_SLOTS = 5;

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
        if (slottedBloodOrbLevel < bloodOrbLevel) {
            return false;
        }

        if (items.length < MAX_INPUT_SLOTS) {
            return false;
        }

        boolean[] checkList = new boolean[MAX_INPUT_SLOTS];

        for (int i = 0; i < Math.min(recipe.length, MAX_INPUT_SLOTS); i++) {
            ItemStack recipeItemStack = recipe[i];

            if (recipeItemStack == null) {
                continue;
            }

            boolean test = false;

            for (int j = 0; j < MAX_INPUT_SLOTS; j++) {
                if (checkList[j]) {
                    continue;
                }

                ItemStack checkedItemStack = items[j];

                if (checkedItemStack == null) {
                    continue;
                }

                boolean quickTest = false;

                if (recipeItemStack.getItem() instanceof ItemBlock) {
                    if (checkedItemStack.getItem() instanceof ItemBlock) {
                        quickTest = true;
                    }
                } else if (!(checkedItemStack.getItem() instanceof ItemBlock)) {
                    quickTest = true;
                }

                if (!quickTest) {
                    continue;
                }

                if ((checkedItemStack.getItemDamage() == recipeItemStack.getItemDamage()
                        || OreDictionary.WILDCARD_VALUE == recipeItemStack.getItemDamage())
                        && checkedItemStack.getItem() == recipeItemStack.getItem()) {
                    test = true;
                    checkList[j] = true;
                    break;
                }
            }

            if (!test) {
                return false;
            }
        }

        return true;
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
