package WayofTime.alchemicalWizardry.client.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRecipe;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.client.nei.widgets.ReagentInfo;
import WayofTime.alchemicalWizardry.client.nei.widgets.RitualInfo;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

/**
 * Binding Ritual Handler by Arcaratus
 */
public class NEIBindingRitualHandler extends TemplateRecipeHandler {

    private static final RitualInfo ritualInfo = new RitualInfo(1f, "AW006Binding");
    private static final ReagentInfo reagentInfo = new ReagentInfo(1f);

    public class CachedBindingRecipe extends CachedRecipe {

        private final PositionedStack input, output;

        public CachedBindingRecipe(BindingRecipe recipe) {
            input = new PositionedStack(recipe.requiredItem, 37, 21, false);
            output = new PositionedStack(recipe.outputItem, 110, 21, false);
        }

        @Override
        public PositionedStack getIngredient() {
            return input;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("alchemicalwizardry.bindingritual") && getClass() == NEIBindingRitualHandler.class) {
            for (BindingRecipe recipe : BindingRegistry.bindingRecipes) {
                if (recipe != null && recipe.outputItem != null) {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (BindingRecipe recipe : BindingRegistry.bindingRecipes) {
            if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.outputItem, result)) {
                if (recipe.outputItem != null) {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (BindingRecipe recipe : BindingRegistry.bindingRecipes) {
            if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.requiredItem, ingredient)) {
                if (recipe.outputItem != null) {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void drawExtras(int recipe) {
        super.drawExtras(recipe);

        // Only shows these for the first result
        if (recipe == 0) {
            ritualInfo.onDraw(0, 0);
            reagentInfo.onDraw(150, 0);
        }
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currenttip, int recipeIndex) {
        if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.isEmpty()) {
            CachedRecipe cRecipe = arecipes.get(recipeIndex);
            Point mousePos = GuiDraw.getMousePosition();

            if (recipeIndex == 0 && cRecipe instanceof NEIBindingRitualHandler.CachedBindingRecipe) {
                if (ritualInfo.getRect(gui).contains(mousePos)) {
                    ritualInfo.onHover(currenttip);
                } else if (reagentInfo.getRect(gui).contains(mousePos)) {
                    currenttip.add(StatCollector.translateToLocal("nei.recipe.reagent.none"));
                }
            }
        }
        return super.handleTooltip(gui, currenttip, recipeIndex);
    }

    @Override
    public String getOverlayIdentifier() {
        return "alchemicalwizardry.bindingritual";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(68, 20, 22, 16), "alchemicalwizardry.bindingritual"));
    }

    @Override
    public String getRecipeName() {
        return "Binding Ritual";
    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation("alchemicalwizardry", "gui/nei/bindingRitual.png").toString();
    }
}
