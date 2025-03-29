package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.BlockStack;

public class UpgradedAltars {

    public static List<AltarComponent> secondTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> thirdTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> fourthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> fifthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> sixthTierAltar = new ArrayList<AltarComponent>();
    public static int highestAltar = 6;

    public static int isAltarValid(World world, int x, int y, int z) {
        for (int i = highestAltar; i >= 2; i--) {
            if (checkAltarIsValid(world, x, y, z, i)) {
                return i;
            }
        }

        return 1;
    }

    public static boolean checkAltarIsValid(World world, int x, int y, int z, int altarTier) {
        if (altarTier == 1) {
            return true;
        }
        List<AltarComponent> altarComponents = getAltarUpgradeListForTier(altarTier);
        if (altarComponents == null) {
            return false;
        }
        for (AltarComponent ac : altarComponents) {
            if (!checkAltarComponent(ac, world, x, y, z, altarTier)) {
                return false;
            }
        }
        return true;
    }

    private static BlockStack[] getRuneOverrides(int altarTier) {
        switch (altarTier) {
            case 2:
                return AlchemicalWizardry.secondTierRunes;
            case 3:
                return AlchemicalWizardry.thirdTierRunes;
            case 4:
                return AlchemicalWizardry.fourthTierRunes;
            case 5:
                return AlchemicalWizardry.fifthTierRunes;
            case 6:
                return AlchemicalWizardry.sixthTierRunes;
            default:
                return null;
        }
    }

    private static boolean checkAltarComponent(AltarComponent altarComponent, IBlockAccess world, int x, int y, int z,
            int altarTier) {
        int compX = x + altarComponent.getX();
        int compY = y + altarComponent.getY();
        int compZ = z + altarComponent.getZ();
        if (altarComponent.getBlock() == Blocks.air) {
            return !world.isAirBlock(compX, compY, compZ);
        }
        Block block = world.getBlock(compX, compY, compZ);
        int metadata = world.getBlockMetadata(compX, compY, compZ);
        if (altarComponent.isBloodRune()) {
            boolean result = false;
            BlockStack[] runes = getRuneOverrides(altarTier);
            if (runes == null) {
                return false;
            }
            for (BlockStack rune : runes) {
                result |= altarComponent.isUpgradeSlot() ? block == rune.getBlock() && metadata == rune.getMeta()
                        : block == altarComponent.getBlock() && metadata == altarComponent.getMetadata();
            }
            return result;
        } else {
            if (altarComponent.getBlock() != block || altarComponent.getMetadata() != metadata) {
                return false;
            }
        }
        return true;
    }

