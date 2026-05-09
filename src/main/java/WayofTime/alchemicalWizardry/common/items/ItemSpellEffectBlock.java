package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellEffectBlock extends ItemBlock {

    public ItemSpellEffectBlock(Block par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        String name = switch (itemstack.getItemDamage()) {
            case 0 -> "fire";
            case 1 -> "ice";
            case 2 -> "wind";
            case 3 -> "earth";
            default -> "broken";
        };

        return getUnlocalizedName() + "." + name;
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
