package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCrystalBlock extends ItemBlock {

    public ItemCrystalBlock(Block par1) {
        super(par1);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return getUnlocalizedName() + "." + switch (itemstack.getItemDamage()) {
            case 0 -> "fullCrystal";
            case 1 -> "crystalBrick";
            default -> "broken";
        };
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
