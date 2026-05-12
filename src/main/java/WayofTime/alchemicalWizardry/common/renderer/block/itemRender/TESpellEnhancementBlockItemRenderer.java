package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellEnhancementBlock;
import cpw.mods.fml.client.FMLClientHandler;

public class TESpellEnhancementBlockItemRenderer implements IItemRenderer {

    private static final ResourceLocation TEXTURE_POWER_1 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPower1.png");
    private static final ResourceLocation TEXTURE_POWER_2 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPower2.png");
    private static final ResourceLocation TEXTURE_POWER_3 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPower3.png");
    private static final ResourceLocation TEXTURE_POWER_4 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPower4.png");
    private static final ResourceLocation TEXTURE_COST_1 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementCost1.png");
    private static final ResourceLocation TEXTURE_COST_2 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementCost2.png");
    private static final ResourceLocation TEXTURE_COST_3 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementCost3.png");
    private static final ResourceLocation TEXTURE_COST_4 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementCost4.png");
    private static final ResourceLocation TEXTURE_POTENCY_1 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPotency1.png");
    private static final ResourceLocation TEXTURE_POTENCY_2 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPotency2.png");
    private static final ResourceLocation TEXTURE_POTENCY_3 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPotency3.png");
    private static final ResourceLocation TEXTURE_POTENCY_4 = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SpellEnhancementPotency4.png");
    private static final ModelSpellEnhancementBlock MODEL = new ModelSpellEnhancementBlock();

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
            case 1 -> TEXTURE_POWER_2;
            case 2 -> TEXTURE_POWER_3;
            case 3 -> TEXTURE_POWER_4;
            case 5 -> TEXTURE_COST_1;
            case 6 -> TEXTURE_COST_2;
            case 7 -> TEXTURE_COST_3;
            case 8 -> TEXTURE_COST_4;
            case 10 -> TEXTURE_POTENCY_1;
            case 11 -> TEXTURE_POTENCY_2;
            case 12 -> TEXTURE_POTENCY_3;
            case 13 -> TEXTURE_POTENCY_4;
            default -> TEXTURE_POWER_1;
        };
    }
}
