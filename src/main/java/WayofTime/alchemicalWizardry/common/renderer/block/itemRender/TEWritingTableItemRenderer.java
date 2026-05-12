package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelWritingTable;
import cpw.mods.fml.client.FMLClientHandler;

public class TEWritingTableItemRenderer implements IItemRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/WritingTable.png");
    private static final ModelWritingTable MODEL = new ModelWritingTable();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        float scale = 0.08f;
        switch (type) {
            case ENTITY, EQUIPPED -> renderChemistrySet(0f, 0f, 0f, scale);
            case INVENTORY -> renderChemistrySet(0f, -0.25f, 0f, scale);
        }
    }

    private void renderChemistrySet(float x, float y, float z, float scale) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(180f, 0f, 1f, 0f);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
        MODEL.render(null, 0, 0, 0, 0, 0, 0);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
