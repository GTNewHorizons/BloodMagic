package WayofTime.alchemicalWizardry.client.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipe;
import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * NEI Altar Recipe Handler by joshie *
 */
public class NEIAltarRecipeHandler extends TemplateRecipeHandler {
	public class CachedAltarRecipe extends CachedRecipe {
		PositionedStack input;
//		PositionedStack inputItems;
		PositionedStack output;
		int tier, lp_amount, consumption, drain;
		
		public CachedAltarRecipe(AltarRecipe recipe) {
//			inputItems = new PositionedStack(recipe.input, 38, 2, false);
			input = new PositionedStack(recipe.requiredItem, 38, 2, false);
			output = new PositionedStack(recipe.result, 132, 32, false);
			tier = recipe.minTier;
			lp_amount = recipe.liquidRequired;
			consumption = recipe.consumptionRate;
			drain = recipe.drainRate;
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
		if (outputId.equals("alchemicalwizardry.altar") && getClass() == NEIAltarRecipeHandler.class) {
			for(AltarRecipe recipe: AltarRecipeRegistry.altarRecipes) {
				if(recipe != null && recipe.result != null) arecipes.add(new CachedAltarRecipe(recipe));
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(AltarRecipe recipe: AltarRecipeRegistry.altarRecipes) {
			if(NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.result, result)) {
				if(recipe != null && recipe.result != null) arecipes.add(new CachedAltarRecipe(recipe));
			}
		}
	}
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient)  {
		for(AltarRecipe recipe: AltarRecipeRegistry.altarRecipes) {
			if(NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.requiredItem, ingredient)) {
				if(recipe != null && recipe.result != null) arecipes.add(new CachedAltarRecipe(recipe));
			}
		}
    }
	
	@Override
	public void drawExtras(int id) {
		CachedAltarRecipe recipe = (CachedAltarRecipe) arecipes.get(id);
		Minecraft.getMinecraft().fontRenderer.drawString("\u00a77" + StatCollector.translateToLocal("bm.string.tier") + ": " + recipe.tier, 78, 5, 0);
		Minecraft.getMinecraft().fontRenderer.drawString("\u00a77" + "LP: " + recipe.lp_amount, 78, 15, 0);
	}
	
	@Override
	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int id) {
	    super.handleTooltip(gui, currenttip, id);
	    Point mouse = GuiDraw.getMousePosition();
	    Point root = gui.getRecipePosition(id);
	    int offsetX = gui.guiLeft + root.x;
	    int offsetY = gui.guiTop + root.y;
	    if(mouse.x >= 14 + offsetX && mouse.x <= 75 + offsetX && mouse.y >= 22 + offsetY && mouse.y <= 56 + offsetY) {
	        CachedAltarRecipe recipe = (CachedAltarRecipe) arecipes.get(id);
            currenttip.add(StatCollector.translateToLocal("bm.string.consume") + ": " + recipe.consumption + "LP/t");
            currenttip.add(StatCollector.translateToLocal("bm.string.drain") + ": " + recipe.drain + "LP/t");
	    }
	    return currenttip;
	}
	
	@Override
	public String getOverlayIdentifier() {
		return "altarrecipes";
	}
	
	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(90, 32, 22, 16), "alchemicalwizardry.altar"));
	}
	
	@Override
	public String getRecipeName() {
		return "          " + StatCollector.translateToLocal("tile.bloodAltar.name");
	}

	@Override
	public String getGuiTexture() {
		return new ResourceLocation("alchemicalwizardry", "gui/nei/altar.png").toString();
	}
}
