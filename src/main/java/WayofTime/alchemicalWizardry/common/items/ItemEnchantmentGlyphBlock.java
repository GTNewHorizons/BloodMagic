package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemEnchantmentGlyphBlock extends ItemBlock {

    public ItemEnchantmentGlyphBlock(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return getUnlocalizedName() + "." + switch (itemstack.getItemDamage()) {
            case 0 -> "enchantability";
            case 1 -> "enchantmentLevel";
            default -> "broken";
        };
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
