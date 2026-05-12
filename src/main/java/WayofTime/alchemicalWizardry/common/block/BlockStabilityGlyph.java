package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.omega.IStabilityGlyph;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStabilityGlyph extends Block implements IStabilityGlyph {

    public BlockStabilityGlyph() {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("stabilityGlyph");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:GlyphStability1");
    }

    @Override
    public int getAdditionalStabilityForFaceCount(World world, int x, int y, int z, int meta, int faceCount) {
        if (meta == 0) {
            return faceCount * 2;
        }
        return faceCount;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        for (int i = 0; i < 1; i++) {
            items.add(new ItemStack(item, 1, i));
        }
    }
}
