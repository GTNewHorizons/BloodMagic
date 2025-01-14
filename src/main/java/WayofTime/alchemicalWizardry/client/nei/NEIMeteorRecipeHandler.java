package WayofTime.alchemicalWizardry.client.nei;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Joiner;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigmComponent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIMeteorRecipeHandler extends TemplateRecipeHandler {

    public class CachedMeteorRecipe extends CachedRecipe {

        private final List<PositionedStack> input = new ArrayList<>();
        private final List<PositionedStack> outputs = new ArrayList<>();
        private final int cost;
        private final int radius;
        private Point focus;

        public CachedMeteorRecipe(MeteorParadigm meteor, ItemStack focusStack) {
            this.input.add(new PositionedStack(meteor.focusStack, 74, 4));
            int row = 0;
            int col = 0;

            float totalComponentWeight = meteor.getTotalListWeight(meteor.componentList);
            int fillerChance = meteor.fillerChance;
            List<MeteorParadigmComponent> sortedComponents = new ArrayList<>(meteor.componentList);
            sortedComponents.sort(Comparator.comparingInt(c -> -c.getWeight()));

            float fillerRatio = (float) (fillerChance / 100.0);
            float componentRatio = 1 - fillerRatio;

            for (MeteorParadigmComponent component : sortedComponents) {
                ItemStack stack = component.getBlock();
                int xPos = 3 + 18 * col;
                int yPos = 37 + 18 * row;

                List<String> tooltips = new ArrayList<>();
                float chance = component.getWeight() / totalComponentWeight * componentRatio;
                tooltips.add(I18n.format("nei.recipe.meteor.chance", getFormattedChance(chance)));
                tooltips.add(I18n.format("nei.recipe.meteor.amount", getEstimatedAmount(chance, meteor.radius)));
                if (!component.getRequiredReagents().isEmpty()) {
                    tooltips.add(I18n.format("nei.recipe.meteor.reagent", getReagentStrings(component)));
                }
                this.outputs.add(new TooltipStack(stack, xPos, yPos, tooltips));

                col++;
                if (col > 8) {
                    col = 0;
                    row++;
                }

                if (matchItem(focusStack, stack)) {
                    this.focus = new Point(xPos - 1, yPos - 1);
                }
            }

            if (fillerChance > 0) {
                if (col != 0) {
                    col = 0;
                    row++;
                }

                List<MeteorParadigmComponent> sortedFiller = new ArrayList<>(meteor.fillerList);
                sortedFiller.sort(Comparator.comparingInt(c -> -c.getWeight()));
                float totalFillerWeight = meteor.getTotalListWeight(meteor.fillerList);

                for (MeteorParadigmComponent filler : sortedFiller) {
                    ItemStack stack = filler.getBlock();
                    int xPos = 3 + 18 * col;
                    int yPos = 37 + 18 * row;

                    List<String> tooltips = new ArrayList<>();
                    float chance = filler.getWeight() / totalFillerWeight * fillerRatio;
                    tooltips.add(I18n.format("nei.recipe.meteor.chance", getFormattedChance(chance)));
                    tooltips.add(I18n.format("nei.recipe.meteor.amount", getEstimatedAmount(chance, meteor.radius)));
                    tooltips.add(I18n.format("nei.recipe.meteor.filler"));
                    if (!filler.getRequiredReagents().isEmpty()) {
                        tooltips.add(I18n.format("nei.recipe.meteor.reagent", getReagentStrings(filler)));
                    }
                    this.outputs.add(new TooltipStack(stack, xPos, yPos, tooltips));

                    col++;
                    if (col > 8) {
                        col = 0;
                        row++;
                    }

                    if (matchItem(focusStack, stack)) {
                        this.focus = new Point(xPos - 1, yPos - 1);
                    }
                }
            }

            this.radius = meteor.radius;
            this.cost = meteor.cost;
        }

        private String getReagentStrings(MeteorParadigmComponent component) {
            ArrayList<Reagent> reagents = component.getRequiredReagents();
            ArrayList<String> reagentNames = new ArrayList<>();
            for (Reagent r : reagents) {
                reagentNames.add(r.name);
            }
            return Joiner.on(", ").join(reagentNames);
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
                arecipes.add(new CachedMeteorRecipe(meteor, null));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MeteorParadigm meteor : getSortedMeteors()) {
            if (meteor.componentList.stream().anyMatch(m -> matchItem(result, m.getBlock()))) {
                arecipes.add(new CachedMeteorRecipe(meteor, result));
            }
            if (meteor.fillerList.stream().anyMatch(m -> matchItem(result, m.getBlock()))) {
                arecipes.add(new CachedMeteorRecipe(meteor, result));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (MeteorParadigm meteor : getSortedMeteors()) {
            if (matchItem(ingredient, meteor.focusStack)) {
                arecipes.add(new CachedMeteorRecipe(meteor, null));
            }
        }
    }

    private boolean matchItem(ItemStack compared, ItemStack compareTo) {
        if (compared == null || compareTo == null) return false;
        if (NEIServerUtils.areStacksSameTypeCrafting(compared, compareTo)) {
            return true;
        }
        // ignore ore variants (like basalt ore)
        if (compared.getUnlocalizedName().startsWith("gt.blockores")
                && compareTo.getUnlocalizedName().startsWith("gt.blockores")) {
            return compared.getItemDamage() % 1000 == compareTo.getItemDamage() % 1000;
        }
        return false;
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
        Minecraft.getMinecraft().fontRenderer
                .drawString(I18n.format("nei.recipe.meteor.cost", String.format("%,d", cost)), 2, 168, 0x000000);
        Minecraft.getMinecraft().fontRenderer
                .drawString(I18n.format("nei.recipe.meteor.radius", radius), 2, 179, 0x000000);
    }

    @Override
    public void drawBackground(int recipeIndex) {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 202);

        CachedMeteorRecipe recipe = (CachedMeteorRecipe) this.arecipes.get(recipeIndex);
        Point focus = recipe.focus;
        if (focus != null) {
            GuiDraw.drawTexturedModalRect(focus.x, focus.y, 172, 0, 18, 18);
        }
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedMeteorRecipe meteorRecipe = (CachedMeteorRecipe) this.arecipes.get(recipe);
        for (PositionedStack pStack : meteorRecipe.outputs) {
            // if (!gui.isMouseOver(pStack, recipe)) continue; // put it back when NEI is properly patched
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
        return MeteorRegistry.paradigmList.stream().sorted(Comparator.comparing(m -> m.cost))
                .collect(Collectors.toList());
    }

    private String getFormattedChance(float chance) {
        return new DecimalFormat("0.##").format(chance * 100);
    }

    private int getEstimatedAmount(float chance, int radius) {
        return (int) Math.ceil(4f / 3 * Math.PI * Math.pow(radius + 0.5, 3) * chance);
    }
}
