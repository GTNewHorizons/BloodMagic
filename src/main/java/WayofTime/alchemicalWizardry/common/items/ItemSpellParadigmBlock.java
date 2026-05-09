package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellParadigmBlock extends ItemBlock {

    public ItemSpellParadigmBlock(Block par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        String name = switch (itemstack.getItemDamage()) {
            case 0 -> "projectile";
            case 1 -> "self";
            case 2 -> "melee";
            case 3 -> "tool";
            default -> "broken";
        };

        return getUnlocalizedName() + "." + name;
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
