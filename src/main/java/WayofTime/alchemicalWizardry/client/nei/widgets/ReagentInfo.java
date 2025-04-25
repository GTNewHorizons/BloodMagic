package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.awt.Rectangle;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModBlocks;
import codechicken.nei.recipe.GuiRecipe;

public class ReagentInfo {

    private final float scale;
    private int prevX, prevY;
    private static final RenderItem renderItem = new RenderItem();
    private static final ItemStack item = new ItemStack(ModBlocks.blockReagentConduit);

    public ReagentInfo(float scale) {
        this.scale = scale;
    }

    public void onDraw(int x, int y) {
        prevX = x;
        prevY = y;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1.0f);
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0);
        GL11.glPopMatrix();
    }

    public Rectangle getRect(GuiRecipe<?> gui) {
        return new Rectangle(gui.guiLeft + prevX + 4, gui.guiTop + prevY + 14, (int) (18 * scale), (int) (18 * scale));
    }
}
