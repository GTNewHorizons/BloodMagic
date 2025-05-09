package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import codechicken.nei.recipe.GuiRecipe;

public class RitualInfo {

    private final String id;
    private final int cost;
    private final float scale;
    private final ItemStack crystal;
    private int prevX, prevY;
    private static final RenderItem renderItem = new RenderItem();

    public RitualInfo(String id, float scale) {
        this.id = id;
        this.scale = scale;
        this.cost = Rituals.getCostForActivation(id);
        Rituals r = Rituals.ritualMap.get(id);

        // This is hardcoded unlike the orbs. I'm not aware of any addon that makes tier 3 rituals or crystals.
        if (r == null || r.crystalLevel == 1) {
            crystal = new ItemStack(ModItems.activationCrystal);
        } else {
            crystal = new ItemStack(ModItems.activationCrystal, 1, 1);
        }
    }

    public int getCost() {
        return cost;
    }

    public void onDraw(int x, int y) {
        prevX = x;
        prevY = y;

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1.0f);
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), crystal, 0, 0);
        GL11.glPopMatrix();
    }

    public Rectangle getRect(GuiRecipe<?> gui) {
        return new Rectangle(gui.guiLeft + prevX + 4, gui.guiTop + prevY + 14, (int) (18 * scale), (int) (18 * scale));
    }

    public void onHover(List<String> list) {
        list.add(
                StatCollector
                        .translateToLocalFormatted("nei.recipe.ritual.name", Rituals.getLocalizedNameOfRitual(id)));
        list.add(StatCollector.translateToLocalFormatted("nei.recipe.ritual.cost", this.cost));
    }
}
