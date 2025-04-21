package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;
import static WayofTime.alchemicalWizardry.client.nei.NEIConfig.getBloodOrbs;
import static WayofTime.alchemicalWizardry.client.nei.NEIConfig.getOrbsByCapacity;

import java.awt.Rectangle;
import java.util.ArrayList;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import codechicken.nei.recipe.GuiRecipe;

public class CostInfo {

    private final int cost;
    private int prevX, prevY;
    private final int orbLP;
    private final ItemStack orbStack;
    private static final RenderItem renderItem = new RenderItem();
    static {
        renderItem.renderWithColor = false;
    }

    public CostInfo(int cost) {
        this.cost = cost;

        Item orb = null;
        getOrbsByCapacity();
        ArrayList<Item> orbList = getBloodOrbs();
        for (Item o : orbList) {
            if (((IBloodOrb) o).getMaxEssence() >= cost) {
                orb = o;
                break;
            }
        }
        if (orb == null) {
            orb = orbList.get(orbList.size() - 1);
        }

        orbLP = ((IBloodOrb) orb).getMaxEssence();
        orbStack = new ItemStack(orb);
    }

    public int getCost() {
        return cost;
    }

    public void onDraw(int x, int y) {
        prevX = x;
        prevY = y;

        int currentLP = SoulNetworkHandler.getCurrentEssence(mc.thePlayer.getDisplayName());

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (orbLP > currentLP) {
            GL11.glColor4f(0.4f, 0.4f, 0.4f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), orbStack, x, y);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        String costText = String.format("%,d", cost);
        FontRenderer fontRenderer = mc.fontRenderer;

        int textWidth = fontRenderer.getStringWidth(costText);
        int textX = x + 9 - (textWidth / 2);
        int textY = y + 18;

        fontRenderer.drawString(costText, textX, textY, 0x000000);
    }

    public Rectangle getRect(GuiRecipe<?> gui) {
        return new Rectangle(gui.guiLeft + prevX + 4, gui.guiTop + prevY + 14, 18, 18);
    }

}
