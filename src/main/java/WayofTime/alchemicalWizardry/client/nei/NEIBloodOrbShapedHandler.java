package WayofTime.alchemicalWizardry.client.nei;

import static WayofTime.alchemicalWizardry.client.nei.NEIConfig.getBloodOrbs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;

/**
 * NEI Blood Orb Shaped Recipe Handler by joshie *
 */
public class NEIBloodOrbShapedHandler extends ShapedRecipeHandler {

    public class CachedBloodOrbRecipe extends CachedShapedRecipe {

        public CachedBloodOrbRecipe(int width, int height, Object[] items, ItemStack out) {
            super(width, height, items, out);
        }

        @Override
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Object o = items[y * width + x];
                    if (o == null) continue;

                    if (o instanceof ItemStack) {
                        PositionedStack stack = new PositionedStack(o, 25 + x * 18, 6 + y * 18, false);
                        stack.setMaxSize(1);
                        ingredients.add(stack);
                    } else if (o instanceof Integer i) {
                        ArrayList<ItemStack> orbs = new ArrayList<>();
                        for (Item item : getBloodOrbs()) {
                            if (((IBloodOrb) item).getOrbLevel() >= i) {
                                orbs.add(new ItemStack(item));
                            }
                        }
                        PositionedStack stack = new PositionedStack(orbs, 25 + x * 18, 6 + y * 18, false);
                        stack.setMaxSize(1);
                        ingredients.add(stack);
                    } else if (o instanceof List) {
                        PositionedStack stack = new PositionedStack(o, 25 + x * 18, 6 + y * 18, false);
                        stack.setMaxSize(1);
                        ingredients.add(stack);
                    }
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == NEIBloodOrbShapedHandler.class) {
            for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
                if (irecipe instanceof ShapedBloodOrbRecipe orbRecipe) {
                    CachedBloodOrbRecipe recipe = forgeShapedRecipe(orbRecipe);
                    if (recipe == null) continue;

                    recipe.computeVisuals();
                    arecipes.add(recipe);
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
            if (irecipe instanceof ShapedBloodOrbRecipe orbRecipe) {
                CachedBloodOrbRecipe recipe = forgeShapedRecipe(orbRecipe);
                if (recipe == null || !NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.result.item, result))
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
            CachedShapedRecipe recipe = null;
            if (irecipe instanceof ShapedBloodOrbRecipe orbRecipe) recipe = forgeShapedRecipe(orbRecipe);

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem())) continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    private CachedBloodOrbRecipe forgeShapedRecipe(ShapedBloodOrbRecipe recipe) {
        int width;
        int height;
        try {
            width = recipe.width;
            height = recipe.height;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Object[] items = recipe.getInput();
        for (Object item : items) if (item instanceof List && ((List<?>) item).isEmpty()) // ore
            // handler,
            // no ores
            return null;

        return new CachedBloodOrbRecipe(width, height, items, recipe.getRecipeOutput());
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("bm.string.crafting.orb.shaped");
    }
}
