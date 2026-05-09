package WayofTime.alchemicalWizardry.common.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelBloodAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class TEAltarRenderer extends TileEntitySpecialRenderer {

    private final ModelBloodAltar modelBloodAltar = new ModelBloodAltar();
    private final RenderItem customRenderItem;

    public TEAltarRenderer() {
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
        if (tileEntity instanceof TEAltar tileAltar) {

            modelBloodAltar.renderBloodAltar(tileAltar, d0, d1, d2);

            if (tileAltar.getCurrentBlood() > 0) modelBloodAltar.renderBloodLevel(tileAltar, d0, d1, d2);

            GL11.glPushMatrix();

            if (tileAltar.getStackInSlot(0) != null) {
                float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(0));
                float rotationAngle = Minecraft.getMinecraft().gameSettings.fancyGraphics
                        ? (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL)
                        : 0;
                EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorldObj());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(0));
                float displacement = 0.2F;

                if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock) {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.7F, (float) d2 + 0.5F);
                } else {
                    GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + displacement + 0.6F, (float) d2 + 0.5F);
                }
                GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
                GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
            }

            GL11.glPopMatrix();
        }
    }

    private float getGhostItemScaleFactor(ItemStack itemStack) {
        float scaleFactor = 1.0F;

        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemBlock) {
                if (customRenderItem.getMiniBlockCount(itemStack, (byte) 1) == 5) {
                    return 0.80F;
                }
                return 0.90F;
            } else {
                return 0.65F;
            }
        }

        return scaleFactor;
    }

}
