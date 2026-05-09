package WayofTime.alchemicalWizardry.common.renderer.block.itemRender;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.common.renderer.model.ModelSpellEnhancementBlock;
import cpw.mods.fml.client.FMLClientHandler;

public class TESpellEnhancementBlockItemRenderer implements IItemRenderer {

    private final ModelSpellEnhancementBlock modelSpellBlock = new ModelSpellEnhancementBlock();

    private void renderConduitItem(RenderBlocks render, ItemStack item, float translateX, float translateY,
            float translateZ) {
        GL11.glPushMatrix();
        GL11.glTranslatef(translateX + 0.5F, translateY + 1.5F, translateZ + 0.5F);
        ResourceLocation test = new ResourceLocation(this.getResourceLocationForMeta(item.getItemDamage()));

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(test);
        GL11.glPushMatrix();
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.modelSpellBlock
                .render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ForgeDirection.DOWN, ForgeDirection.UP);
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

    public String getResourceLocationForMeta(int meta) {
        return switch (meta) {
            case 2 -> "alchemicalwizardry:textures/models/SpellEnhancementPower3.png";
            case 3 -> "alchemicalwizardry:textures/models/SpellEnhancementPower4.png";
            case 5 -> "alchemicalwizardry:textures/models/SpellEnhancementCost1.png";
            case 6 -> "alchemicalwizardry:textures/models/SpellEnhancementCost2.png";
            case 7 -> "alchemicalwizardry:textures/models/SpellEnhancementCost3.png";
            case 8 -> "alchemicalwizardry:textures/models/SpellEnhancementCost4.png";
            case 10 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency1.png";
            case 11 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency2.png";
            case 12 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency3.png";
            case 13 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency4.png";
            default -> "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
        };
    }
}