    public static AltarUpgradeComponent getUpgrades(World world, int x, int y, int z, int altarTier) {
        if (world.isRemote) {
            return null;
        }
        AltarUpgradeComponent upgrades = new AltarUpgradeComponent();
        List<AltarComponent> list = UpgradedAltars.getAltarUpgradeListForTier(altarTier);
        BlockStack[] runes = getRuneOverrides(altarTier);
        if (list == null || runes == null) {
            return upgrades;
        }
        for (AltarComponent altarComponent : list) {
            if (altarComponent.isUpgradeSlot()) {
                Block block = world
                        .getBlock(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
                int metadata = world.getBlockMetadata(
                        x + altarComponent.getX(),
                        y + altarComponent.getY(),
                        z + altarComponent.getZ());
                BlockStack blockStack = new BlockStack(block, metadata);
                switch (Arrays.asList(runes).indexOf(blockStack)) {
                    case 1:
                        upgrades.addSpeedUpgrade();
                        break;

                    case 2:
                        upgrades.addEfficiencyUpgrade();
                        break;

                    case 3:
                        upgrades.addSacrificeUpgrade();
                        break;

                    case 4:
                        upgrades.addSelfSacrificeUpgrade();
                        break;

                    case 5:
                        upgrades.addaltarCapacitiveUpgrade();
                        break;

                    case 6:
                        upgrades.addDisplacementUpgrade();
                        break;

                    case 7:
                        upgrades.addorbCapacitiveUpgrade();
                        break;

                    case 8:
                        upgrades.addBetterCapacitiveUpgrade();
                        break;

                    case 9:
                        upgrades.addAccelerationUpgrade();
                        break;

                    case 10:
                        upgrades.addQuicknessUpgrade();
                        break;
                }
            }
        }
        return upgrades;
    }

    public static void loadAltars() {
        secondTierAltar.add(
                new AltarComponent(
                        -1,
                        -1,
                        -1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        false));
        secondTierAltar.add(
                new AltarComponent(
                        0,
                        -1,
                        -1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        secondTierAltar.add(
                new AltarComponent(
                        1,
                        -1,
                        -1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        false));
        secondTierAltar.add(
                new AltarComponent(
                        -1,
                        -1,
                        0,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        secondTierAltar.add(
                new AltarComponent(
                        1,
                        -1,
                        0,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        secondTierAltar.add(
                new AltarComponent(
                        -1,
                        -1,
                        1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        false));
        secondTierAltar.add(
                new AltarComponent(
                        0,
                        -1,
                        1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        secondTierAltar.add(
                new AltarComponent(
                        1,
                        -1,
                        1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        false));

        thirdTierAltar.add(
                new AltarComponent(
                        -1,
                        -1,
                        -1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        0,
                        -1,
                        -1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        1,
                        -1,
                        -1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        -1,
                        -1,
                        0,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        1,
                        -1,
                        0,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        -1,
                        -1,
                        1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        0,
                        -1,
                        1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        1,
                        -1,
                        1,
                        AlchemicalWizardry.secondTierRunes[0].getBlock(),
                        AlchemicalWizardry.secondTierRunes[0].getMeta(),
                        true,
                        true));
        thirdTierAltar.add(
                new AltarComponent(
                        -3,
                        -1,
                        -3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        -3,
                        0,
                        -3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        3,
                        -1,
                        -3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        3,
                        0,
                        -3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        -3,
                        -1,
                        3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        -3,
                        0,
                        3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        3,
                        -1,
                        3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        3,
                        0,
                        3,
                        AlchemicalWizardry.specialAltarBlock[0].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[0].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        -3,
                        1,
                        -3,
                        AlchemicalWizardry.specialAltarBlock[1].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[1].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        3,
                        1,
                        -3,
                        AlchemicalWizardry.specialAltarBlock[1].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[1].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        -3,
                        1,
                        3,
                        AlchemicalWizardry.specialAltarBlock[1].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[1].getMeta(),
                        false,
                        false));
        thirdTierAltar.add(
                new AltarComponent(
                        3,
                        1,
                        3,
                        AlchemicalWizardry.specialAltarBlock[1].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[1].getMeta(),
                        false,
                        false));

        for (int i = -2; i <= 2; i++) {
            thirdTierAltar.add(
                    new AltarComponent(
                            3,
                            -2,
                            i,
                            AlchemicalWizardry.thirdTierRunes[0].getBlock(),
                            AlchemicalWizardry.thirdTierRunes[0].getMeta(),
                            true,
                            true));
            thirdTierAltar.add(
                    new AltarComponent(
                            -3,
                            -2,
                            i,
                            AlchemicalWizardry.thirdTierRunes[0].getBlock(),
                            AlchemicalWizardry.thirdTierRunes[0].getMeta(),
                            true,
                            true));
            thirdTierAltar.add(
                    new AltarComponent(
                            i,
                            -2,
                            3,
                            AlchemicalWizardry.thirdTierRunes[0].getBlock(),
                            AlchemicalWizardry.thirdTierRunes[0].getMeta(),
                            true,
                            true));
            thirdTierAltar.add(
                    new AltarComponent(
                            i,
                            -2,
                            -3,
                            AlchemicalWizardry.thirdTierRunes[0].getBlock(),
                            AlchemicalWizardry.thirdTierRunes[0].getMeta(),
                            true,
                            true));
        }

        fourthTierAltar.addAll(thirdTierAltar);

        for (int i = -3; i <= 3; i++) {
            fourthTierAltar.add(
                    new AltarComponent(
                            5,
                            -3,
                            i,
                            AlchemicalWizardry.fourthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fourthTierRunes[0].getMeta(),
                            true,
                            true));
            fourthTierAltar.add(
                    new AltarComponent(
                            -5,
                            -3,
                            i,
                            AlchemicalWizardry.fourthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fourthTierRunes[0].getMeta(),
                            true,
                            true));
            fourthTierAltar.add(
                    new AltarComponent(
                            i,
                            -3,
                            5,
                            AlchemicalWizardry.fourthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fourthTierRunes[0].getMeta(),
                            true,
                            true));
            fourthTierAltar.add(
                    new AltarComponent(
                            i,
                            -3,
                            -5,
                            AlchemicalWizardry.fourthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fourthTierRunes[0].getMeta(),
                            true,
                            true));
        }
        for (int i = -2; i <= 1; i++) {
            fourthTierAltar.add(
                    new AltarComponent(
                            5,
                            i,
                            5,
                            AlchemicalWizardry.specialAltarBlock[2].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[2].getMeta(),
                            false,
                            false));
            fourthTierAltar.add(
                    new AltarComponent(
                            5,
                            i,
                            -5,
                            AlchemicalWizardry.specialAltarBlock[2].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[2].getMeta(),
                            false,
                            false));
            fourthTierAltar.add(
                    new AltarComponent(
                            -5,
                            i,
                            -5,
                            AlchemicalWizardry.specialAltarBlock[2].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[2].getMeta(),
                            false,
                            false));
            fourthTierAltar.add(
                    new AltarComponent(
                            -5,
                            i,
                            5,
                            AlchemicalWizardry.specialAltarBlock[2].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[2].getMeta(),
                            false,
                            false));
        }
        fourthTierAltar.add(
                new AltarComponent(
                        5,
                        2,
                        5,
                        AlchemicalWizardry.specialAltarBlock[3].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[3].getMeta(),
                        false,
                        false));
        fourthTierAltar.add(
                new AltarComponent(
                        5,
                        2,
                        -5,
                        AlchemicalWizardry.specialAltarBlock[3].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[3].getMeta(),
                        false,
                        false));
        fourthTierAltar.add(
                new AltarComponent(
                        -5,
                        2,
                        -5,
                        AlchemicalWizardry.specialAltarBlock[3].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[3].getMeta(),
                        false,
                        false));
        fourthTierAltar.add(
                new AltarComponent(
                        -5,
                        2,
                        5,
                        AlchemicalWizardry.specialAltarBlock[3].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[3].getMeta(),
                        false,
                        false));

        fifthTierAltar.addAll(fourthTierAltar);
        fifthTierAltar.add(
                new AltarComponent(
                        -8,
                        -3,
                        8,
                        AlchemicalWizardry.specialAltarBlock[4].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[4].getMeta(),
                        false,
                        false));
        fifthTierAltar.add(
                new AltarComponent(
                        -8,
                        -3,
                        -8,
                        AlchemicalWizardry.specialAltarBlock[4].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[4].getMeta(),
                        false,
                        false));
        fifthTierAltar.add(
                new AltarComponent(
                        8,
                        -3,
                        -8,
                        AlchemicalWizardry.specialAltarBlock[4].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[4].getMeta(),
                        false,
                        false));
        fifthTierAltar.add(
                new AltarComponent(
                        8,
                        -3,
                        8,
                        AlchemicalWizardry.specialAltarBlock[4].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[4].getMeta(),
                        false,
                        false));
        for (int i = -6; i <= 6; i++) {
            fifthTierAltar.add(
                    new AltarComponent(
                            8,
                            -4,
                            i,
                            AlchemicalWizardry.fifthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fifthTierRunes[0].getMeta(),
                            true,
                            true));
            fifthTierAltar.add(
                    new AltarComponent(
                            -8,
                            -4,
                            i,
                            AlchemicalWizardry.fifthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fifthTierRunes[0].getMeta(),
                            true,
                            true));
            fifthTierAltar.add(
                    new AltarComponent(
                            i,
                            -4,
                            8,
                            AlchemicalWizardry.fifthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fifthTierRunes[0].getMeta(),
                            true,
                            true));
            fifthTierAltar.add(
                    new AltarComponent(
                            i,
                            -4,
                            -8,
                            AlchemicalWizardry.fifthTierRunes[0].getBlock(),
                            AlchemicalWizardry.fifthTierRunes[0].getMeta(),
                            true,
                            true));
        }

        sixthTierAltar.addAll(fifthTierAltar);
        for (int i = -4; i <= 2; i++) {
            sixthTierAltar.add(
                    new AltarComponent(
                            11,
                            i,
                            11,
                            AlchemicalWizardry.specialAltarBlock[5].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[5].getMeta(),
                            false,
                            false));
            sixthTierAltar.add(
                    new AltarComponent(
                            -11,
                            i,
                            -11,
                            AlchemicalWizardry.specialAltarBlock[5].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[5].getMeta(),
                            false,
                            false));
            sixthTierAltar.add(
                    new AltarComponent(
                            11,
                            i,
                            -11,
                            AlchemicalWizardry.specialAltarBlock[5].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[5].getMeta(),
                            false,
                            false));
            sixthTierAltar.add(
                    new AltarComponent(
                            -11,
                            i,
                            11,
                            AlchemicalWizardry.specialAltarBlock[5].getBlock(),
                            AlchemicalWizardry.specialAltarBlock[5].getMeta(),
                            false,
                            false));
        }
        sixthTierAltar.add(
                new AltarComponent(
                        11,
                        3,
                        11,
                        AlchemicalWizardry.specialAltarBlock[6].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[6].getMeta(),
                        false,
                        false));
        sixthTierAltar.add(
                new AltarComponent(
                        -11,
                        3,
                        -11,
                        AlchemicalWizardry.specialAltarBlock[6].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[6].getMeta(),
                        false,
                        false));
        sixthTierAltar.add(
                new AltarComponent(
                        11,
                        3,
                        -11,
                        AlchemicalWizardry.specialAltarBlock[6].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[6].getMeta(),
                        false,
                        false));
        sixthTierAltar.add(
                new AltarComponent(
                        -11,
                        3,
                        11,
                        AlchemicalWizardry.specialAltarBlock[6].getBlock(),
                        AlchemicalWizardry.specialAltarBlock[6].getMeta(),
                        false,
                        false));
        for (int i = -9; i <= 9; i++) {
            sixthTierAltar.add(
                    new AltarComponent(
                            11,
                            -5,
                            i,
                            AlchemicalWizardry.sixthTierRunes[0].getBlock(),
                            AlchemicalWizardry.sixthTierRunes[0].getMeta(),
                            true,
                            true));
            sixthTierAltar.add(
                    new AltarComponent(
                            -11,
                            -5,
                            i,
                            AlchemicalWizardry.sixthTierRunes[0].getBlock(),
                            AlchemicalWizardry.sixthTierRunes[0].getMeta(),
                            true,
                            true));
            sixthTierAltar.add(
                    new AltarComponent(
                            i,
                            -5,
                            11,
                            AlchemicalWizardry.sixthTierRunes[0].getBlock(),
                            AlchemicalWizardry.sixthTierRunes[0].getMeta(),
                            true,
                            true));
            sixthTierAltar.add(
                    new AltarComponent(
                            i,
                            -5,
                            -11,
                            AlchemicalWizardry.sixthTierRunes[0].getBlock(),
                            AlchemicalWizardry.sixthTierRunes[0].getMeta(),
                            true,
                            true));
        }
    }

    public static List<AltarComponent> getAltarUpgradeListForTier(int tier) {
        switch (tier) {
            case 2:
                return secondTierAltar;

            case 3:
                return thirdTierAltar;

            case 4:
                return fourthTierAltar;

            case 5:
                return fifthTierAltar;

            case 6:
                return sixthTierAltar;
        }

        return null;
    }
}
