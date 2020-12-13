package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.IRitualStone;
import WayofTime.alchemicalWizardry.common.items.ScribeTool;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class RitualStone extends Block implements IRitualStone
{
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
    @SideOnly(Side.CLIENT)
    private IIcon bloodStoneIcon;

    public RitualStone()
    {
        super(Material.iron);
        setHardness(2.0F);
        setResistance(5.0F);
        this.setBlockName("ritualStone");
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blankIcon = iconRegister.registerIcon("AlchemicalWizardry:RitualStone");
        this.waterStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterRitualStone");
        this.fireStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:FireRitualStone");
        this.earthStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:EarthRitualStone");
        this.airStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:AirRitualStone");
        this.duskStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:DuskRitualStone");
        this.dawnStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:LightRitualStone");
        this.bloodStoneIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodRitualStone");
    }

    @Override
    public int damageDropped(int metadata)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
    {
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem == null)
        {
            return false;
        }

        Item item = playerItem.getItem();

        if (!(item instanceof ScribeTool))
        {
            return false;
        }

        if (playerItem.getMaxDamage() <= playerItem.getItemDamage() && !(playerItem.getMaxDamage() == 0))
        {
            return false;
        }

        ScribeTool scribeTool = (ScribeTool) item;

        if (!player.capabilities.isCreativeMode)
        {
            playerItem.setItemDamage(playerItem.getItemDamage() + 1);
        }

        world.setBlockMetadataWithNotify(x, y, z, scribeTool.getType(), 3);
        world.markBlockForUpdate(x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        switch (metadata)
        {
            case 0:
                return blankIcon;

            case 1:
                return waterStoneIcon;

            case 2:
                return fireStoneIcon;

            case 3:
                return earthStoneIcon;

            case 4:
                return airStoneIcon;

            case 5:
                return duskStoneIcon;
                
            case 6:
            	return dawnStoneIcon;

            case 7:
            	return bloodStoneIcon;
            	
            default:
                return blankIcon;
        }
    }

	@Override
	public boolean isRuneType(World world, int x, int y, int z, int meta, int runeType) 
	{
		return meta == runeType;
	}
}
