package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.common.renderer.model.ModelAlchemicalCalcinator;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAlchemicCalcinator;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderAlchemicCalcinator extends TileEntitySpecialRenderer {

    private final ModelAlchemicalCalcinator CALCINATOR_MODEL = new ModelAlchemicalCalcinator();
    private final ResourceLocation REAGENT_TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/Reagent.png");
    private static final ResourceLocation CALCINATOR_TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/AlchemicalCalcinator.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
        if (!(tileEntity instanceof TEAlchemicCalcinator calcinator)) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(CALCINATOR_TEXTURE);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.CALCINATOR_MODEL.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        if (calcinator.getStackInSlot(1) != null) {
            GL11.glPushMatrix();
            boolean fancySaved = Minecraft.isFancyGraphicsEnabled();
            Minecraft.getMinecraft().gameSettings.fancyGraphics = true;

            float scaleFactor = getGhostItemScaleFactor(calcinator.getStackInSlot(1));
            EntityItem ghostEntityItem = new EntityItem(calcinator.getWorldObj());
            ghostEntityItem.hoverStart = 0.0F;
            ghostEntityItem.setEntityItemStack(calcinator.getStackInSlot(1));
            float displacement = 0.2F;

            if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock) {
                GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
            } else {
                GL11.glTranslatef(
                        (float) d0 + 0.5F,
                        (float) d1 + displacement + 10.4f / 16.0f,
                        (float) d2 + 0.5F - 0.0625f * 2f);
            }
            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

            if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)) {
                GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
            }

            TEAltarRenderer.customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
            Minecraft.getMinecraft().gameSettings.fancyGraphics = fancySaved;
            GL11.glPopMatrix();
        }

        if (calcinator.getStackInSlot(0) != null) {
            GL11.glPushMatrix();
            boolean fancySaved = Minecraft.isFancyGraphicsEnabled();
            Minecraft.getMinecraft().gameSettings.fancyGraphics = true;

            float scaleFactor = getGhostItemScaleFactor(calcinator.getStackInSlot(0));
            EntityItem ghostEntityItem = new EntityItem(calcinator.getWorldObj());
            ghostEntityItem.hoverStart = 0.0F;
            ghostEntityItem.setEntityItemStack(calcinator.getStackInSlot(0));
            float displacement = -0.5F;

            if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock) {
                GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
            } else {
                GL11.glTranslatef(
                        (float) d0 + 0.5F,
                        (float) d1 + displacement + 10.4f / 16.0f,
                        (float) d2 + 0.5F - 0.0625f * 2f);
            }
            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

            if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)) {
                GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
            }

            TEAltarRenderer.customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
            Minecraft.getMinecraft().gameSettings.fancyGraphics = fancySaved;
            GL11.glPopMatrix();
        }

        ReagentContainerInfo[] info = calcinator.getContainerInfo(ForgeDirection.UNKNOWN);
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

    private void renderTankContents(double x, double y, double z, int colourRed, int colourGreen, int colourBlue,
            int colourIntensity) {
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
        tessellator.setColorRGBA(colourRed, colourGreen, colourBlue, colourIntensity);
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        tessellator.setBrightness(240);

        double x1 = -7d / 16d;
        double x2 = 7d / 16d;
        double y1 = 1d / 16d;
        double y2 = 5d / 16d;
        double z1 = -7d / 16d;
        double z2 = 7d / 16d;

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
        tessellator.draw();

        GL11.glDepthMask(true);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private float getGhostItemScaleFactor(ItemStack itemStack) {
        float scaleFactor = 1.5F;

        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemBlock) {
                if (TEAltarRenderer.customRenderItem.getMiniBlockCount(itemStack, (byte) 1) == 5) {
                    return 0.80F * scaleFactor;
                }
                return 0.90F * scaleFactor;
            } else {
                return 0.65F * scaleFactor;
            }
        }

        return scaleFactor;
    }

}
