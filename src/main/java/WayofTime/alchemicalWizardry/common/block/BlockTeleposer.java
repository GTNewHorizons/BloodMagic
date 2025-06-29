package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.event.TeleposeEvent;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import codechicken.multipart.MultipartHelper;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTeleposer extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon topIcon;

    @SideOnly(Side.CLIENT)
    private IIcon sideIcon;

    public BlockTeleposer() {
        super(Material.rock);
        setHardness(2.0F);
        setResistance(5.0F);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setBlockName("bloodTeleposer");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.topIcon = iconRegister.registerIcon("AlchemicalWizardry:Teleposer_Top");
        this.sideIcon = iconRegister.registerIcon("AlchemicalWizardry:Teleposer_Side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 1) return topIcon;
        return sideIcon;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what,
            float these, float are) {
        ItemStack playerItem = player.getCurrentEquippedItem();

        if (playerItem == null || !(playerItem.getItem() instanceof TelepositionFocus)) {
            player.openGui(AlchemicalWizardry.instance, 1, world, x, y, z);
            return true;
        }

        SoulNetworkHandler.checkAndSetItemPlayer(playerItem, player);

        NBTTagCompound itemTag = IBindable.getTag(playerItem);
        itemTag.setInteger("xCoord", x);
        itemTag.setInteger("yCoord", y);
        itemTag.setInteger("zCoord", z);
        itemTag.setInteger("dimensionId", world.provider.dimensionId);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z) {
        Random rand = new Random();
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory inventory)) {
            return;
        }

        ItemStack item = inventory.getStackInSlot(0);

        if (item == null || item.stackSize <= 0) {
            return;
        }

        float rx = rand.nextFloat() * 0.8F + 0.1F;
        float ry = rand.nextFloat() * 0.8F + 0.1F;
        float rz = rand.nextFloat() * 0.8F + 0.1F;
        EntityItem entityItem = new EntityItem(
                world,
                x + rx,
                y + ry,
                z + rz,
                new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

        if (item.hasTagCompound()) {
            entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
        }

        float factor = 0.05F;
        entityItem.motionX = rand.nextGaussian() * factor;
        entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = rand.nextGaussian() * factor;
        world.spawnEntityInWorld(entityItem);
        item.stackSize = 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TETeleposer();
    }

    public static boolean swapBlocks(Object caller, World worldA, World worldB, int xA, int yA, int zA, int xB, int yB,
            int zB) {
        return swapBlocks(caller, worldA, worldB, xA, yA, zA, xB, yB, zB, true, 3);
    }

    public static boolean swapBlocksWithoutSound(Object caller, World worldA, World worldB, int xA, int yA, int zA,
            int xB, int yB, int zB) {
        return swapBlocks(caller, worldA, worldB, xA, yA, zA, xB, yB, zB, false, 3);
    }

    public static boolean swapBlocks(Object caller, World worldA, World worldB, int xA, int yA, int zA, int xB, int yB,
            int zB, boolean doSound, int flag) {
        TileEntity tileA = worldA.getTileEntity(xA, yA, zA);
        TileEntity tileB = worldB.getTileEntity(xB, yB, zB);

        NBTTagCompound nbtA = new NBTTagCompound();
        NBTTagCompound nbtB = new NBTTagCompound();

        if (tileA != null) tileA.writeToNBT(nbtA);
        if (tileB != null) tileB.writeToNBT(nbtB);

        Block blockA = worldA.getBlock(xA, yA, zA);
        Block blockB = worldB.getBlock(xB, yB, zB);

        if (blockA.equals(Blocks.air) && blockB.equals(Blocks.air)) return false;

        if (!(caller instanceof TEDemonPortal) && (blockA instanceof BlockPortal || blockB instanceof BlockPortal))
            return false;

        int metaA = worldA.getBlockMetadata(xA, yA, zA);
        int metaB = worldB.getBlockMetadata(xB, yB, zB);

        TeleposeEvent evt = new TeleposeEvent(worldA, xA, yA, zA, blockA, metaA, worldB, xB, yB, zB, blockB, metaB);
        if (MinecraftForge.EVENT_BUS.post(evt)) return false;

        if (doSound) {
            String sound = "mob.endermen.portal";
            worldA.playSoundEffect(xA, yA, zA, sound, 1.0F, 1.0F);
            worldB.playSoundEffect(xB, yB, zB, sound, 1.0F, 1.0F);
        }

        // Clear current tile entities
        worldB.setTileEntity(xB, yB, zB, blockB.createTileEntity(worldB, metaB));
        worldA.setTileEntity(xA, yA, zA, blockA.createTileEntity(worldA, metaA));

        // Swap blocks
        worldB.setBlock(xB, yB, zB, blockA, metaA, flag);
        worldA.setBlock(xA, yA, zA, blockB, metaB, flag);

        // Restore tile entities
        swapTile(worldB, xB, yB, zB, tileA, nbtA);
        swapTile(worldA, xA, yA, zA, tileB, nbtB);

        return true;
    }

    private static void swapTile(World world, int x, int y, int z, TileEntity oldTile, NBTTagCompound nbt) {
        if (oldTile == null) return;

        TileEntity newTile;

        boolean multipart = AlchemicalWizardry.isFMPLoaded && oldTile instanceof TileMultipart;
        if (multipart) {
            newTile = MultipartHelper.createTileFromNBT(world, nbt);
        } else {
            newTile = TileEntity.createAndLoadEntity(nbt);
        }

        world.setTileEntity(x, y, z, newTile);

        newTile.xCoord = x;
        newTile.yCoord = y;
        newTile.zCoord = z;

        if (multipart) MultipartHelper.sendDescPacket(world, newTile);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if (!(world.getTileEntity(x, y, z) instanceof TETeleposer teleposer)) return;
        // was it a low->high pulse
        boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
        if (!teleposer.hasRedstone && isPowered) {
            teleposer.hasRedstone = true;
            teleposer.activate();
        } else {
            teleposer.hasRedstone = isPowered;
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }
}
