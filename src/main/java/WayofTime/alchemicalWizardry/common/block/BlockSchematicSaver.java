package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.tileEntity.TESchematicSaver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSchematicSaver extends BlockContainer {

    public BlockSchematicSaver() {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        // setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("schematicSaver");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("AlchemicalWizardry:SchematicSaver");
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TESchematicSaver();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float what,
            float these, float are) {
        if (world.isRemote) {
            return false;
        }

        TESchematicSaver tileEntity = (TESchematicSaver) world.getTileEntity(x, y, z);

        tileEntity.rightClickBlock(player, side);

        return false;
    }
}
