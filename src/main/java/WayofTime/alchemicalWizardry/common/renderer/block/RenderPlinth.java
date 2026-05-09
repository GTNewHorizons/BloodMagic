package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
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

    private final ModelPlinth modelPlinth = new ModelPlinth();
    private final RenderItem customRenderItem;

    public RenderPlinth() {
        customRenderItem = new RenderItem() {

            @Override
            public boolean shouldBob() {
                return false;
            }
        };
        customRenderItem.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
        if (tileEntity instanceof TEPlinth tilePlinth) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/Plinth.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelPlinth.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
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
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
                } else {
                    GL11.glTranslatef(
                            (float) d0 + 0.5F,
                            (float) d1 + displacement + 10.4f / 16.0f,
                            (float) d2 + 0.5F - 0.1875f);
                }
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

                if (!(ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock)) {
                    GL11.glRotatef(90f, 1.0f, 0.0f, 0.0F);
                }

                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);

                Minecraft.getMinecraft().gameSettings.fancyGraphics = fancySaved;
            }

            GL11.glPopMatrix();
        }
    }

    private float getGhostItemScaleFactor(ItemStack itemStack) {
        float scaleFactor = 2.0F / 0.9F;

        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemBlock) {
                if (customRenderItem.getMiniBlockCount(itemStack, (byte) 1) == 5) {
                    return 0.80F * scaleFactor / 2;
                }
                return 0.90F * scaleFactor / 2;
            } else {
                return 0.65F * scaleFactor;
            }
        }

        return scaleFactor;
    }

}
