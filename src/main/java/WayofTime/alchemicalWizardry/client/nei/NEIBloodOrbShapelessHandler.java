package WayofTime.alchemicalWizardry.client.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapelessRecipeHandler;

/**
 * NEI Blood Orb Shapeless Recipe Handler by joshie *
 */
public class NEIBloodOrbShapelessHandler extends ShapelessRecipeHandler {

    public class CachedBloodOrbRecipe extends CachedShapelessRecipe {

        public CachedBloodOrbRecipe(ArrayList<Object> items, ItemStack recipeOutput) {
            super(items, recipeOutput);
        }

        @Override
        public void setIngredients(List<?> items) {
            ingredients.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                Object o = items.get(ingred);
                if (o instanceof ItemStack) {
                    PositionedStack stack = new PositionedStack(
                            items.get(ingred),
                            25 + stackorder[ingred][0] * 18,
                            6 + stackorder[ingred][1] * 18);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                } else if (o instanceof Integer i) {
                    ArrayList<ItemStack> orbs = new ArrayList<>();
                    for (Item item : NEIConfig.getBloodOrbs()) {
                        if (((IBloodOrb) item).getOrbLevel() >= i) {
                            orbs.add(new ItemStack(item));
                        }
                    }

                    PositionedStack stack = new PositionedStack(
                            orbs,
                            25 + stackorder[ingred][0] * 18,
                            6 + stackorder[ingred][1] * 18);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                } else if (o instanceof List) {
                    PositionedStack stack = new PositionedStack(
                            o,
                            25 + stackorder[ingred][0] * 18,
                            6 + stackorder[ingred][1] * 18);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == NEIBloodOrbShapelessHandler.class) {
            List<IRecipe> allRecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allRecipes) {
                CachedBloodOrbRecipe recipe = null;
                if (irecipe instanceof ShapelessBloodOrbRecipe orbRecipe) recipe = forgeShapelessRecipe(orbRecipe);

                if (recipe == null) continue;

                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allRecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allRecipes) {
            if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(irecipe.getRecipeOutput(), result)) {
                CachedBloodOrbRecipe recipe = null;
                if (irecipe instanceof ShapelessBloodOrbRecipe orbRecipe) recipe = forgeShapelessRecipe(orbRecipe);

                if (recipe == null) continue;

                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allRecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allRecipes) {
            CachedBloodOrbRecipe recipe = null;
            if (irecipe instanceof ShapelessBloodOrbRecipe orbRecipe) recipe = forgeShapelessRecipe(orbRecipe);

            if (recipe == null) continue;

            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    public CachedBloodOrbRecipe forgeShapelessRecipe(ShapelessBloodOrbRecipe recipe) {
        ArrayList<Object> items = recipe.getInput();

        for (Object item : items) if (item instanceof List && ((List<?>) item).isEmpty()) // ore handler, no ores
            return null;

        return new CachedBloodOrbRecipe(items, recipe.getRecipeOutput());
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("bm.string.crafting.orb.shapeless");
    }
}
