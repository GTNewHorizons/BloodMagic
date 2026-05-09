package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrystal extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon brickIcon;

    public BlockCrystal() {
        super(Material.iron);
        this.setBlockName("crystalBlock");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:ShardCluster");
        this.brickIcon = iconRegister.registerIcon("AlchemicalWizardry:ShardClusterBrick");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List<ItemStack> items) {
        if (this.equals(ModBlocks.blockCrystal)) {
            items.add(new ItemStack(item, 1, 0));
            items.add(new ItemStack(item, 1, 1));
        } else {
            super.getSubBlocks(item, par2CreativeTabs, items);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == 1) {
            return brickIcon;
        }
        return blockIcon;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }
}
