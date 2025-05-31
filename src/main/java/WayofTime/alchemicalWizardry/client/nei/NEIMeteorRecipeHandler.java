package WayofTime.alchemicalWizardry.client.nei;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Joiner;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.client.nei.widgets.CostInfo;
import WayofTime.alchemicalWizardry.client.nei.widgets.RadiusInfo;
import WayofTime.alchemicalWizardry.client.nei.widgets.ReagentInfo;
import WayofTime.alchemicalWizardry.client.nei.widgets.RitualInfo;
import WayofTime.alchemicalWizardry.common.summoning.meteor.Meteor;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorComponent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorReagent;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorReagentRegistry;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEIMeteorRecipeHandler extends TemplateRecipeHandler {

    private static final RitualInfo ritualInfo = new RitualInfo(0.75f, "AW019FallingTower");
    private static final ReagentInfo reagentInfo = new ReagentInfo(0.75f);

    public class CachedMeteorRecipe extends CachedRecipe {

        private final List<PositionedStack> input = new ArrayList<>();
        private final List<PositionedStack> outputs = new ArrayList<>();
        private final CostInfo costInfo;
        private final RadiusInfo radiusInfo;
        private Point focus;

        public float fillerChance;

        public CachedMeteorRecipe(Meteor meteor, ItemStack focusStack) {
            this.input.add(new PositionedStack(meteor.focusItem, 75, 4));
            int row = 0;
            int col = 0;

            float totalComponentWeight = MeteorComponent.getTotalListWeight(meteor.ores);
            fillerChance = meteor.fillerChance;
            List<MeteorComponent> sortedComponents = new ArrayList<>(meteor.ores);
            sortedComponents.sort(Comparator.comparingInt(c -> -c.getWeight()));

            float fillerRatio = (float) (fillerChance / 100.0);
            float componentRatio = 1 - fillerRatio;

            if (fillerChance < 100) {
                for (MeteorComponent component : sortedComponents) {
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
            }
            if (fillerChance > 0) {
                if (col != 0) {
                    col = 0;
                    row++;
                }

                List<MeteorComponent> sortedFiller = new ArrayList<>(meteor.filler);
                sortedFiller.sort(Comparator.comparingInt(c -> -c.getWeight()));
                float totalFillerWeight = MeteorComponent.getTotalListWeight(meteor.filler);

                for (MeteorComponent filler : sortedFiller) {
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

            this.costInfo = new CostInfo(1.0f, meteor.cost);
            this.radiusInfo = new RadiusInfo(1.0f, meteor.radius);
        }

        private String getReagentStrings(MeteorComponent component) {
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
            return costInfo.getCost();
        }

        public int getRadius() {
            return radiusInfo.getRadius();
        }
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(75, 22, 15, 13), getOverlayIdentifier()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier()) && getClass() == NEIMeteorRecipeHandler.class) {
            for (Meteor meteor : getSortedMeteors()) {
                arecipes.add(new CachedMeteorRecipe(meteor, null));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Meteor meteor : getSortedMeteors()) {
            if (meteor.ores.stream().anyMatch(m -> matchItem(result, m.getBlock()))) {
                arecipes.add(new CachedMeteorRecipe(meteor, result));
            }
            if (meteor.fillerChance > 0 && meteor.filler.stream().anyMatch(m -> matchItem(result, m.getBlock()))) {
                arecipes.add(new CachedMeteorRecipe(meteor, result));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Meteor meteor : getSortedMeteors()) {
            if (matchItem(ingredient, meteor.focusItem)) {
                arecipes.add(new CachedMeteorRecipe(meteor, null));
            }
        }
    }

    private boolean matchItem(ItemStack compared, ItemStack compareTo) {
        if (compared == null || compareTo == null) return false;
        if (NEIServerUtils.areStacksSameTypeCrafting(compared, compareTo)) {
            return true;
        }

        // Ignore GregTech ore variants (like basalt ore)
        if (compared.getUnlocalizedName().startsWith("gt.blockores")
                && compareTo.getUnlocalizedName().startsWith("gt.blockores")) {
            return compared.getItemDamage() % 1000 == compareTo.getItemDamage() % 1000;
        }

        // Check oredicts for ores. Checks for direct matches (oreIron with oreIron) as well as IC2/GT crushed ores
        // (crushedGold with oreGold), EFR raw ores (rawCopper with oreCopper (although those all seem to have oreX
        // anyways)), and GT:NH raw ores (rawOreDiamond with oreDiamond).
        String[] prefixes = { "crushed", "rawOre", "raw" };
        for (int i : OreDictionary.getOreIDs(compareTo)) {
            String s1 = OreDictionary.getOreName(i);
            if (s1.startsWith("ore")) {
                for (int j : OreDictionary.getOreIDs(compared)) {
                    if (i == j) {
                        return true;
                    }
                    String s2 = OreDictionary.getOreName(j);
                    for (String prefix : prefixes) {
                        if (i == OreDictionary.getOreID(s2.replaceFirst("^" + prefix, "ore"))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public int recipiesPerPage() {
        return 1;
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
        meteorRecipe.costInfo.onDraw(30, 4);
        meteorRecipe.radiusInfo.onDraw(120, 4);
        ritualInfo.onDraw(0, 0);
        reagentInfo.onDraw(153, 0);
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
            if (!(pStack instanceof TooltipStack tStack)) break;

            if (Arrays.stream(tStack.items).anyMatch(s -> NEIServerUtils.areStacksSameTypeCrafting(s, stack))) {
                currenttip.addAll(tStack.getTooltips());
                break;
            }
        }
        return currenttip;
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currenttip, int recipeIndex) {
        if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.isEmpty()) {
            CachedRecipe cRecipe = arecipes.get(recipeIndex);
            Point mousePos = GuiDraw.getMousePosition();

            if (cRecipe instanceof CachedMeteorRecipe cachedRecipe) {
                if (cachedRecipe.costInfo.getRect(gui).contains(mousePos)) {
                    currenttip.add(StatCollector.translateToLocal("nei.recipe.meteor.cost"));
                    if (APISpellHelper.getPlayerLPTag(mc.thePlayer) < cachedRecipe.getCost()) {
                        currenttip.add(
                                EnumChatFormatting.RED
                                        + StatCollector.translateToLocal("nei.recipe.meteor.tooExpensive"));
                    }
                } else if (cachedRecipe.radiusInfo.getRect(gui).contains(mousePos)) {
                    currenttip.add(
                            StatCollector.translateToLocalFormatted(
                                    "nei.recipe.meteor.diameter",
                                    2 * cachedRecipe.radiusInfo.getRadius() + 1));
                } else if (ritualInfo.getRect(gui).contains(mousePos)) {
                    ritualInfo.onHover(currenttip);
                } else if (reagentInfo.getRect(gui).contains(mousePos)) {
                    reagentInfo.onHover(currenttip);
                    addMeteorReagentInfo(currenttip, cachedRecipe);
                }
            }
        }
        return super.handleTooltip(gui, currenttip, recipeIndex);
    }

    private void addMeteorReagentInfo(List<String> list, CachedMeteorRecipe cachedRecipe) {
        DecimalFormat fmt = new DecimalFormat("###.##");
        for (Map.Entry<Reagent, MeteorReagent> entry : MeteorReagentRegistry.reagents.entrySet()) {
            ArrayList<String> l = new ArrayList<>();
            Reagent r = entry.getKey();
            MeteorReagent mr = entry.getValue();

            if (mr.radiusChange != 0) {
                l.add(
                        StatCollector.translateToLocalFormatted(
                                "nei.recipe.reagent.meteor.radius",
                                mr.radiusChange,
                                Meteor.getNewRadius(cachedRecipe.getRadius(), Collections.singletonList(r))));
            }

            if (cachedRecipe.fillerChance > 0) {
                if (mr.fillerChanceChange != 0 || mr.rawFillerChanceChange != 0) {
                    l.add(
                            StatCollector.translateToLocalFormatted(
                                    "nei.recipe.reagent.meteor.filler.chance",
                                    fmt.format(
                                            Meteor.getNewFillerChance(
                                                    cachedRecipe.fillerChance,
                                                    Collections.singletonList(r)))));
                }

                if (!mr.filler.isEmpty()) {
                    l.add(
                            StatCollector.translateToLocalFormatted(
                                    "nei.recipe.reagent.meteor.filler.blocks",
                                    reagentFillerBlockNames(mr)));
                }
            }

            if (mr.disableExplosions) {
                l.add(StatCollector.translateToLocal("nei.recipe.reagent.meteor.explosions"));
            } else if (mr.invertExplosionBlockDamage && AlchemicalWizardry.doMeteorsDestroyBlocks) {
                l.add(StatCollector.translateToLocal("nei.recipe.reagent.meteor.blockDamage.off"));
            } else if (mr.invertExplosionBlockDamage) {
                l.add(StatCollector.translateToLocal("nei.recipe.reagent.meteor.blockDamage.on"));
            }

            if (!l.isEmpty()) {
                list.add(r.name + ":");
                list.addAll(l);
            }
        }
    }

    @Override
    public String getRecipeName() {
        return I18n.format("nei.recipe.meteor.category");
    }

    private List<Meteor> getSortedMeteors() {
        return MeteorRegistry.meteorList.stream().sorted(Comparator.comparing(m -> m.cost))
                .collect(Collectors.toList());
    }

    private String getFormattedChance(float chance) {
        return new DecimalFormat("0.##").format(chance * 100);
    }

    private int getEstimatedAmount(float chance, int radius) {
        return (int) Math.ceil(4f / 3 * Math.PI * Math.pow(radius + 0.5, 3) * chance);
    }

    private String reagentFillerBlockNames(MeteorReagent mr) {
        ArrayList<String> blockNames = new ArrayList<>();
        for (MeteorComponent mc : mr.filler) {
            blockNames.add(mc.getBlock().getDisplayName() + requiredReagentsForFiller(mc));
        }
        return Joiner.on(", ").join(blockNames);
    }

    private String requiredReagentsForFiller(MeteorComponent mc) {
        List<Reagent> reagents = mc.getRequiredReagents();
        if (reagents.isEmpty()) return "";
        return " (" + reagents.stream().map(r -> r.name).collect(Collectors.joining(", ")) + ")";
    }
}
