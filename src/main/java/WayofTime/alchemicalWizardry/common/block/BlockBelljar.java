package WayofTime.alchemicalWizardry.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEBellJar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBelljar extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon bottomIcon;

    @SideOnly(Side.CLIENT)
    private IIcon otherIcon;

    public BlockBelljar() {
        super(Material.glass);
        setHardness(2.0F);
        setResistance(5.0F);
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("crystalBelljar");
        this.setStepSound(Block.soundTypeGlass);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0) {
            return bottomIcon;
        }
        return otherIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.bottomIcon = iconRegister.registerIcon("minecraft:planks_oak");
        this.otherIcon = iconRegister.registerIcon("minecraft:glass");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> items) {
        if (this.equals(ModBlocks.blockCrystalBelljar)) {
            items.add(new ItemStack(item, 1, 0));

            for (Reagent reagent : ReagentRegistry.reagentList.values()) {
                ItemStack stack = new ItemStack(item, 1, 0);
                NBTTagCompound tag = new NBTTagCompound();

                ReagentContainer[] tanks = new ReagentContainer[1];
                tanks[0] = new ReagentContainer(reagent, 16000, 16000);

                NBTTagList tagList = new NBTTagList();

                NBTTagCompound savedTag = new NBTTagCompound();
                tanks[0].writeToNBT(savedTag);
                tagList.appendTag(savedTag);

                tag.setTag("reagentTanks", tagList);

                stack.setTagCompound(tag);

                items.add(stack);
            }
        } else {
            super.getSubBlocks(item, tab, items);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEBellJar) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null) {
                ((TEBellJar) tile).readTankNBTOnPlace(tag);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TEBellJar();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TEBellJar) {
            return ((TEBellJar) tile).getRSPowerOutput();
        }
        return 15;
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        this.dropBlockAsItem(world, x, y, z, meta, 0);
        super.onBlockHarvested(world, x, y, z, meta, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> list = new ArrayList<>();

        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TEBellJar bellJar) {
            ItemStack drop = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            bellJar.writeTankNBT(tag);
            drop.setTagCompound(tag);

            list.add(drop);
        }

        return list;
    }
}
