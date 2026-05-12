package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelCrystalBelljar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBellJar;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderCrystalBelljar extends TileEntitySpecialRenderer {

    private final ModelCrystalBelljar MODEL = new ModelCrystalBelljar();
    public static final ResourceLocation BELLJAR_TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/CrystalBelljar.png");
    private final ResourceLocation REAGENT_TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/Reagent.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
        if (!(tileEntity instanceof TEBellJar tileAltar)) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(BELLJAR_TEXTURE);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.MODEL.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        ReagentContainerInfo[] info = tileAltar.getContainerInfo(ForgeDirection.UNKNOWN);
        if (info.length >= 1 && info[0] != null) {
            ReagentStack reagentStack = info[0].reagent;
            int capacity = info[0].capacity;
            if (reagentStack != null && reagentStack.reagent != null) {
                Reagent reagent = reagentStack.reagent;
                this.renderTankContents(
                        d0,
                        d1,
                        d2,
                        reagent.red(),
                        reagent.green(),
                        reagent.blue(),
                        200 * reagentStack.amount / capacity);
            }
        }
    }

    private void renderTankContents(double x, double y, double z, int red, int green, int blue, int intensity) {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        this.bindTexture(REAGENT_TEXTURE);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(red, green, blue, intensity);
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        tessellator.setBrightness(240);

        double x1 = -4d / 16d;
        double x2 = 4d / 16d;
        double y1 = -6d / 16d;
        double y2 = 4d / 16d;
        double z1 = -4d / 16d;
        double z2 = 4d / 16d;

        double resx1 = 0.0d;
        double resx2 = 0.0d;
        double resy1 = 1.0d;
        double resy2 = 1.0d;

        tessellator.addVertexWithUV(x1, y1, z1, resx1, resy1);
        tessellator.addVertexWithUV(x2, y1, z1, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z1, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z1, resx1, resy2);
        tessellator.addVertexWithUV(x1, y1, z1, resx1, resy1);
        tessellator.addVertexWithUV(x1, y1, z2, resx2, resy1);
        tessellator.addVertexWithUV(x1, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z1, resx1, resy2);
        tessellator.addVertexWithUV(x1, y1, z2, resx1, resy1);
        tessellator.addVertexWithUV(x2, y1, z2, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z2, resx1, resy2);
        tessellator.addVertexWithUV(x2, y1, z1, resx1, resy1);
        tessellator.addVertexWithUV(x2, y1, z2, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x2, y2, z1, resx1, resy2);
        tessellator.addVertexWithUV(x1, y2, z1, resx1, resy1);
        tessellator.addVertexWithUV(x2, y2, z1, resx2, resy1);
        tessellator.addVertexWithUV(x2, y2, z2, resx2, resy2);
        tessellator.addVertexWithUV(x1, y2, z2, resx1, resy2);
        tessellator.draw();

        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
