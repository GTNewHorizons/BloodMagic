package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpellParadigmBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpellParadigm extends BlockOrientable {

    public BlockSpellParadigm() {
        super();
        this.setBlockName("blockSpellParadigm");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TESpellParadigmBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        if (this.equals(ModBlocks.blockSpellParadigm)) {
            items.add(new ItemStack(item, 1, 0));
            items.add(new ItemStack(item, 1, 1));
            items.add(new ItemStack(item, 1, 2));
            items.add(new ItemStack(item, 1, 3));
        } else {
            super.getSubBlocks(item, tab, items);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what,
            float these, float are) {
        ItemStack stack = player.getCurrentEquippedItem();

        if (stack != null && stack.getItem() instanceof ItemComplexSpellCrystal) {
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound itemTag = stack.getTagCompound();
            itemTag.setInteger("xCoord", x);
            itemTag.setInteger("yCoord", y);
            itemTag.setInteger("zCoord", z);
            itemTag.setInteger("dimensionId", world.provider.dimensionId);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, what, these, are);
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
