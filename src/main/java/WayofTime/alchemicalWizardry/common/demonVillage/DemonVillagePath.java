package WayofTime.alchemicalWizardry.common.demonVillage;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.IRoadWard;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonVillagePath {

    public int xPos;
    public int yPos;
    public int zPos;
    public ForgeDirection dir;
    public int length;

    public static boolean canGoDown = true;
    public static boolean tunnelIfObstructed = false;
    public static boolean createBridgeInAirIfObstructed = false;

    public DemonVillagePath(int xPos, int yPos, int zPos, ForgeDirection dir, int length) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.dir = dir;
        this.length = length;
    }

    public Int3AndBool constructFullPath(TEDemonPortal portal, World world, int clearance) {
        int xi = this.xPos;
        int yi = this.yPos;
        int zi = this.zPos;
        int rad = this.getRoadRadius();
        int value = 0;

        int finalYPos = this.constructPartialPath(
                portal,
                world,
                clearance,
                xi - rad * dir.offsetX,
                yi,
                zi - rad * dir.offsetZ,
                dir,
                length + rad,
                false);

        for (int i = -rad; i <= rad; i++) {
            value = Math.max(
                    this.constructPartialPath(
                            portal,
                            world,
                            clearance,
                            xi - rad * dir.offsetX + i * dir.offsetZ,
                            yi,
                            zi - rad * dir.offsetZ + i * dir.offsetX,
                            dir,
                            length + 2 * rad,
                            true),
                    value);
            if (TEDemonPortal.printDebug) System.out.println((length + 2 * rad) + ", " + value);
        }

        Int3 position = new Int3(xi, finalYPos, zi);

        boolean bool = value >= length + 2 * rad;

        return new Int3AndBool(position, bool);
    }

    public static class Int3AndBool {

        public Int3 coords;
        public boolean bool;

        public Int3AndBool(Int3 int3, boolean bool) {
            this.coords = int3;
            this.bool = bool;
        }
    }

    public int constructPartialPath(TEDemonPortal portal, World world, int clearance, int x, int y, int z,
            ForgeDirection dir, int length, boolean doConstruct) {
        for (int i = 0; i < length; i++) {
            int xOffset = i * dir.offsetX;
            int zOffset = i * dir.offsetZ;

            boolean completed = false;

            for (int yOffset = 0; yOffset <= clearance; yOffset++) {
                int sign = 1;

                Block block1 = world.getBlock(x + xOffset, y + sign * yOffset, z + zOffset);
                Block highBlock1 = world.getBlock(x + xOffset, y + sign * yOffset + 1, z + zOffset);

                if ((this.forceReplaceBlock(block1))
                        || (!block1.isReplaceable(world, x + xOffset, y + sign * yOffset, z + zOffset)
                                && this.isBlockReplaceable(block1))
                                && (this.forceCanTunnelUnder(highBlock1) || highBlock1
                                        .isReplaceable(world, x + xOffset, y + sign * yOffset + 1, z + zOffset))) {
                    if (doConstruct) {
                        world.setBlock(
                                x + xOffset,
                                y + sign * yOffset,
                                z + zOffset,
                                portal.getRoadBlock(),
                                portal.getRoadMeta(),
                                3);
                    }
                    y += sign * yOffset;
                    completed = true;
                    break;
                } else if (canGoDown) {
                    sign = -1;
                    Block block2 = world.getBlock(x + xOffset, y + sign * yOffset, z + zOffset);
                    Block highBlock2 = world.getBlock(x + xOffset, y + sign * yOffset + 1, z + zOffset);

                    if ((this.forceReplaceBlock(block2))
                            || (!block2.isReplaceable(world, x + xOffset, y + sign * yOffset, z + zOffset)
                                    && this.isBlockReplaceable(block2))
                                    && (this.forceCanTunnelUnder(highBlock2) || highBlock2
                                            .isReplaceable(world, x + xOffset, y + sign * yOffset + 1, z + zOffset))) {
                        if (doConstruct) {
                            world.setBlock(
                                    x + xOffset,
                                    y + sign * yOffset,
                                    z + zOffset,
                                    portal.getRoadBlock(),
                                    portal.getRoadMeta(),
                                    3);
                        }
                        y += sign * yOffset;
                        completed = true;
                        break;
                    }
                }
            }

            if (!completed) {
                boolean returnAmount = true;
                if (createBridgeInAirIfObstructed) {
                    Block block1 = world.getBlock(x + xOffset, y, z + zOffset);

                    if (block1.isReplaceable(world, x + xOffset, y, z + zOffset) || !this.isBlockReplaceable(block1)
                            || !this.forceReplaceBlock(block1)) {
                        returnAmount = false;

                        if (doConstruct) {
                            world.setBlock(x + xOffset, y, z + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                        }
                    }

                } else if (tunnelIfObstructed) {
                    Block block1 = world.getBlock(x + xOffset, y, z + zOffset);

                    if (!block1.isReplaceable(world, x + xOffset, y, z + zOffset) || this.isBlockReplaceable(block1)
                            || !this.forceReplaceBlock(block1)) {
                        returnAmount = false;

                        if (doConstruct) {
                            world.setBlock(x + xOffset, y, z + zOffset, portal.getRoadBlock(), portal.getRoadMeta(), 3);
                            world.setBlockToAir(x + xOffset, y + 1, z + zOffset);
                            world.setBlockToAir(x + xOffset, y + 2, z + zOffset);
                            world.setBlockToAir(x + xOffset, y + 3, z + zOffset);
                        }
                    }
                }

                if (returnAmount) {
                    return doConstruct ? i : y;
                }
            }
        }

        return doConstruct ? length : y;
    }

    public int getRoadRadius() {
        return 1;
    }

    public boolean isBlockReplaceable(Block block) {
        return !(block.getMaterial() == Material.leaves || block.getMaterial() == Material.vine
                || block instanceof IRoadWard) && !block.equals(ModBlocks.blockDemonPortal);
    }

    public boolean forceReplaceBlock(Block block) {
        return block.getMaterial().isLiquid();
    }

    public boolean forceCanTunnelUnder(Block block) {
        return block instanceof BlockFlower || block == Blocks.double_plant;
    }

    public int xPos() {
        return xPos;
    }

    public int yPos() {
        return yPos;
    }

    public int zPos() {
        return zPos;
    }

    public ForgeDirection dir() {
        return dir;
    }

    public int length() {
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DemonVillagePath that)) return false;
        return this.xPos == that.xPos && this.yPos == that.yPos
                && this.zPos == that.zPos
                && Objects.equals(this.dir, that.dir)
                && this.length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos, zPos, dir, length);
    }

    @Override
    public String toString() {
        return "DemonVillagePath[" + "xPos="
                + xPos
                + ", "
                + "yPos="
                + yPos
                + ", "
                + "zPos="
                + zPos
                + ", "
                + "dir="
                + dir
                + ", "
                + "length="
                + length
                + ']';
    }

}
