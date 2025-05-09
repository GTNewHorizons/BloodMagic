package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.awt.Rectangle;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.ModItems;
import codechicken.nei.recipe.GuiRecipe;

public class RadiusInfo {

    private final int radius;
    private int prevX, prevY;
    private static final RenderItem renderItem = new RenderItem();
    // It's a circle with a dot in the center. Couldn't ask for a better item!
    private static final ItemStack item = new ItemStack(ModItems.demonPlacer);

    public RadiusInfo(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void onDraw(int x, int y) {
        prevX = x;
        prevY = y;

        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, x, y);

        String costText = StatCollector.translateToLocalFormatted("nei.recipe.meteor.radius", radius);
        FontRenderer fontRenderer = mc.fontRenderer;

        int textWidth = fontRenderer.getStringWidth(costText);
        int textX = x + 9 - (textWidth / 2);
        int textY = y + 18;

        fontRenderer.drawString(costText, textX, textY, 0x000000);
    }

    public Rectangle getRect(GuiRecipe<?> gui) {
        return new Rectangle(gui.guiLeft + prevX + 5, gui.guiTop + prevY + 15, 16, 16);
    }

}
