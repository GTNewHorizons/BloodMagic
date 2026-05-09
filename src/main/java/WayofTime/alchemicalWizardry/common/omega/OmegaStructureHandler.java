package WayofTime.alchemicalWizardry.common.omega;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;

public class OmegaStructureHandler {

    public static final OmegaStructureParameters emptyParam = new OmegaStructureParameters(0, 0, 0);

    public static boolean isStructureIntact(World world, int x, int y, int z) {
        return true;
    }

    public static OmegaStructureParameters getStructureStabilityFactor(World world, int x, int y, int z, int expLim,
            Int3 offset) {

        // 0 indicates unchecked, 1 indicates checked and is air, -1 indicates checked to be right
        // next to air blocks in question but is NOT air
        int[][][] boolList = new int[expLim * 2 + 1][expLim * 2 + 1][expLim * 2 + 1];

        for (int i = 0; i < 2 * expLim + 1; i++) {
            for (int j = 0; j < 2 * expLim + 1; j++) {
                for (int k = 0; k < 2 * expLim + 1; k++) {
                    boolList[i][j][k] = 0;
                }
            }
        }

        boolList[expLim + offset.xCoord][expLim + offset.yCoord][expLim + offset.zCoord] = 1;
        boolean isReady = false;

        while (!isReady) {
            isReady = true;

            for (int i = 0; i < 2 * expLim + 1; i++) {
                for (int j = 0; j < 2 * expLim + 1; j++) {
                    for (int k = 0; k < 2 * expLim + 1; k++) {
                        if (boolList[i][j][k] == 1) {
                            if (i - 1 >= 0 && !(boolList[i - 1][j][k] == 1 || boolList[i - 1][j][k] == -1)) {
                                Block block = world.getBlock(x - expLim + i - 1, y - expLim + j, z - expLim + k);
                                if (world.isAirBlock(x - expLim + i - 1, y - expLim + j, z - expLim + k)
                                        || block == ModBlocks.blockSpectralContainer) {
                                    // One of the found air blocks is at the range boundary, and thus
                                    // the container is incomplete
                                    if (i - 1 == 0) {
                                        return emptyParam;
                                    }
                                    boolList[i - 1][j][k] = 1;
                                    isReady = false;
                                } else {
                                    boolList[i - 1][j][k] = -1;
                                }
                            }

                            if (j - 1 >= 0 && !(boolList[i][j - 1][k] == 1 || boolList[i][j - 1][k] == -1)) {
                                Block block = world.getBlock(x - expLim + i, y - expLim + j - 1, z - expLim + k);
                                if (world.isAirBlock(x - expLim + i, y - expLim + j - 1, z - expLim + k)
                                        || block == ModBlocks.blockSpectralContainer) {
                                    if (j - 1 == 0) {
                                        return emptyParam;
                                    }
                                    boolList[i][j - 1][k] = 1;
                                    isReady = false;
                                } else {
                                    boolList[i][j - 1][k] = -1;
                                }
                            }

                            if (k - 1 >= 0 && !(boolList[i][j][k - 1] == 1 || boolList[i][j][k - 1] == -1)) {
                                Block block = world.getBlock(x - expLim + i, y - expLim + j, z - expLim + k - 1);
                                if (world.isAirBlock(x - expLim + i, y - expLim + j, z - expLim + k - 1)
                                        || block == ModBlocks.blockSpectralContainer) {
                                    if (k - 1 == 0) {
                                        return emptyParam;
                                    }
                                    boolList[i][j][k - 1] = 1;
                                    isReady = false;
                                } else {
                                    boolList[i][j][k - 1] = -1;
                                }
                            }

                            if (i + 1 <= 2 * expLim && !(boolList[i + 1][j][k] == 1 || boolList[i + 1][j][k] == -1)) {
                                Block block = world.getBlock(x - expLim + i + 1, y - expLim + j, z - expLim + k);
                                if (world.isAirBlock(x - expLim + i + 1, y - expLim + j, z - expLim + k)
                                        || block == ModBlocks.blockSpectralContainer) {
                                    if (i + 1 == expLim * 2) {
                                        return emptyParam;
                                    }
                                    boolList[i + 1][j][k] = 1;
                                    isReady = false;
                                } else {
                                    boolList[i + 1][j][k] = -1;
                                }
                            }

                            if (j + 1 <= 2 * expLim && !(boolList[i][j + 1][k] == 1 || boolList[i][j + 1][k] == -1)) {
                                Block block = world.getBlock(x - expLim + i, y - expLim + j + 1, z - expLim + k);
                                if (world.isAirBlock(x - expLim + i, y - expLim + j + 1, z - expLim + k)
                                        || block == ModBlocks.blockSpectralContainer) {
                                    if (j + 1 == expLim * 2) {
                                        return emptyParam;
                                    }
                                    boolList[i][j + 1][k] = 1;
                                    isReady = false;
                                } else {
                                    boolList[i][j + 1][k] = -1;
                                }
                            }

                            if (k + 1 <= 2 * expLim && !(boolList[i][j][k + 1] == 1 || boolList[i][j][k + 1] == -1)) {
                                Block block = world.getBlock(x - expLim + i, y - expLim + j, z - expLim + k + 1);
                                if (world.isAirBlock(x - expLim + i, y - expLim + j, z - expLim + k + 1)
                                        || block == ModBlocks.blockSpectralContainer) {
                                    if (k + 1 == expLim * 2) {
                                        return emptyParam;
                                    }
                                    boolList[i][j][k + 1] = 1;
                                    isReady = false;
                                } else {
                                    boolList[i][j][k + 1] = -1;
                                }
                            }
                        }
                    }
                }
            }
        }

        int tally = 0;
        int enchantability = 0;
        int enchantmentLevel = 0;

        for (int i = 0; i < 2 * expLim + 1; i++) {
            for (int j = 0; j < 2 * expLim + 1; j++) {
                for (int k = 0; k < 2 * expLim + 1; k++) {
                    if (boolList[i][j][k] != -1) {
                        continue;
                    }

                    int indTally = 0;

                    if (i - 1 >= 0 && boolList[i - 1][j][k] == 1) {
                        indTally++;
                    }

                    if (j - 1 >= 0 && boolList[i][j - 1][k] == 1) {
                        indTally++;
                    }

                    if (k - 1 >= 0 && boolList[i][j][k - 1] == 1) {
                        indTally++;
                    }

                    if (i + 1 <= 2 * expLim && boolList[i + 1][j][k] == 1) {
                        indTally++;
                    }

                    if (j + 1 <= 2 * expLim && boolList[i][j + 1][k] == 1) {
                        indTally++;
                    }

                    if (k + 1 <= 2 * expLim && boolList[i][j][k + 1] == 1) {
                        indTally++;
                    }

                    Block block = world.getBlock(x - expLim + i, y - expLim + j, z - expLim + k);
                    int meta = world.getBlockMetadata(x - expLim + i, y - expLim + j, z - expLim + k);

                    if (block instanceof IEnchantmentGlyph glyph) {
                        tally += glyph.getAdditionalStabilityForFaceCount(
                                world,
                                x - expLim + i,
                                y - expLim + j,
                                z - expLim + k,
                                meta,
                                indTally);
                        enchantability += glyph
                                .getEnchantability(world, x - expLim + i, y - expLim + j, z - expLim + k, meta);
                        enchantmentLevel += glyph
                                .getEnchantmentLevel(world, x - expLim + i, y - expLim + j, z - expLim + k, meta);
                    } else if (block instanceof IStabilityGlyph) {
                        tally += ((IStabilityGlyph) block).getAdditionalStabilityForFaceCount(
                                world,
                                x - expLim + i,
                                y - expLim + j,
                                z - expLim + k,
                                meta,
                                indTally);
                    } else {
                        tally += indTally;
                    }
                }
            }
        }

        return new OmegaStructureParameters(tally, enchantability, enchantmentLevel);
    }

    public static OmegaStructureParameters getStructureStabilityFactor(World world, int x, int y, int z, int expLim) {
        return getStructureStabilityFactor(world, x, y, z, expLim, new Int3(0, 0, 0));
    }
}
