package WayofTime.alchemicalWizardry.client.nei;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;
import static WayofTime.alchemicalWizardry.client.nei.NEIConfig.ARROW_TEXTURE;

import java.awt.Rectangle;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class NEICalcinatorHandler extends TemplateRecipeHandler {

    public class CachedReagentInfo extends CachedRecipe {

        private final Reagent reagent;
        private final int amount;
        private final int color;

        public CachedReagentInfo(ReagentStack reagent) {
            this.reagent = reagent.reagent;
            this.amount = reagent.amount;
            this.color = reagent.reagent.getColourRed() << 16 | reagent.reagent.getColourGreen() << 8
                    | reagent.reagent.getColourBlue();
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(ReagentRegistry.getItemForReagent(reagent), 32, 6);
        }

        public Reagent getReagent() {
            return reagent;
        }

        public int getAmount() {
            return amount;
        }

        public void onDraw(int x, int y) {
            drawColoredProgressBar(x, y);

            String amountText = String.format("%,d AR", amount);
            FontRenderer fontRenderer = mc.fontRenderer;

            int textWidth = fontRenderer.getStringWidth(amountText);
            int textX = x + 84 - (textWidth / 2);
            int textY = y + 19;

            fontRenderer.drawString(amountText, textX, textY, 0x000000);

            textWidth = fontRenderer.getStringWidth(reagent.name);
            textX = x + 84 - (textWidth / 2);
            textY = y + 29;

            fontRenderer.drawString(reagent.name, textX, textY, color);
        }

        private void drawColoredProgressBar(int x, int y) {
            int arrowX = x + 25;
            int arrowY = y + 19;
            int arrowWidth = 24;
            int arrowHeight = 16;

            int fillTime = 192;
            int holdTime = 8;
            int totalCycle = fillTime + holdTime;

            int cycleTick = cycleticks % totalCycle;

            int fillWidth;
            if (cycleTick < fillTime) {
                fillWidth = cycleTick * arrowWidth / fillTime;
            } else {
                fillWidth = arrowWidth;
            }

            TextureManager texManager = mc.getTextureManager();
            texManager.bindTexture(ARROW_TEXTURE);

            Tessellator tessellator = Tessellator.instance;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            GL11.glColor4f(r, g, b, 1.0f);

            tessellator.startDrawingQuads();

            tessellator.addVertexWithUV(arrowX, arrowY + arrowHeight, 0, 0, 1);
            tessellator.addVertexWithUV(arrowX + fillWidth, arrowY + arrowHeight, 0, fillWidth / (float) arrowWidth, 1);
            tessellator.addVertexWithUV(arrowX + fillWidth, arrowY, 0, fillWidth / (float) arrowWidth, 0);
            tessellator.addVertexWithUV(arrowX, arrowY, 0, 0, 0);

            tessellator.draw();

            GL11.glDisable(GL11.GL_BLEND);
        }

        @Override
        public PositionedStack getOtherStack() {
            ItemStack[] orbStacks = NEIConfig.getBloodOrbs().stream().map(ItemStack::new).toArray(ItemStack[]::new);

            PositionedStack stack = new PositionedStack(orbStacks, 32, 33, true);
            stack.setPermutationToRender((cycleticks / 20) % orbStacks.length);

            return stack;
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
        } else { // Check reagents in crystal belljars or other reagent storages
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
                ItemStack reagentItem = ReagentRegistry.getItemForReagent(reagent.reagent);
                ReagentStack reagentStackForItem = ReagentRegistry.getReagentStackForItem(reagentItem);
                if (reagentStackForItem != null) {
                    arecipes.add(new CachedReagentInfo(reagentStackForItem));
                }
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
        transferRects.add(new RecipeTransferRect(new Rectangle(54, 23, 23, 16), "alchemicalwizardry.calcinator"));
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
