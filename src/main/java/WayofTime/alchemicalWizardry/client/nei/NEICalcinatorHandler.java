package WayofTime.alchemicalWizardry.client.nei;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;
import static WayofTime.alchemicalWizardry.client.nei.NEIConfig.ARROW_TEXTURE;
import static codechicken.lib.gui.GuiDraw.fontRenderer;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEICalcinatorHandler extends TemplateRecipeHandler {

    public class CachedReagentInfo extends CachedRecipe {

        private final Reagent reagent;
        private final PositionedStack stack;
        private final PositionedStack orbs;
        private final int amount;

        private final String amountText;
        private final int amountTextWidth;

        private final String nameText;
        private final int nameTextWidth;

        private static final int ARROW_WIDTH = 24;
        private static final int ARROW_HEIGHT = 16;
        private static final int FILL_TIME = 192;
        private static final int HOLD_TIME = 8;

        public CachedReagentInfo(ReagentStack reagent) {
            this.reagent = reagent.reagent;
            this.stack = new PositionedStack(ReagentRegistry.getItemForReagent(reagent.reagent), 32, 6);
            this.amount = reagent.amount;

            ItemStack[] orbStacks = NEIConfig.getBloodOrbs().stream().map(ItemStack::new).toArray(ItemStack[]::new);
            this.orbs = new PositionedStack(orbStacks, 32, 33);

            String formattedAmount = String.format("%,d", amount);
            this.amountText = StatCollector
                    .translateToLocalFormatted("text.recipe.alchemicCalcinator.reagent", formattedAmount);
            this.amountTextWidth = fontRenderer.getStringWidth(amountText);

            this.nameText = this.reagent.name;
            this.nameTextWidth = fontRenderer.getStringWidth(nameText);
        }

        @Override
        public PositionedStack getResult() {
            return stack;
        }

        public Reagent getReagent() {
            return reagent;
        }

        public int getAmount() {
            return amount;
        }

        public void onDraw(int x, int y) {
            drawColoredProgressBar(x, y);

            int textX = x + 84 - (amountTextWidth / 2);
            int textY = y + 19;
            fontRenderer.drawString(amountText, textX, textY, 0x000000);

            textX = x + 84 - (nameTextWidth / 2);
            textY += 10;
            int color = reagent.getColourRed() << 16 | reagent.getColourGreen() << 8 | reagent.getColourBlue();
            fontRenderer.drawString(nameText, textX, textY, color);
        }

        private void drawColoredProgressBar(int x, int y) {
            int arrowX = x + 25;
            int arrowY = y + 19;

            int totalCycle = FILL_TIME + HOLD_TIME;

            int cycleTick = cycleticks % totalCycle;

            int fillWidth;
            if (cycleTick < FILL_TIME) {
                fillWidth = cycleTick * ARROW_WIDTH / FILL_TIME;
            } else {
                fillWidth = ARROW_WIDTH;
            }

            TextureManager texManager = mc.getTextureManager();
            texManager.bindTexture(ARROW_TEXTURE);

            Tessellator tessellator = Tessellator.instance;

            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glColor4ub(
                    (byte) reagent.getColourRed(),
                    (byte) reagent.getColourGreen(),
                    (byte) reagent.getColourBlue(),
                    (byte) 255);

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(arrowX, arrowY + ARROW_HEIGHT, 0, 0, 1);
            tessellator
                    .addVertexWithUV(arrowX + fillWidth, arrowY + ARROW_HEIGHT, 0, fillWidth / (float) ARROW_WIDTH, 1);
            tessellator.addVertexWithUV(arrowX + fillWidth, arrowY, 0, fillWidth / (float) ARROW_WIDTH, 0);
            tessellator.addVertexWithUV(arrowX, arrowY, 0, 0, 0);
            tessellator.draw();

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, Collections.singletonList(this.orbs));
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("alchemicalwizardry.calcinator") && getClass() == NEICalcinatorHandler.class) {
            for (ReagentStack reagent : ReagentRegistry.itemToReagentMap.values()) {
                arecipes.add(new CachedReagentInfo(reagent));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        checkReagents(result);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        checkReagents(ingredient);
    }

    private void checkReagents(ItemStack result) {
        ReagentStack rs = ReagentRegistry.getReagentStackForItem(result);
        if (rs != null) {
            arecipes.add(new CachedReagentInfo(rs));
            return;
        }
        // Check reagents in crystal belljars or other reagent storages
        NBTTagCompound tagCompound = result.getTagCompound();
        if (tagCompound == null || tagCompound.hasNoTags()) {
            return;
        }
        NBTTagList tagList = tagCompound.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);
        if (tagList.tagList.isEmpty()) {
            return;
        }
        for (int i = 0; i < tagList.tagCount(); i++) {
            // Get the proper size of the ReagentStack created by melting the item for this reagent
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
            ReagentStack reagent = ReagentContainer.readFromNBT(savedTag).getReagent();
            if (reagent == null) continue;
            ItemStack reagentItem = ReagentRegistry.getItemForReagent(reagent.reagent);
            ReagentStack reagentStackForItem = ReagentRegistry.getReagentStackForItem(reagentItem);
            if (reagentStackForItem != null) {
                arecipes.add(new CachedReagentInfo(reagentStackForItem));
            }
        }
    }

    @Override
    public int recipiesPerPage() {
        return 5;
    }

    @Override
    public void drawExtras(int recipe) {
        NEICalcinatorHandler.CachedReagentInfo reagentInfo = (CachedReagentInfo) this.arecipes.get(recipe);
        reagentInfo.onDraw(30, 0);
    }

    @Override
    public String getOverlayIdentifier() {
        return "alchemicalwizardry.calcinator";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(55, 18, 24, 17), "alchemicalwizardry.calcinator"));
    }

    @Override
    public String getGuiTexture() {
        return new ResourceLocation("alchemicalwizardry", "gui/nei/calcinator.png").toString();
    }

    @Override
    public String getRecipeName() {
        return "Alchemic Calcinator";
    }
}
