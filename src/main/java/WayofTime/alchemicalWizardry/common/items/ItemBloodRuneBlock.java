package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBloodRuneBlock extends ItemBlock {

    public ItemBloodRuneBlock(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        String name;

        switch (itemstack.getItemDamage()) {
            case 0: {
                name = "blank";
                break;
            }

            case 1: {
                name = "fill";
                break;
            }

            case 2:
                name = "empty";
                break;

            case 3:
                name = "orb";
                break;

            case 4:
                name = "betterCapacity";
                break;

            case 5:
                name = "acceleration";
                break;

            case 6:
                name = "quickness";
                break;

            default:
                name = "broken";
        }

        return getUnlocalizedName() + "." + name;
    }

    public int getMetadata(int par1) {

        return par1;
    }
}
