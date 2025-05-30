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

public class BloodRune extends Block {

    // private Icon bloodRuneIcon;
    @SideOnly(Side.CLIENT)
    private IIcon altarCapacityRuneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon dislocationRuneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon orbCapacityRuneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon betterCapacityRuneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon accelerationRuneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon quicknessRuneIcon;

    public BloodRune() {
        super(Material.iron);
        this.setBlockName("bloodRune");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankRune");
        this.altarCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:AltarCapacityRune");
        this.dislocationRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:DislocationRune");
        this.orbCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:OrbCapacityRune");
        this.betterCapacityRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:BetterCapacityRune");
        this.accelerationRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:AccelerationRune");
        this.quicknessRuneIcon = iconRegister.registerIcon("AlchemicalWizardry:QuicknessRune");
    }

    public int getRuneEffect(int metaData) {
        switch (metaData) {
            case 0:
                return 0;

            case 1: // Altar Capacity rune
                return 5;

            case 2: // Filling/emptying rune
                return 6;

            case 3: // Orb Capacity rune
                return 7;

            case 4: // Better Capacity rune
                return 8;

            case 5: // Acceleration rune
                return 9;

            case 6: // Quickness Rune
                return 10;
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        if (this.equals(ModBlocks.bloodRune)) {
            par3List.add(new ItemStack(par1, 1, 0));
            par3List.add(new ItemStack(par1, 1, 1));
            par3List.add(new ItemStack(par1, 1, 2));
            par3List.add(new ItemStack(par1, 1, 3));
            par3List.add(new ItemStack(par1, 1, 4));
            par3List.add(new ItemStack(par1, 1, 5));
            par3List.add(new ItemStack(par1, 1, 6));
        } else {
            super.getSubBlocks(par1, par2CreativeTabs, par3List);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return switch (meta) {
            case 1 -> altarCapacityRuneIcon;
            case 2 -> dislocationRuneIcon;
            case 3 -> this.orbCapacityRuneIcon;
            case 4 -> this.betterCapacityRuneIcon;
            case 5 -> this.accelerationRuneIcon;
            case 6 -> this.quicknessRuneIcon;
            default -> blockIcon;
        };
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }
}
