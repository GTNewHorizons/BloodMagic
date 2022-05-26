package WayofTime.alchemicalWizardry.client.nei;

import java.awt.Rectangle;

import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRecipe;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Binding Ritual Handler by Arcaratus
 */
public class NEIBindingRitualHandler extends TemplateRecipeHandler
{
    public class CachedBindingRecipe extends CachedRecipe
    {
        PositionedStack input, output;

        public CachedBindingRecipe(BindingRecipe recipe)
        {
            input = new PositionedStack(recipe.requiredItem, 37, 21, false);
            output = new PositionedStack(recipe.outputItem, 110, 21, false);
        }

        @Override
        public PositionedStack getIngredient()
        {
            return input;
        }

        @Override
        public PositionedStack getResult()
        {
            return output;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals("alchemicalwizardry.bindingritual") && getClass() == NEIBindingRitualHandler.class)
        {
            for (BindingRecipe recipe : BindingRegistry.bindingRecipes)
            {
                if (recipe != null && recipe.outputItem != null)
                {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (BindingRecipe recipe: BindingRegistry.bindingRecipes)
        {
            if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.outputItem, result))
            {
                if (recipe != null && recipe.outputItem != null)
                {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        for (BindingRecipe recipe: BindingRegistry.bindingRecipes)
        {
            if (NEIServerUtils.areStacksSameTypeCraftingWithNBT(recipe.requiredItem, ingredient))
            {
                if (recipe != null && recipe.outputItem != null)
                {
                    arecipes.add(new CachedBindingRecipe(recipe));
                }
            }
        }
    }

    @Override
    public String getOverlayIdentifier()
    {
        return "alchemicalwizardry.bindingritual";
    }

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(68, 20, 22, 16), "alchemicalwizardry.bindingritual"));
    }

    @Override
    public String getRecipeName()
    {
        return "Binding Ritual";
    }

    @Override
    public String getGuiTexture()
    {
        return new ResourceLocation("alchemicalwizardry", "gui/nei/bindingRitual.png").toString();
    }
}