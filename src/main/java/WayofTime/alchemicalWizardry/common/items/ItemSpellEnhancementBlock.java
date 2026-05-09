package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellEnhancementBlock extends ItemBlock {

    public ItemSpellEnhancementBlock(Block par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        String name = switch (itemstack.getItemDamage()) {
            case 0 -> "power1";
            case 1 -> "power2";
            case 2 -> "power3";
            case 3 -> "power4";
            case 4 -> "power5";
            case 5 -> "cost1";
            case 6 -> "cost2";
            case 7 -> "cost3";
            case 8 -> "cost4";
            case 9 -> "cost5";
            case 10 -> "potency1";
            case 11 -> "potency2";
            case 12 -> "potency3";
            case 13 -> "potency4";
            case 14 -> "potency5";
            default -> "broken";
        };

        return getUnlocalizedName() + "." + name;
    }

    @Override
    public int getMetadata(int par1) {

        return par1;
    }
}
