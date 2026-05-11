package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellEffectBlock;
import cpw.mods.fml.client.FMLClientHandler;

public class TESpellEffectBlockItemRenderer implements IItemRenderer {

    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEffectFire.png");
    private static final ResourceLocation TEXTURE_ICE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEffectIce.png");
    private static final ResourceLocation TEXTURE_WIND = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEffectWind.png");
    private static final ResourceLocation TEXTURE_EARTH = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEffectEarth.png");
    private static final ModelSpellEffectBlock MODEL = new ModelSpellEffectBlock();

    private void renderSpellBlock(ItemStack item, float translateX, float translateY, float translateZ) {
        GL11.glPushMatrix();
        GL11.glTranslatef(translateX + 0.5F, translateY + 1.5F, translateZ + 0.5F);
        FMLClientHandler.instance().getClient().renderEngine
                .bindTexture(this.getResourceLocationForMeta(item.getItemDamage()));
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        MODEL.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ForgeDirection.DOWN, ForgeDirection.UP);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    /**
     * IItemRenderer implementation *
     */
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY, INVENTORY -> renderSpellBlock(item, -0.5f, -0.5f, -0.5f);
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> renderSpellBlock(item, -0.4f, 0.50f, 0.35f);
            default -> {}
        }
    }

    public ResourceLocation getResourceLocationForMeta(int meta) {
        return switch (meta) {
            case 0 -> TEXTURE_FIRE;
            case 1 -> TEXTURE_ICE;
            case 2 -> TEXTURE_WIND;
            default -> TEXTURE_EARTH;
        };
    }
}
