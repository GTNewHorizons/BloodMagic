package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.loot.DemonVillageLootRegistry;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.IBlockPortalNode;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.ITilePortalNode;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class BlockSet {

    protected String blockid;
    protected int[] metadata;
    protected List<Int3> positions;

    public BlockSet() {
        this(Blocks.stone);
    }

    public BlockSet(String blockid) {
        this.blockid = blockid;
        this.metadata = new int[4];
        positions = new ArrayList<>();
    }

    public BlockSet(Block block) {
        this(BlockSet.getPairedIdForBlock(block));
    }

    public BlockSet(Block block, int meta) {
        this(block);
        Arrays.fill(metadata, meta);
        if (block instanceof BlockStairs) {
            switch (meta) {
                case 0 -> metadata = new int[] { 0, 1, 3, 2 };
                case 1 -> metadata = new int[] { 1, 0, 2, 3 };
                case 2 -> metadata = new int[] { 2, 3, 0, 1 };
                case 3 -> metadata = new int[] { 3, 2, 1, 0 };
                case 4 -> metadata = new int[] { 4, 5, 7, 6 };
                case 5 -> metadata = new int[] { 5, 4, 6, 7 };
                case 6 -> metadata = new int[] { 6, 7, 4, 5 };
                case 7 -> metadata = new int[] { 7, 6, 5, 4 };
            }
        } else if (block instanceof BlockLadder) {
            switch (meta) {
                case 2 -> metadata = new int[] { 2, 3, 4, 5 };
                case 3 -> metadata = new int[] { 3, 2, 5, 4 };
                case 4 -> metadata = new int[] { 4, 5, 3, 2 };
                case 5 -> metadata = new int[] { 5, 4, 2, 3 };
            }
        } else if (block instanceof BlockTrapDoor) {
            int div = meta / 4;
            switch (meta % 4) {
                case 0 -> metadata = new int[] { div * 4, 1 + div * 4, 2 + div * 4, 3 + div * 4 };
                case 1 -> metadata = new int[] { 1 + div * 4, div * 4, 3 + div * 4, 2 + div * 4 };
                case 2 -> metadata = new int[] { 2 + div * 4, 3 + div * 4, 1 + div * 4, div * 4 };
                case 3 -> metadata = new int[] { 3 + div * 4, 2 + div * 4, div * 4, 1 + div * 4 };
            }
        } else if (block instanceof BlockTorch) {
            switch (meta) {
                case 1 -> metadata = new int[] { 1, 2, 4, 3 };
                case 2 -> metadata = new int[] { 2, 1, 3, 4 };
                case 3 -> metadata = new int[] { 3, 4, 1, 2 };
                case 4 -> metadata = new int[] { 4, 3, 2, 1 };
            }
        } else if (block instanceof BlockDoor) {
            switch (meta) {
                case 0 -> metadata = new int[] { 0, 2, 3, 1 };
                case 1 -> metadata = new int[] { 1, 3, 0, 2 };
                case 2 -> metadata = new int[] { 2, 0, 1, 3 };
                case 3 -> metadata = new int[] { 3, 1, 2, 0 };
            }
        } else if (block instanceof BlockRedstoneComparator) {
            int div = meta / 4;
            switch (meta % 4) {
                case 0 -> metadata = new int[] { div * 4, 2 + div * 4, 3 + div * 4, 1 + div * 4 };
                case 1 -> metadata = new int[] { 1 + div * 4, 3 + div * 4, div * 4, 2 + div * 4 };
                case 2 -> metadata = new int[] { 2 + div * 4, div * 4, 1 + div * 4, 3 + div * 4 };
                case 3 -> metadata = new int[] { 3 + div * 4, 1 + div * 4, 2 + div * 4, div * 4 };
            }
        } else if (block instanceof BlockRedstoneRepeater) {
            int div = meta / 4;
            switch (meta % 4) {
                case 0 -> metadata = new int[] { div * 4, 2 + div * 4, 3 + div * 4, 1 + div * 4 };
                case 1 -> metadata = new int[] { 1 + div * 4, 3 + div * 4, div * 4, 2 + div * 4 };
                case 2 -> metadata = new int[] { 2 + div * 4, div * 4, 1 + div * 4, 3 + div * 4 };
                case 3 -> metadata = new int[] { 3 + div * 4, 1 + div * 4, 2 + div * 4, div * 4 };
            }
        }
    }

    public List<Int3> getPositions() {
        return positions;
    }

    public void addPositionToBlock(int xOffset, int yOffset, int zOffset) {
        positions.add(new Int3(xOffset, yOffset, zOffset));
    }

    public Block getBlock() {
        return getBlockForString(blockid);
    }

    public static String getPairedIdForBlock(Block block) {
        UniqueIdentifier un = GameRegistry.findUniqueIdentifierFor(block);
        String name = "";

        if (un != null) {
            name = un.modId + ":" + un.name;
        }

        return name;
    }

    public static Block getBlockForString(String str) {
        String[] parts = str.split(":");
        String modId = parts[0];
        String name = parts[1];
        return GameRegistry.findBlock(modId, name);
    }

    public int getMetaForDirection(ForgeDirection dir) {
        if (metadata.length < 4) {
            return 0;
        }

        return switch (dir) {
            case NORTH -> metadata[0];
            case SOUTH -> metadata[1];
            case WEST -> metadata[2];
            case EAST -> metadata[3];
            default -> 0;
        };
    }

    public void buildAtIndex(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord,
            ForgeDirection dir, int index, boolean populateInventories, int tier) {
        Block block = this.getBlock();
        if (index >= positions.size() || block == null) {
            return;
        }

        Int3 position = positions.get(index);
        int xOff = position.x();
        int yOff = position.y();
        int zOff = position.z();
        int meta = this.getMetaForDirection(dir);

        switch (dir) {
            case SOUTH -> {
                xOff *= -1;
                zOff *= -1;
            }
            case WEST -> {
                int temp = zOff;
                zOff = xOff * -1;
                xOff = temp;
            }
            case EAST -> {
                int temp2 = zOff * -1;
                zOff = xOff;
                xOff = temp2;
            }
        }

        world.setBlock(xCoord + xOff, yCoord + yOff, zCoord + zOff, block, meta, 3);
        if (populateInventories) {
            this.populateIfIInventory(world, xCoord + xOff, yCoord + yOff, zCoord + zOff, tier);
        }
        if (block instanceof IBlockPortalNode) {
            TileEntity tile = world.getTileEntity(xCoord + xOff, yCoord + yOff, zCoord + zOff);
            if (tile instanceof ITilePortalNode) {
                ((ITilePortalNode) tile).setPortalLocation(teDemonPortal);
            }
        }
    }

    public void populateIfIInventory(World world, int x, int y, int z, int tier) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IInventory) {
            DemonVillageLootRegistry.populateChest((IInventory) tile, tier);
        }
    }

    public void buildAll(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord,
            ForgeDirection dir, boolean populateInventories, int tier) {
        for (int i = 0; i < positions.size(); i++) {
            this.buildAtIndex(teDemonPortal, world, xCoord, yCoord, zCoord, dir, i, populateInventories, tier);
        }
    }

    public boolean isContained(Block block, int defaultMeta) {
        Block thisBlock = this.getBlock();
        return thisBlock != null && thisBlock.equals(block) && this.metadata[0] == defaultMeta;
    }
}
