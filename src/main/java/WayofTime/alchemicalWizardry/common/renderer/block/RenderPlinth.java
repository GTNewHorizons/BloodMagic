package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelPlinth;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderPlinth extends TileEntitySpecialRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/Plinth.png");
    private static final ModelPlinth MODEL = new ModelPlinth();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (!(tileEntity instanceof TEPlinth tilePlinth)) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEXTURE);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        MODEL.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPushMatrix();

        if (tilePlinth.getStackInSlot(0) != null) {
            boolean fancySaved = Minecraft.isFancyGraphicsEnabled();
            Minecraft.getMinecraft().gameSettings.fancyGraphics = true;

            float scaleFactor = getGhostItemScaleFactor(tilePlinth.getStackInSlot(0));
            EntityItem ghostEntityItem = new EntityItem(tilePlinth.getWorldObj());
            ghostEntityItem.hoverStart = 0.0F;
            ghostEntityItem.setEntityItemStack(tilePlinth.getStackInSlot(0));
            float displacement = 0.2F;

            if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock) {
                GL11.glTranslatef((float) x + 0.5F, (float) y + displacement + 0.7F, (float) z + 0.5F);
            } else {
                GL11.glTranslatef(
                        (float) x + 0.5F,
                        (float) y + displacement + 10.4f / 16.0f,
                        (float) z + 0.5F - 0.1875f);
            }
            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

            if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)) {
                GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
            }

            TEAltarRenderer.customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);

            Minecraft.getMinecraft().gameSettings.fancyGraphics = fancySaved;
        }

        GL11.glPopMatrix();
    }

    private float getGhostItemScaleFactor(ItemStack itemStack) {
        if (itemStack == null) {
            return 2.22f;
        }
        if (!(itemStack.getItem() instanceof ItemBlock)) {
            return 1.44f;
        }
        if (TEAltarRenderer.customRenderItem.getMiniBlockCount(itemStack, (byte) 1) == 5) {
            return 0.89f;
        }
        return 1.0f;
    }

}
