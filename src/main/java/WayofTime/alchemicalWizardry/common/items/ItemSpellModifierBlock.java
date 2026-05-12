package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellModifierBlock extends ItemBlock {

    public ItemSpellModifierBlock(Block par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        String name = switch (itemstack.getItemDamage()) {
            case 0 -> "default";
            case 1 -> "offensive";
            case 2 -> "defensive";
            case 3 -> "environmental";
            default -> "broken";
        };

        return getUnlocalizedName() + "." + name;
    }

    @Override
    public int getMetadata(int par1) {

        return par1;
    }
}
