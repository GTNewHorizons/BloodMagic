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
        return switch (metaData) {
            case 1 -> 5; // Altar Capacity rune
            case 2 -> 6; // Filling/emptying rune
            case 3 -> 7; // Orb Capacity rune
            case 4 -> 8; // Better Capacity rune
            case 5 -> 9; // Acceleration rune
            case 6 -> 10; // Quickness Rune
            default -> 0;
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        if (this.equals(ModBlocks.bloodRune)) {
            for (int i = 0; i < 7; i++) {
                items.add(new ItemStack(item, 1, i));
            }
        } else {
            super.getSubBlocks(item, tab, items);
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
