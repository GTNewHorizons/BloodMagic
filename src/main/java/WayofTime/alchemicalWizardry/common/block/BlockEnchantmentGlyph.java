package WayofTime.alchemicalWizardry.common.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.omega.IEnchantmentGlyph;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEnchantmentGlyph extends Block implements IEnchantmentGlyph {

    @SideOnly(Side.CLIENT)
    private IIcon enchantmentLevel;

    public BlockEnchantmentGlyph() {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("enchantmentGlyph");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:GlyphEnchantability");
        this.enchantmentLevel = iconRegister.registerIcon("AlchemicalWizardry:GlyphEnchantmentLevel");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == 1) {
            return enchantmentLevel;
        }
        return this.blockIcon;
    }

    @Override
    public int getAdditionalStabilityForFaceCount(World world, int x, int y, int z, int meta, int faceCount) {
        if (meta == 0) {
            return -faceCount * 10;
        }
        return -faceCount * 20;
    }

    @Override
    public int getEnchantability(World world, int x, int y, int z, int meta) {
        if (meta == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getEnchantmentLevel(World world, int x, int y, int z, int meta) {
        if (meta == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        for (int i = 0; i < 2; i++) {
            items.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }
}
