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

import WayofTime.alchemicalWizardry.common.renderer.model.ModelWritingTable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.client.FMLClientHandler;

public class RenderWritingTable extends TileEntitySpecialRenderer {

    private final ModelWritingTable modelWritingTable = new ModelWritingTable();
    private final RenderItem customRenderItem;

    public RenderWritingTable() {
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
        if (tileEntity instanceof TEWritingTable tileAltar) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d0 + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/WritingTable.png");
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
            GL11.glPushMatrix();
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            this.modelWritingTable.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPopMatrix();

            for (int i = 1; i <= 6; i++) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LIGHTING);
                if (tileAltar.getStackInSlot(i) != null) {
                    float scaleFactor = getGhostItemScaleFactor(tileAltar.getStackInSlot(i));
                    float rotationAngle = Minecraft.getMinecraft().gameSettings.fancyGraphics
                            ? (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL)
                            : 0;
                    EntityItem ghostEntityItem = new EntityItem(tileAltar.getWorldObj());
                    ghostEntityItem.hoverStart = 0.0F;
                    ghostEntityItem.setEntityItemStack(tileAltar.getStackInSlot(i));
                    float displacementX = getXDisplacementForSlot(i);
                    float displacementY = getYDisplacementForSlot(i);
                    float displacementZ = getZDisplacementForSlot(i);

                    if (ghostEntityItem.getEntityItem().getItem() instanceof ItemBlock) {
                        GL11.glTranslatef(
                                (float) d0 + 0.5F + displacementX,
                                (float) d1 + displacementY + 0.7F,
                                (float) d2 + 0.5F + displacementZ);
                    } else {
                        GL11.glTranslatef(
                                (float) d0 + 0.5F + displacementX,
                                (float) d1 + displacementY + 0.6F,
                                (float) d2 + 0.5F + displacementZ);
                    }
                    GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
                    GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
                    customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
                }

                GL11.glPopMatrix();
            }
        }
    }

    private float getGhostItemScaleFactor(ItemStack itemStack) {
        float scaleFactor = 0.8F;

        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemBlock) {
                if (customRenderItem.getMiniBlockCount(itemStack, (byte) 1) == 5) {
                    return 0.80F * scaleFactor;
                }
                return 0.90F * scaleFactor;
            } else {
                return 0.65F * scaleFactor;
            }
        }

        return scaleFactor;
    }

    private float getXDisplacementForSlot(int slot) {
        return switch (slot) {
            case 1 -> -0.375f;
            case 2, 5 -> -0.125f;
            case 3, 4 -> 0.3125f;
            default -> 0.0f;
        };
    }

    private float getYDisplacementForSlot(int slot) {
        return switch (slot) {
            case 0, 6 -> 0.4f;
            default -> -0.35f;
        };
    }

    private float getZDisplacementForSlot(int slot) {
        return switch (slot) {
            case 2 -> 0.375f;
            case 3 -> 0.25f;
            case 4 -> -0.25f;
            case 5 -> -0.375f;
            default -> 0.0f;
        };
    }

}
