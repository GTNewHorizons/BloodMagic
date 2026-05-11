package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellParadigmBlock;
import cpw.mods.fml.client.FMLClientHandler;

public class TESpellParadigmBlockItemRenderer implements IItemRenderer {

    private static final ResourceLocation TEXTURE_SELF = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellParadigmSelf.png");
    private static final ResourceLocation TEXTURE_MELEE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellParadigmMelee.png");
    private static final ResourceLocation TEXTURE_TOOL = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellParadigmTool.png");
    private static final ResourceLocation TEXTURE_PROJECTILE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellParadigmProjectile.png");
    private static final ModelSpellParadigmBlock MODEL = new ModelSpellParadigmBlock();

    private void renderSpellBlock(ItemStack item, float translateX, float translateY, float translateZ) {
        GL11.glPushMatrix();
        GL11.glTranslatef(translateX + 0.5F, translateY + 1.5F, translateZ + 0.5F);
        ResourceLocation test = this.getResourceLocationForMeta(item.getItemDamage());
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
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
            case 1 -> TEXTURE_SELF;
            case 2 -> TEXTURE_MELEE;
            case 3 -> TEXTURE_TOOL;
            default -> TEXTURE_PROJECTILE;
        };
    }
}
