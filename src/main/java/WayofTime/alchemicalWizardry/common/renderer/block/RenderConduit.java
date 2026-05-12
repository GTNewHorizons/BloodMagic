package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelConduit;
import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderConduit extends TileEntitySpecialRenderer {

    public static final ResourceLocation TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/Conduit.png");
    private final ModelConduit MODEL = new ModelConduit();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
        if (tileEntity instanceof TEConduit tileConduit) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.MODEL.render(
                    null,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0625F,
                    tileConduit.getInputDirection(),
                    tileConduit.getOutputDirection());
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
}
