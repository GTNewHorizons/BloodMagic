package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
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

    private static List<BlockStack> getRuneOverrides(int altarTier) {
        return switch (altarTier) {
            case 2 -> AlchemicalWizardry.secondTierRunes;
            case 3 -> AlchemicalWizardry.thirdTierRunes;
            case 4 -> AlchemicalWizardry.fourthTierRunes;
            case 5 -> AlchemicalWizardry.fifthTierRunes;
            case 6 -> AlchemicalWizardry.sixthTierRunes;
            default -> null;
        };
    }

    private static boolean checkAltarComponent(AltarComponent altarComponent, IBlockAccess world, int x, int y, int z,
            int altarTier) {
        int compX = x + altarComponent.getX();
        int compY = y + altarComponent.getY();
        int compZ = z + altarComponent.getZ();
        if (altarComponent.anyBlockMatches()) {
            return !world.isAirBlock(compX, compY, compZ);
        }

        Block block = world.getBlock(compX, compY, compZ);
        int meta = world.getBlockMetadata(compX, compY, compZ);
        if (!altarComponent.isBloodRune()) {
            return altarComponent.matches(block, meta);
        }

        List<BlockStack> runes = getRuneOverrides(altarTier);
        if (runes == null) {
            return false;
        }

        for (BlockStack rune : runes) {
            if (altarComponent.isUpgradeSlot() ? block == rune.getBlock() && meta == rune.getMeta()
                    : altarComponent.matches(block, meta)) {
                return true;
            }
        }

        return false;
    }

    public static AltarUpgradeComponent getUpgrades(World world, int x, int y, int z, int altarTier) {
        if (world.isRemote) {
            return null;
        }
        AltarUpgradeComponent upgrades = new AltarUpgradeComponent();
        List<AltarComponent> list = UpgradedAltars.getAltarUpgradeListForTier(altarTier);
        List<BlockStack> runes = getRuneOverrides(altarTier);
        if (list == null || runes == null) {
            return upgrades;
        }
        for (AltarComponent altarComponent : list) {
            if (!altarComponent.isUpgradeSlot()) {
                continue;
            }

            Block block = world
                    .getBlock(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
            int metadata = world
                    .getBlockMetadata(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
            BlockStack blockStack = new BlockStack(block, metadata);
            switch (runes.indexOf(blockStack)) {
                case 1 -> upgrades.addSpeedUpgrade();
                case 2 -> upgrades.addEfficiencyUpgrade();
                case 3 -> upgrades.addSacrificeUpgrade();
                case 4 -> upgrades.addSelfSacrificeUpgrade();
                case 5 -> upgrades.addaltarCapacitiveUpgrade();
                case 6 -> upgrades.addDisplacementUpgrade();
                case 7 -> upgrades.addorbCapacitiveUpgrade();
                case 8 -> upgrades.addBetterCapacitiveUpgrade();
                case 9 -> upgrades.addAccelerationUpgrade();
                case 10 -> upgrades.addQuicknessUpgrade();
            }
        }
        return upgrades;
    }

    public static void loadAltars() {
        secondTierAltar.add(new AltarComponent(-1, -1, -1, AlchemicalWizardry.secondTierRunes, true, false));
        secondTierAltar.add(new AltarComponent(0, -1, -1, AlchemicalWizardry.secondTierRunes, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, -1, AlchemicalWizardry.secondTierRunes, true, false));
        secondTierAltar.add(new AltarComponent(-1, -1, 0, AlchemicalWizardry.secondTierRunes, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 0, AlchemicalWizardry.secondTierRunes, true, true));
        secondTierAltar.add(new AltarComponent(-1, -1, 1, AlchemicalWizardry.secondTierRunes, true, false));
        secondTierAltar.add(new AltarComponent(0, -1, 1, AlchemicalWizardry.secondTierRunes, true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 1, AlchemicalWizardry.secondTierRunes, true, false));

        thirdTierAltar.add(new AltarComponent(-1, -1, -1, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, -1, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, -1, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 0, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 0, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 1, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, 1, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 1, AlchemicalWizardry.secondTierRunes, true, true));
        thirdTierAltar.add(new AltarComponent(-3, -1, -3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, -3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, -3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, -3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(-3, -1, 3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, 3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, 3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, 3, AlchemicalWizardry.thirdTierPillars, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, -3, AlchemicalWizardry.thirdTierCaps, false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, -3, AlchemicalWizardry.thirdTierCaps, false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, 3, AlchemicalWizardry.thirdTierCaps, false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, 3, AlchemicalWizardry.thirdTierCaps, false, false));

        for (int i = -2; i <= 2; i++) {
            thirdTierAltar.add(new AltarComponent(3, -2, i, AlchemicalWizardry.thirdTierRunes, true, true));
            thirdTierAltar.add(new AltarComponent(-3, -2, i, AlchemicalWizardry.thirdTierRunes, true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, 3, AlchemicalWizardry.thirdTierRunes, true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, -3, AlchemicalWizardry.thirdTierRunes, true, true));
        }

        fourthTierAltar.addAll(thirdTierAltar);

        for (int i = -3; i <= 3; i++) {
            fourthTierAltar.add(new AltarComponent(5, -3, i, AlchemicalWizardry.fourthTierRunes, true, true));
            fourthTierAltar.add(new AltarComponent(-5, -3, i, AlchemicalWizardry.fourthTierRunes, true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, 5, AlchemicalWizardry.fourthTierRunes, true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, -5, AlchemicalWizardry.fourthTierRunes, true, true));
        }
        for (int i = -2; i <= 1; i++) {
            fourthTierAltar.add(new AltarComponent(5, i, 5, AlchemicalWizardry.fourthTierPillars, false, false));
            fourthTierAltar.add(new AltarComponent(5, i, -5, AlchemicalWizardry.fourthTierPillars, false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, -5, AlchemicalWizardry.fourthTierPillars, false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, 5, AlchemicalWizardry.fourthTierPillars, false, false));
        }
        fourthTierAltar.add(new AltarComponent(5, 2, 5, AlchemicalWizardry.fourthTierCaps, false, false));
        fourthTierAltar.add(new AltarComponent(5, 2, -5, AlchemicalWizardry.fourthTierCaps, false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, -5, AlchemicalWizardry.fourthTierCaps, false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, 5, AlchemicalWizardry.fourthTierCaps, false, false));

        fifthTierAltar.addAll(fourthTierAltar);
        fifthTierAltar.add(new AltarComponent(-8, -3, 8, AlchemicalWizardry.fifthTierBeacons, false, false));
        fifthTierAltar.add(new AltarComponent(-8, -3, -8, AlchemicalWizardry.fifthTierBeacons, false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, -8, AlchemicalWizardry.fifthTierBeacons, false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, 8, AlchemicalWizardry.fifthTierBeacons, false, false));
        for (int i = -6; i <= 6; i++) {
            fifthTierAltar.add(new AltarComponent(8, -4, i, AlchemicalWizardry.fifthTierRunes, true, true));
            fifthTierAltar.add(new AltarComponent(-8, -4, i, AlchemicalWizardry.fifthTierRunes, true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, 8, AlchemicalWizardry.fifthTierRunes, true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, -8, AlchemicalWizardry.fifthTierRunes, true, true));
        }

        sixthTierAltar.addAll(fifthTierAltar);
        for (int i = -4; i <= 2; i++) {
            sixthTierAltar.add(new AltarComponent(11, i, 11, AlchemicalWizardry.sixthTierPillars, false, false));
            sixthTierAltar.add(new AltarComponent(-11, i, -11, AlchemicalWizardry.sixthTierPillars, false, false));
            sixthTierAltar.add(new AltarComponent(11, i, -11, AlchemicalWizardry.sixthTierPillars, false, false));
            sixthTierAltar.add(new AltarComponent(-11, i, 11, AlchemicalWizardry.sixthTierPillars, false, false));
        }
        sixthTierAltar.add(new AltarComponent(11, 3, 11, AlchemicalWizardry.sixthTierCaps, false, false));
        sixthTierAltar.add(new AltarComponent(-11, 3, -11, AlchemicalWizardry.sixthTierCaps, false, false));
        sixthTierAltar.add(new AltarComponent(11, 3, -11, AlchemicalWizardry.sixthTierCaps, false, false));
        sixthTierAltar.add(new AltarComponent(-11, 3, 11, AlchemicalWizardry.sixthTierCaps, false, false));
        for (int i = -9; i <= 9; i++) {
            sixthTierAltar.add(new AltarComponent(11, -5, i, AlchemicalWizardry.sixthTierRunes, true, true));
            sixthTierAltar.add(new AltarComponent(-11, -5, i, AlchemicalWizardry.sixthTierRunes, true, true));
            sixthTierAltar.add(new AltarComponent(i, -5, 11, AlchemicalWizardry.sixthTierRunes, true, true));
            sixthTierAltar.add(new AltarComponent(i, -5, -11, AlchemicalWizardry.sixthTierRunes, true, true));
        }
    }

    public static List<AltarComponent> getAltarUpgradeListForTier(int tier) {
        return switch (tier) {
            case 2 -> secondTierAltar;
            case 3 -> thirdTierAltar;
            case 4 -> fourthTierAltar;
            case 5 -> fifthTierAltar;
            case 6 -> sixthTierAltar;
            default -> null;
        };
    }
}
