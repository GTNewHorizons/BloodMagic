package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.util.ArrayList;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.client.nei.NEIConfig;

public class CostInfo extends BMNEIWidget {

    private final int cost;
    private final ItemStack orbStack;
    private static final RenderItem renderItem = new RenderItem();
    static {
        renderItem.renderWithColor = false;
    }

    public CostInfo(float scale, int cost) {
        super(scale);
        this.cost = cost;

        Item orb = null;
        ArrayList<Item> orbList = NEIConfig.getOrbsByCapacity();
        for (Item o : orbList) {
            if (((IBloodOrb) o).getMaxEssence() >= cost) {
                orb = o;
                break;
            }
        }
        if (orb == null) {
            orb = orbList.get(orbList.size() - 1);
        }

        orbStack = new ItemStack(orb);
    }

    public int getCost() {
        return cost;
    }

    public void onDraw(int x, int y) {
        prevX = x;
        prevY = y;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (cost > APISpellHelper.getPlayerLPTag(mc.thePlayer)) {
            GL11.glColor4f(0.4f, 0.4f, 0.4f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), orbStack, x, y);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        drawCenteredText(x, y, String.format("%,d", cost));
    }

}
