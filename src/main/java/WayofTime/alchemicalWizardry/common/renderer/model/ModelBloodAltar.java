package WayofTime.alchemicalWizardry.common.renderer.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.client.FMLClientHandler;

public class ModelBloodAltar extends ModelBase {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/altar.png");

    private final IModelCustom MODEL = AdvancedModelLoader
            .loadModel(new ResourceLocation("alchemicalwizardry:models/bloodaltar-fixeUV.obj"));

    public void renderBloodAltar() {
        MODEL.renderAll();
    }

    public void renderBloodAltar(TEAltar altar, double x, double y, double z) {
        float scale = 0.1f;
        // Push a blank matrix onto the stack
        GL11.glPushMatrix();
        // Move the object into the correct position on the block (because the OBJ's origin is the center of the object)
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        // Scale our object to about half-size in all directions (the OBJ file is a little large)
        GL11.glScalef(scale, scale, scale);
        // Bind the texture, so that OpenGL properly textures our block.
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
        // Render the object, using modelTutBox.renderAll();
        this.renderBloodAltar();
        // Pop this matrix from the stack.
        GL11.glPopMatrix();
    }

    public void renderBloodLevel(TEAltar altar, double x, double y, double z) {
        GL11.glPushMatrix();
        float level = altar.getFluidAmount();
        GL11.glTranslatef((float) x, (float) y + 0.6499f + 0.12f * (level / altar.getCapacity()), (float) z);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        renderBloodLevel(AlchemicalWizardry.lifeEssenceFluid.getStillIcon());
        GL11.glPopMatrix();
    }

    public void renderBloodLevel(IIcon icon) {
        Tessellator tessellator = Tessellator.instance;

        double minU = icon.getInterpolatedU(0);
        double maxU = icon.getInterpolatedU(16);
        double minV = icon.getInterpolatedV(0);
        double maxV = icon.getInterpolatedV(16);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);
        tessellator.addVertexWithUV(1, 0, 1, maxU, maxV);
        tessellator.addVertexWithUV(1, 0, 0, maxU, minV);
        tessellator.addVertexWithUV(0, 0, 0, minU, minV);
        tessellator.addVertexWithUV(0, 0, 1, minU, maxV);
        tessellator.draw();
    }
}
