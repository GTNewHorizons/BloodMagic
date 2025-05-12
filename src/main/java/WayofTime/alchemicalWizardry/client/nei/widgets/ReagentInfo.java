package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.util.List;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModBlocks;

public class ReagentInfo extends BMNEIWidget {

    private static final RenderItem renderItem = new RenderItem();
    private static final ItemStack item = new ItemStack(ModBlocks.blockReagentConduit);

    public ReagentInfo(float scale) {
        super(scale);
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

    /**
     * Adds the text Reagent effects: to the tooltip list.
     */
    public void onHover(List<String> list) {
        list.add(StatCollector.translateToLocal("nei.recipe.reagent.text"));
    }
}
