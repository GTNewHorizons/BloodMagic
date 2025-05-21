package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.IRitualStone;
import WayofTime.alchemicalWizardry.common.items.ScribeTool;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RitualStone extends Block implements IRitualStone {

    @SideOnly(Side.CLIENT)
    private IIcon blankIcon;

    @SideOnly(Side.CLIENT)
    private IIcon waterStoneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon fireStoneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon earthStoneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon airStoneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon duskStoneIcon;

    @SideOnly(Side.CLIENT)
    private IIcon dawnStoneIcon;

    public RitualStone() {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        this.setBlockName("ritualStone");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blankIcon = iconRegister.registerIcon("AlchemicalWizardry:RitualStone");
        this.waterStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterRitualStone");
        this.fireStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:FireRitualStone");
        this.earthStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:EarthRitualStone");
        this.airStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:AirRitualStone");
        this.duskStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:DuskRitualStone");
        this.dawnStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:LightRitualStone");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX,
            float subY, float subZ) {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || !(stack.getItem() instanceof ScribeTool scribe)) {
            return false;
        }

        int toolType = scribe.getType();

        if (toolType == world.getBlockMetadata(x, y, z)) {
            return false;
        }

        if (stack.getMaxDamage() != 0 && stack.getItemDamage() >= stack.getMaxDamage()) {
            return false;
        }

        if (!player.capabilities.isCreativeMode) {
            stack.setItemDamage(stack.getItemDamage() + 1);
        }

        world.setBlockMetadataWithNotify(x, y, z, toolType, 3);
        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return switch (metadata) {
            case 1 -> waterStoneIcon;
            case 2 -> fireStoneIcon;
            case 3 -> earthStoneIcon;
            case 4 -> airStoneIcon;
            case 5 -> duskStoneIcon;
            case 6 -> dawnStoneIcon;
            default -> blankIcon;
        };
    }

    @Override
    public boolean isRuneType(World world, int x, int y, int z, int meta, int runeType) {
        return meta == runeType;
    }
}
