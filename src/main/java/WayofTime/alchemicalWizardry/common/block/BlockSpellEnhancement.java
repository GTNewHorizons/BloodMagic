package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellEnhancementBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpellEnhancement extends BlockOrientable {

    public BlockSpellEnhancement() {
        super();
        this.setBlockName("blockSpellEnhancement");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TESpellEnhancementBlock();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        if (this.equals(ModBlocks.blockSpellEnhancement)) {
            for (int i = 0; i < 15; i++) {
                items.add(new ItemStack(item, 1, i));
            }
        } else {
            super.getSubBlocks(item, tab, items);
        }
    }
}
