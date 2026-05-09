package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellModifierBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpellModifier extends BlockOrientable {

    public BlockSpellModifier() {
        super();
        this.setBlockName("blockSpellModifier");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TESpellModifierBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        if (this.equals(ModBlocks.blockSpellModifier)) {
            for (int i = 0; i < 4; i++) {
                items.add(new ItemStack(item, 1, i));
            }
        } else {
            super.getSubBlocks(item, tab, items);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }
}
