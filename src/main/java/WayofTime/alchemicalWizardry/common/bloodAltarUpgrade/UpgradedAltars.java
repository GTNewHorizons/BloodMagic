package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.BlockStack;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class UpgradedAltars
{
    public static List<AltarComponent> secondTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> thirdTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> fourthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> fifthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> sixthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> seventhTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> eighthTierAltar = new ArrayList<AltarComponent>();
    public static List<AltarComponent> ninthTierAltar = new ArrayList<AltarComponent>();
    public static int highestAltar = 9;

    public static int isAltarValid(World world, int x, int y, int z)
    {
        for (int i = highestAltar; i >= 2; i--)
        {
            if (checkAltarIsValid(world, x, y, z, i))
            {
                return i;
            }
        }

        return 1;
    }

    public static boolean checkAltarIsValid(World world, int x, int y, int z, int altarTier)
    {
        if (altarTier == 1)
        {
            return true;
        }
        List<AltarComponent> altarComponents = getAltarUpgradeListForTier(altarTier);
        if (altarComponents == null)
        {
            return false;
        }
        for (AltarComponent ac : altarComponents)
        {
            if (!checkAltarComponent(ac, world, x, y, z, altarTier))
            {
                return false;
            }
        }
        return true;
    }

    private static BlockStack[] getRuneOverrides(int altarTier)
    {
        switch (altarTier)
        {
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
            case 7:
                return AlchemicalWizardry.seventhTierRunes;
            case 8:
                return AlchemicalWizardry.eighthTierRunes;
            case 9:
                return AlchemicalWizardry.ninthTierRunes;
            default:
                return null;
        }
    }
    private static boolean checkAltarComponent(AltarComponent altarComponent, IBlockAccess world, int x, int y, int z, int altarTier)//ugh
    {
        Block block = world.getBlock(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
        int metadata = world.getBlockMetadata(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
        if (altarComponent.isBloodRune())
        {
            boolean result = false;
            BlockStack[] runes = getRuneOverrides(altarTier);
            if (runes == null)
            {
                return false;
            }
            for (BlockStack rune : runes)
            {
                result |= altarComponent.isUpgradeSlot() ?
                          block == rune.getBlock() && metadata == rune.getMeta() :
                          block == altarComponent.getBlock() && metadata == altarComponent.getMetadata();
            }
            return result;
        }
        else
        {
            if (altarComponent.getBlock() != block || altarComponent.getMetadata() != metadata)
            {
                return false;
            }
        }
        return true;
    }

    public static AltarUpgradeComponent getUpgrades(World world, int x, int y, int z, int altarTier)
    {
    	if(world.isRemote)
    	{
    		return null;
    	}
        AltarUpgradeComponent upgrades = new AltarUpgradeComponent();
        List<AltarComponent> list = UpgradedAltars.getAltarUpgradeListForTier(altarTier);
        BlockStack[] runes = getRuneOverrides(altarTier);
        if (list == null || runes == null)
        {
            return upgrades;
        }
        for (AltarComponent altarComponent : list)
        {
            if (altarComponent.isUpgradeSlot())
            {
                Block block = world.getBlock(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
                int metadata = world.getBlockMetadata(x + altarComponent.getX(), y + altarComponent.getY(), z + altarComponent.getZ());
                BlockStack blockStack = new BlockStack(block, metadata);
                switch (Arrays.asList(runes).indexOf(blockStack))
                {
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
                        upgrades.addSuperSpeedUpgrade();
                        break;
                        
                    case 11:
                        upgrades.addUltraSpeedUpgrade();
                        break;
                        
                    case 12:
                        upgrades.addSuperSacrificeUpgrade();
                        break;
                        
                    case 13:
                        upgrades.addUltraSacrificeUpgrade();
                        break;
                        
                    case 14:
                        upgrades.addSuperDisplacementUpgrade();
                        break;
                        
                    case 15:
                        upgrades.addUltraDisplacementUpgrade();
                        break;
                }
            }
        }
        return upgrades;
    }

    public static void loadAltars()
    {
        secondTierAltar.add(new AltarComponent(-1, -1, -1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, false));
        secondTierAltar.add(new AltarComponent(0, -1, -1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        secondTierAltar.add(new AltarComponent(1, -1, -1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, false));
        secondTierAltar.add(new AltarComponent(-1, -1, 0, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 0, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        secondTierAltar.add(new AltarComponent(-1, -1, 1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, false));
        secondTierAltar.add(new AltarComponent(0, -1, 1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        secondTierAltar.add(new AltarComponent(1, -1, 1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, false));

        thirdTierAltar.add(new AltarComponent(-1, -1, -1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, -1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, -1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 0, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 0, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(-1, -1, 1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(0, -1, 1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(1, -1, 1, AlchemicalWizardry.secondTierRunes[0].getBlock(), AlchemicalWizardry.secondTierRunes[0].getMeta(), true, true));
        thirdTierAltar.add(new AltarComponent(-3, -1, -3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, -3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, -3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, -3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(-3, -1, 3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(-3, 0, 3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(3, -1, 3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(3, 0, 3, AlchemicalWizardry.specialAltarBlock[0].getBlock(), AlchemicalWizardry.specialAltarBlock[0].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, -3, AlchemicalWizardry.specialAltarBlock[1].getBlock(), AlchemicalWizardry.specialAltarBlock[1].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, -3, AlchemicalWizardry.specialAltarBlock[1].getBlock(), AlchemicalWizardry.specialAltarBlock[1].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(-3, 1, 3, AlchemicalWizardry.specialAltarBlock[1].getBlock(), AlchemicalWizardry.specialAltarBlock[1].getMeta(), false, false));
        thirdTierAltar.add(new AltarComponent(3, 1, 3, AlchemicalWizardry.specialAltarBlock[1].getBlock(), AlchemicalWizardry.specialAltarBlock[1].getMeta(), false, false));

        for (int i = -2; i <= 2; i++)
        {
            thirdTierAltar.add(new AltarComponent(3, -2, i, AlchemicalWizardry.thirdTierRunes[0].getBlock(), AlchemicalWizardry.thirdTierRunes[0].getMeta(), true, true));
            thirdTierAltar.add(new AltarComponent(-3, -2, i, AlchemicalWizardry.thirdTierRunes[0].getBlock(), AlchemicalWizardry.thirdTierRunes[0].getMeta(), true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, 3, AlchemicalWizardry.thirdTierRunes[0].getBlock(), AlchemicalWizardry.thirdTierRunes[0].getMeta(), true, true));
            thirdTierAltar.add(new AltarComponent(i, -2, -3, AlchemicalWizardry.thirdTierRunes[0].getBlock(), AlchemicalWizardry.thirdTierRunes[0].getMeta(), true, true));
        }

        fourthTierAltar.addAll(thirdTierAltar);

        for (int i = -3; i <= 3; i++)
        {
            fourthTierAltar.add(new AltarComponent(5, -3, i, AlchemicalWizardry.fourthTierRunes[0].getBlock(), AlchemicalWizardry.fourthTierRunes[0].getMeta(), true, true));
            fourthTierAltar.add(new AltarComponent(-5, -3, i, AlchemicalWizardry.fourthTierRunes[0].getBlock(), AlchemicalWizardry.fourthTierRunes[0].getMeta(), true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, 5, AlchemicalWizardry.fourthTierRunes[0].getBlock(), AlchemicalWizardry.fourthTierRunes[0].getMeta(), true, true));
            fourthTierAltar.add(new AltarComponent(i, -3, -5, AlchemicalWizardry.fourthTierRunes[0].getBlock(), AlchemicalWizardry.fourthTierRunes[0].getMeta(), true, true));
        }
        for (int i = -2; i <= 1; i++)
        {
            fourthTierAltar.add(new AltarComponent(5, i, 5, AlchemicalWizardry.specialAltarBlock[2].getBlock(), AlchemicalWizardry.specialAltarBlock[2].getMeta(), false, false));
            fourthTierAltar.add(new AltarComponent(5, i, -5, AlchemicalWizardry.specialAltarBlock[2].getBlock(), AlchemicalWizardry.specialAltarBlock[2].getMeta(), false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, -5, AlchemicalWizardry.specialAltarBlock[2].getBlock(), AlchemicalWizardry.specialAltarBlock[2].getMeta(), false, false));
            fourthTierAltar.add(new AltarComponent(-5, i, 5, AlchemicalWizardry.specialAltarBlock[2].getBlock(), AlchemicalWizardry.specialAltarBlock[2].getMeta(), false, false));
        }
        fourthTierAltar.add(new AltarComponent(5, 2, 5, AlchemicalWizardry.specialAltarBlock[3].getBlock(), AlchemicalWizardry.specialAltarBlock[3].getMeta(), false, false));
        fourthTierAltar.add(new AltarComponent(5, 2, -5, AlchemicalWizardry.specialAltarBlock[3].getBlock(), AlchemicalWizardry.specialAltarBlock[3].getMeta(), false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, -5, AlchemicalWizardry.specialAltarBlock[3].getBlock(), AlchemicalWizardry.specialAltarBlock[3].getMeta(), false, false));
        fourthTierAltar.add(new AltarComponent(-5, 2, 5, AlchemicalWizardry.specialAltarBlock[3].getBlock(), AlchemicalWizardry.specialAltarBlock[3].getMeta(), false, false));

        fifthTierAltar.addAll(fourthTierAltar);
        fifthTierAltar.add(new AltarComponent(-8, -3, 8, AlchemicalWizardry.specialAltarBlock[4].getBlock(), AlchemicalWizardry.specialAltarBlock[4].getMeta(), false, false));
        fifthTierAltar.add(new AltarComponent(-8, -3, -8, AlchemicalWizardry.specialAltarBlock[4].getBlock(), AlchemicalWizardry.specialAltarBlock[4].getMeta(), false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, -8, AlchemicalWizardry.specialAltarBlock[4].getBlock(), AlchemicalWizardry.specialAltarBlock[4].getMeta(), false, false));
        fifthTierAltar.add(new AltarComponent(8, -3, 8, AlchemicalWizardry.specialAltarBlock[4].getBlock(), AlchemicalWizardry.specialAltarBlock[4].getMeta(), false, false));
        for (int i = -6; i <= 6; i++)
        {
            fifthTierAltar.add(new AltarComponent(8, -4, i, AlchemicalWizardry.fifthTierRunes[0].getBlock(), AlchemicalWizardry.fifthTierRunes[0].getMeta(), true, true));
            fifthTierAltar.add(new AltarComponent(-8, -4, i, AlchemicalWizardry.fifthTierRunes[0].getBlock(), AlchemicalWizardry.fifthTierRunes[0].getMeta(), true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, 8, AlchemicalWizardry.fifthTierRunes[0].getBlock(), AlchemicalWizardry.fifthTierRunes[0].getMeta(), true, true));
            fifthTierAltar.add(new AltarComponent(i, -4, -8, AlchemicalWizardry.fifthTierRunes[0].getBlock(), AlchemicalWizardry.fifthTierRunes[0].getMeta(), true, true));
        }

        sixthTierAltar.addAll(fifthTierAltar);
        for (int i = -4; i <= 2; i++)
        {
        	sixthTierAltar.add(new AltarComponent(11, i, 11, AlchemicalWizardry.specialAltarBlock[5].getBlock(), AlchemicalWizardry.specialAltarBlock[5].getMeta(), false, false));
        	sixthTierAltar.add(new AltarComponent(-11, i, -11, AlchemicalWizardry.specialAltarBlock[5].getBlock(), AlchemicalWizardry.specialAltarBlock[5].getMeta(), false, false));
        	sixthTierAltar.add(new AltarComponent(11, i, -11, AlchemicalWizardry.specialAltarBlock[5].getBlock(), AlchemicalWizardry.specialAltarBlock[5].getMeta(), false, false));
        	sixthTierAltar.add(new AltarComponent(-11, i, 11, AlchemicalWizardry.specialAltarBlock[5].getBlock(), AlchemicalWizardry.specialAltarBlock[5].getMeta(), false, false));
        }
        sixthTierAltar.add(new AltarComponent(11, 3, 11, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
    	sixthTierAltar.add(new AltarComponent(-11, 3, -11, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
    	sixthTierAltar.add(new AltarComponent(11, 3, -11, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
    	sixthTierAltar.add(new AltarComponent(-11, 3, 11, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        for (int i = -9; i <= 9; i++)
        {
            sixthTierAltar.add(new AltarComponent(11, -5, i, AlchemicalWizardry.sixthTierRunes[0].getBlock(), AlchemicalWizardry.sixthTierRunes[0].getMeta(), true, true));
            sixthTierAltar.add(new AltarComponent(-11, -5, i, AlchemicalWizardry.sixthTierRunes[0].getBlock(), AlchemicalWizardry.sixthTierRunes[0].getMeta(), true, true));
            sixthTierAltar.add(new AltarComponent(i, -5, 11, AlchemicalWizardry.sixthTierRunes[0].getBlock(), AlchemicalWizardry.sixthTierRunes[0].getMeta(), true, true));
            sixthTierAltar.add(new AltarComponent(i, -5, -11, AlchemicalWizardry.sixthTierRunes[0].getBlock(), AlchemicalWizardry.sixthTierRunes[0].getMeta(), true, true));
        }
        
        //new
        seventhTierAltar.addAll(sixthTierAltar);
        for (int i = -5; i <= 4; i++)
        {
        	seventhTierAltar.add(new AltarComponent(15, i, 15, AlchemicalWizardry.specialAltarBlock[7].getBlock(), AlchemicalWizardry.specialAltarBlock[7].getMeta(), false, false));
        	seventhTierAltar.add(new AltarComponent(-15, i, -15, AlchemicalWizardry.specialAltarBlock[7].getBlock(), AlchemicalWizardry.specialAltarBlock[7].getMeta(), false, false));
        	seventhTierAltar.add(new AltarComponent(15, i, -15, AlchemicalWizardry.specialAltarBlock[7].getBlock(), AlchemicalWizardry.specialAltarBlock[7].getMeta(), false, false));
        	seventhTierAltar.add(new AltarComponent(-15, i, 15, AlchemicalWizardry.specialAltarBlock[7].getBlock(), AlchemicalWizardry.specialAltarBlock[7].getMeta(), false, false));
        }
        seventhTierAltar.add(new AltarComponent(15, 5, 15, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, 5, -15, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, 5, -15, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, 5, 15, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        //bases for 7th
        seventhTierAltar.add(new AltarComponent(-16, -6, -14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-16, -6, -15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-16, -6, -16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, -6, -14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, -6, -15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, -6, -16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-14, -6, -14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-14, -6, -15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-14, -6, -16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        
        seventhTierAltar.add(new AltarComponent(16, -6, -14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(16, -6, -15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(16, -6, -16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, -6, -14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, -6, -15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, -6, -16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(14, -6, -14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(14, -6, -15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(14, -6, -16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        
        seventhTierAltar.add(new AltarComponent(-16, -6, 14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-16, -6, 15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-16, -6, 16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, -6, 14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, -6, 15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-15, -6, 16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-14, -6, 14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-14, -6, 15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(-14, -6, 16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        
        seventhTierAltar.add(new AltarComponent(16, -6, 14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(16, -6, 15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(16, -6, 16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, -6, 14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, -6, 15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(15, -6, 16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(14, -6, 14, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(14, -6, 15, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        seventhTierAltar.add(new AltarComponent(14, -6, 16, AlchemicalWizardry.specialAltarBlock[6].getBlock(), AlchemicalWizardry.specialAltarBlock[6].getMeta(), false, false));
        for (int i = -12; i <= 12; i++)
        {
        	seventhTierAltar.add(new AltarComponent(15, -6, i, AlchemicalWizardry.seventhTierRunes[0].getBlock(), AlchemicalWizardry.seventhTierRunes[0].getMeta(), true, true));
        	seventhTierAltar.add(new AltarComponent(-15, -6, i, AlchemicalWizardry.seventhTierRunes[0].getBlock(), AlchemicalWizardry.seventhTierRunes[0].getMeta(), true, true));
        	seventhTierAltar.add(new AltarComponent(i, -6, 15, AlchemicalWizardry.seventhTierRunes[0].getBlock(), AlchemicalWizardry.seventhTierRunes[0].getMeta(), true, true));
        	seventhTierAltar.add(new AltarComponent(i, -6, -15, AlchemicalWizardry.seventhTierRunes[0].getBlock(), AlchemicalWizardry.seventhTierRunes[0].getMeta(), true, true));
        }
        
        eighthTierAltar.addAll(seventhTierAltar);
        for (int i = -6; i <= 6; i++)
        {
        	eighthTierAltar.add(new AltarComponent(19, i, 19, AlchemicalWizardry.specialAltarBlock[9].getBlock(), AlchemicalWizardry.specialAltarBlock[9].getMeta(), false, false));
        	eighthTierAltar.add(new AltarComponent(-19, i, -19, AlchemicalWizardry.specialAltarBlock[9].getBlock(), AlchemicalWizardry.specialAltarBlock[9].getMeta(), false, false));
        	eighthTierAltar.add(new AltarComponent(19, i, -19, AlchemicalWizardry.specialAltarBlock[9].getBlock(), AlchemicalWizardry.specialAltarBlock[9].getMeta(), false, false));
        	eighthTierAltar.add(new AltarComponent(-19, i, 19, AlchemicalWizardry.specialAltarBlock[9].getBlock(), AlchemicalWizardry.specialAltarBlock[9].getMeta(), false, false));
        }
        eighthTierAltar.add(new AltarComponent(19, 7, 19, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, 7, -19, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, 7, -19, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, 7, 19, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        //bases for 8th
        eighthTierAltar.add(new AltarComponent(-18, -7, -18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-18, -7, -19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-18, -7, -20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, -7, -18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, -7, -19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, -7, -20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-20, -7, -18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-20, -7, -19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-20, -7, -20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        
        eighthTierAltar.add(new AltarComponent(18, -7, -18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(18, -7, -19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(18, -7, -20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, -7, -18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, -7, -19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, -7, -20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(20, -7, -18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(20, -7, -19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(20, -7, -20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        
        eighthTierAltar.add(new AltarComponent(-18, -7, 18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-18, -7, 19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-18, -7, 20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, -7, 18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, -7, 19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-19, -7, 20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-20, -7, 18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-20, -7, 19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(-20, -7, 20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        
        eighthTierAltar.add(new AltarComponent(18, -7, 18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(18, -7, 19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(18, -7, 20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, -7, 18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, -7, 19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(19, -7, 20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(20, -7, 18, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(20, -7, 19, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        eighthTierAltar.add(new AltarComponent(20, -7, 20, AlchemicalWizardry.specialAltarBlock[8].getBlock(), AlchemicalWizardry.specialAltarBlock[8].getMeta(), false, false));
        
        for (int i = -16; i <= 16; i++)
        {
        	eighthTierAltar.add(new AltarComponent(19, -7, i, AlchemicalWizardry.eighthTierRunes[0].getBlock(), AlchemicalWizardry.eighthTierRunes[0].getMeta(), true, true));
        	eighthTierAltar.add(new AltarComponent(-19, -7, i, AlchemicalWizardry.eighthTierRunes[0].getBlock(), AlchemicalWizardry.eighthTierRunes[0].getMeta(), true, true));
        	eighthTierAltar.add(new AltarComponent(i, -7, 19, AlchemicalWizardry.eighthTierRunes[0].getBlock(), AlchemicalWizardry.eighthTierRunes[0].getMeta(), true, true));
        	eighthTierAltar.add(new AltarComponent(i, -7, -19, AlchemicalWizardry.eighthTierRunes[0].getBlock(), AlchemicalWizardry.eighthTierRunes[0].getMeta(), true, true));
        }
        
        ninthTierAltar.addAll(eighthTierAltar);
        for (int i = -6; i <= 9; i++)
        {
        	ninthTierAltar.add(new AltarComponent(24, i, 24, AlchemicalWizardry.specialAltarBlock[11].getBlock(), AlchemicalWizardry.specialAltarBlock[11].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(-24, i, -24, AlchemicalWizardry.specialAltarBlock[11].getBlock(), AlchemicalWizardry.specialAltarBlock[11].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(24, i, -24, AlchemicalWizardry.specialAltarBlock[11].getBlock(), AlchemicalWizardry.specialAltarBlock[11].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(-24, i, 24, AlchemicalWizardry.specialAltarBlock[11].getBlock(), AlchemicalWizardry.specialAltarBlock[11].getMeta(), false, false));
        }
        ninthTierAltar.add(new AltarComponent(24, 10, 24, AlchemicalWizardry.specialAltarBlock[12].getBlock(), AlchemicalWizardry.specialAltarBlock[12].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(-24, 10, -24, AlchemicalWizardry.specialAltarBlock[12].getBlock(), AlchemicalWizardry.specialAltarBlock[12].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(24, 10, -24, AlchemicalWizardry.specialAltarBlock[12].getBlock(), AlchemicalWizardry.specialAltarBlock[12].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(-24, 10, 24, AlchemicalWizardry.specialAltarBlock[12].getBlock(), AlchemicalWizardry.specialAltarBlock[12].getMeta(), false, false));
        //bases for 9th
        ninthTierAltar.add(new AltarComponent(23, -7, 24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(25, -7, 24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(24, -7, 24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(24, -7, 23, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        ninthTierAltar.add(new AltarComponent(24, -7, 25, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        for (int i = 22; i <= 26; i++) {
        	ninthTierAltar.add(new AltarComponent(i, -8, 23, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, 24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, 25, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -23, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -25, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        }
        for (int i = 23; i <= 25; i++) {
        	ninthTierAltar.add(new AltarComponent(i, -8, 22, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, 26, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -22, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -26, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        }
        for (int i = -26; i <= -22; i++) {
        	ninthTierAltar.add(new AltarComponent(i, -8, 23, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, 24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, 25, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -23, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -24, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -25, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        }
        for (int i = -25; i <= -23; i++) {
        	ninthTierAltar.add(new AltarComponent(i, -8, 22, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, 26, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -22, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        	ninthTierAltar.add(new AltarComponent(i, -8, -26, AlchemicalWizardry.specialAltarBlock[10].getBlock(), AlchemicalWizardry.specialAltarBlock[10].getMeta(), false, false));
        }
        
        for (int i = -20; i <= 20; i++)
        {
        	ninthTierAltar.add(new AltarComponent(24, -8, i, AlchemicalWizardry.ninthTierRunes[0].getBlock(), AlchemicalWizardry.ninthTierRunes[0].getMeta(), true, true));
        	ninthTierAltar.add(new AltarComponent(-24, -8, i, AlchemicalWizardry.ninthTierRunes[0].getBlock(), AlchemicalWizardry.ninthTierRunes[0].getMeta(), true, true));
        	ninthTierAltar.add(new AltarComponent(i, -8, 24, AlchemicalWizardry.ninthTierRunes[0].getBlock(), AlchemicalWizardry.ninthTierRunes[0].getMeta(), true, true));
        	ninthTierAltar.add(new AltarComponent(i, -8, -24, AlchemicalWizardry.ninthTierRunes[0].getBlock(), AlchemicalWizardry.ninthTierRunes[0].getMeta(), true, true));
        }      
    }

    public static List<AltarComponent> getAltarUpgradeListForTier(int tier)
    {
        switch (tier)
        {
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
            	
            case 7:
            	return seventhTierAltar;
            	
            case 8:
            	return eighthTierAltar;
            	
            case 9:
            	return ninthTierAltar;
        }

        return null;
    }
}
