package WayofTime.alchemicalWizardry.client.nei.widgets;

import static WayofTime.alchemicalWizardry.client.ClientUtils.mc;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.ModItems;

public class RadiusInfo extends BMNEIWidget {

    private final int radius;
    private static final RenderItem renderItem = new RenderItem();
    // It's a circle with a dot in the center. Couldn't ask for a better item!
    private static final ItemStack item = new ItemStack(ModItems.demonPlacer);

    public RadiusInfo(float scale, int radius) {
        super(scale);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void onDraw(int x, int y) {
        prevX = x;
        prevY = y;

        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, x, y);

        drawCenteredText(x, y, StatCollector.translateToLocalFormatted("nei.recipe.meteor.radius", radius));
    }

}
