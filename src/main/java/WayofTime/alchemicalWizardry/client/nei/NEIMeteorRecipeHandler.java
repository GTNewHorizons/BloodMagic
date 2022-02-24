package WayofTime.alchemicalWizardry.client.nei;

import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigmComponent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NEIMeteorRecipeHandler extends TemplateRecipeHandler {
    public class CachedMeteorRecipe extends CachedRecipe {

        private final List<MeteorParadigmComponent> components;
        private final List<PositionedStack> input = new ArrayList<>();
        private final List<PositionedStack> outputs = new ArrayList<>();
        private final int cost;
        private final int radius;

        public CachedMeteorRecipe(MeteorParadigm meteor) {
            this.components = meteor.componentList;
            this.input.add(new PositionedStack(meteor.focusStack, 74, 4));
            int row = 0;
            int col = 0;
            for (MeteorParadigmComponent component : meteor.componentList) {
                ItemStack stack = component.getValidBlockParadigm();
                List<String> tooltips = new ArrayList<>();
                if (stack == null) {
                    stack = new ItemStack(Blocks.fire);
                    tooltips.add(String.format("no entries found for oredict \"%s\"", component.getOreDictName()));
                }
                tooltips.add(I18n.format("nei.recipe.meteor.chance", getFormattedChance(component.getChance())));
                this.outputs.add(new TooltipStack(stack, 3 + 18 * col, 37 + 18 * row, tooltips));
                col++;
                if (col > 8) {
                    col = 0;
                    row++;
                }
            }
            this.radius = meteor.radius;
            this.cost = meteor.cost;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.input;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }

        public List<MeteorParadigmComponent> getComponents() {
            return components;
        }

        public int getCost() {
            return cost;
        }

        public int getRadius() {
            return radius;
        }
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(75, 22, 15, 13), getOverlayIdentifier()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier()) && getClass() == NEIMeteorRecipeHandler.class) {
            for (MeteorParadigm meteor : getSortedMeteors()) {
                arecipes.add(new CachedMeteorRecipe(meteor));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MeteorParadigm meteor : getSortedMeteors()) {
            if (meteor.componentList.stream().anyMatch(m -> NEIServerUtils.areStacksSameTypeCrafting(result, m.getValidBlockParadigm()))) {
                arecipes.add(new CachedMeteorRecipe(meteor));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (MeteorParadigm meteor : getSortedMeteors()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, meteor.focusStack)) {
                arecipes.add(new CachedMeteorRecipe(meteor));
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation("alchemicalwizardry", "gui/nei/meteor.png").toString();
    }

    @Override
    public String getOverlayIdentifier() {
        return "alchemicalwizardry.meteor";
    }

    @Override
    public void drawExtras(int recipe) {
        CachedMeteorRecipe meteorRecipe = (CachedMeteorRecipe) this.arecipes.get(recipe);
        int cost = meteorRecipe.getCost();
        int radius = meteorRecipe.getRadius();
        Minecraft.getMinecraft().fontRenderer.drawString(I18n.format("nei.recipe.meteor.cost", String.format("%,d", cost)), 2, 96, 0x000000);
        Minecraft.getMinecraft().fontRenderer.drawString(I18n.format("nei.recipe.meteor.radius", radius), 2, 107, 0x000000);
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 172, 130);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedMeteorRecipe meteorRecipe = (CachedMeteorRecipe) this.arecipes.get(recipe);
        for (PositionedStack pStack : meteorRecipe.outputs) {
            if (!gui.isMouseOver(pStack, recipe)) continue;
            if (!(pStack instanceof TooltipStack)) break;

            TooltipStack tStack = (TooltipStack) pStack;
            if (Arrays.stream(tStack.items).anyMatch(s -> NEIServerUtils.areStacksSameTypeCrafting(s, stack))) {
                currenttip.addAll(tStack.getTooltips());
                break;
            }
        }
        return currenttip;
    }

    @Override
    public String getRecipeName() {
        return I18n.format("nei.recipe.meteor.category");
    }

    private List<MeteorParadigm> getSortedMeteors() {
        return MeteorRegistry.paradigmList
            .stream()
            .sorted(Comparator.comparing(m -> m.cost))
            .collect(Collectors.toList());
    }

    private String getFormattedChance(int chance) {
        float percentage = (float) chance / 10;
        boolean isInteger = Float.compare(percentage, (float) (chance / 10)) == 0;
        if (isInteger) return String.format("%,d", (int) percentage);
        else return String.format("%,.1f", percentage);
    }
}
