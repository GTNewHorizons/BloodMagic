package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.awt.Rectangle;

import net.minecraft.client.gui.FontRenderer;

import codechicken.nei.recipe.GuiRecipe;

public abstract class BMNEIWidget {

    protected final float scale;
    public int prevX, prevY;

    protected BMNEIWidget(float scale) {
        this.scale = scale;
    }

    public Rectangle getRect(GuiRecipe<?> gui) {
        return new Rectangle(gui.guiLeft + prevX + 4, gui.guiTop + prevY + 14, (int) (18 * scale), (int) (18 * scale));
    }

    protected static void drawCenteredText(int x, int y, String text) {
        FontRenderer fontRenderer = mc.fontRenderer;

        int textWidth = fontRenderer.getStringWidth(text);
        int textX = x + 9 - (textWidth / 2);
        int textY = y + 18;

        fontRenderer.drawString(text, textX, textY, 0x000000);
    }
}
