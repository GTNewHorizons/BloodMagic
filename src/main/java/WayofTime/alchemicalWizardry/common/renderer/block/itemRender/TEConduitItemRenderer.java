package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelConduit;
import cpw.mods.fml.client.FMLClientHandler;

public class TEConduitItemRenderer implements IItemRenderer {

    private final ModelConduit modelConduit = new ModelConduit();

    private void renderConduitItem(RenderBlocks render, ItemStack item, float translateX, float translateY,
            float translateZ) {
        GL11.glPushMatrix();
        GL11.glTranslatef(translateX + 0.5F, translateY + 1.5F, translateZ + 0.5F);
        ResourceLocation test = new ResourceLocation("alchemicalwizardry:textures/models/Conduit.png");
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.modelConduit.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ForgeDirection.DOWN, ForgeDirection.UP);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    /**
     * IItemRenderer implementation *
     */
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return switch (type) {
            case ENTITY, INVENTORY, EQUIPPED_FIRST_PERSON, EQUIPPED -> true;
            default -> false;
        };
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY, INVENTORY:
                renderConduitItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
                break;
            case EQUIPPED, EQUIPPED_FIRST_PERSON:
                renderConduitItem((RenderBlocks) data[0], item, -0.4f, 0.50f, 0.35f);
                break;
            default:
        }
    }
}
