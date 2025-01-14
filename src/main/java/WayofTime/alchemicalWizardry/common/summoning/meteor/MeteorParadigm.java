package WayofTime.alchemicalWizardry.common.summoning.meteor;

import static WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorReagentRegistry.getFillerList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.common.blocks.TileEntityOres;

public class MeteorParadigm {

    public List<MeteorParadigmComponent> componentList = new ArrayList<>();
    public List<MeteorParadigmComponent> fillerList = new ArrayList<>();
    public ItemStack focusStack;
    public int radius;
    public int cost;
    public int fillerChance; // Out of 100

    public static Random rand = new Random();

    public MeteorParadigm(ItemStack focusStack, int radius, int cost, int fillerChance) {
        this.focusStack = focusStack;
        this.radius = radius;
        this.cost = cost;
        this.fillerChance = fillerChance;
    }

    // modId:itemName:meta:weight(:reagent1, reagent2, ... optional)
    private static final Pattern itemNamePattern = Pattern.compile("(.*):(.*):(\\d+):(\\d+)(:.*)?");
    // OREDICT:oreDictName:weight(:reagent1, reagent2, ... optional)
    private static final Pattern oredictPattern = Pattern.compile("OREDICT:(.*):(\\d+)(:.*)?");

    public static List<MeteorParadigmComponent> parseStringArray(String[] blockArray) {
        List<MeteorParadigmComponent> addList = new ArrayList<>();
        for (int i = 0; i < blockArray.length; ++i) {
            String blockName = blockArray[i];
            boolean success = false;

            Matcher matcher = itemNamePattern.matcher(blockName);
            if (matcher.matches()) {
                String modID = matcher.group(1);
                String itemName = matcher.group(2);
                int meta = Integer.parseInt(matcher.group(3));
                int weight = Integer.parseInt(matcher.group(4));
                String reagent = matcher.group(5);

                ArrayList<Reagent> reagentList = getReagents(reagent, blockName);

                ItemStack stack = GameRegistry.findItemStack(modID, itemName, 1);
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    stack.setItemDamage(meta);
                    addList.add(new MeteorParadigmComponent(stack, weight, reagentList));
                    success = true;
                }

            } else if ((matcher = oredictPattern.matcher(blockName)).matches()) {
                String oreDict = matcher.group(1);
                int weight = Integer.parseInt(matcher.group(2));
                String reagent = matcher.group(3);

                ArrayList<Reagent> reagentList = getReagents(reagent, blockName);

                List<ItemStack> list = OreDictionary.getOres(oreDict);
                for (ItemStack stack : list) {
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        addList.add(new MeteorParadigmComponent(stack, weight, reagentList));
                        success = true;
                        break;
                    }
                }

            }

            if (!success) {
                AlchemicalWizardry.logger.warn("Unable to add Meteor Paradigm \"{}\"", blockName);
                AlchemicalWizardry.logger.warn(
                        "Valid formats are \"modId:itemName:meta:weight(:reagent1, reagent2, ... optional)\" and \"OREDICT:oreDictName:weight(:reagent1, reagent2, ... optional)\".");
            }
        }
        return addList;
    }

    private static ArrayList<Reagent> getReagents(String reagent, String blockName) {
        ArrayList<Reagent> reagentList = new ArrayList<>();
        if (reagent != null) {
            String[] reagents = reagent.substring(1).split(", ?");
            for (String str : reagents) {
                Reagent r = ReagentRegistry.getReagentForKey(str);
                if (r == null) {
                    AlchemicalWizardry.logger.warn("Unable to add reagent \"{}\" for {}.", str, blockName);
                    continue;
                }
                reagentList.add(r);
            }
        }
        return reagentList;
    }

    public int getTotalListWeight(List<MeteorParadigmComponent> blockList) {
        int totalWeight = 0;
        for (MeteorParadigmComponent mpc : blockList) {
            totalWeight += mpc.getWeight();
        }
        return totalWeight;
    }

    public void createMeteorImpact(World world, int x, int y, int z, List<Reagent> reagents) {
        int radius = getNewRadius(this.radius, reagents);
        int fillerChance = getNewFillerChance(this.fillerChance, reagents);

        if (MeteorReagentRegistry.doExplosions(reagents)) {
            world.createExplosion(null, x, y, z, radius * 4, MeteorReagentRegistry.doMeteorsDestroyBlocks(reagents));
        }

        List<MeteorParadigmComponent> componentList = removeBlocksMissingRequiredReagents(this.componentList, reagents);
        List<MeteorParadigmComponent> fillerList = getNewFillerList(this.fillerList, reagents);

        int totalComponentWeight = getTotalListWeight(componentList);
        int totalFillerWeight = getTotalListWeight(fillerList);

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f)) {
                        continue;
                    }

                    if (!world.isAirBlock(x + i, y + j, z + k)) {
                        continue;
                    }

                    if (fillerChance <= 0 || world.rand.nextInt(100) >= fillerChance) {
                        setMeteorBlock(x + i, y + j, z + k, world, componentList, totalComponentWeight);
                    } else {
                        setMeteorBlock(x + i, y + j, z + k, world, fillerList, totalFillerWeight);
                    }
                }
            }
        }
    }

    private int getNewRadius(int radius, List<Reagent> reagents) {
        radius += MeteorReagentRegistry.getLargestRadiusIncrease(reagents);
        radius += MeteorReagentRegistry.getLargestRadiusDecrease(reagents);
        return Math.max(radius, 1);
    }

    private int getNewFillerChance(int fillerChance, List<Reagent> reagents) {
        // Don't add filler to meteors with none
        if (fillerChance <= 0) {
            return 0;
        }

        int increase = MeteorReagentRegistry.getLargestFillerChanceIncrease(reagents);
        int decrease = MeteorReagentRegistry.getLargestFillerChanceDecrease(reagents);
        // Avoid division by zero
        if (decrease == -100) {
            return 0;
        }

        fillerChance += MeteorReagentRegistry.getLargestRawFillerChanceIncrease(reagents);
        fillerChance += MeteorReagentRegistry.getLargestRawFillerChanceDecrease(reagents);
        if (increase > 0) {
            fillerChance = (fillerChance + increase) * 100 / (100 + increase);
        } else if (decrease < 0) {
            fillerChance = (fillerChance + decrease) * 100 / (100 + decrease);
        }
        return Math.max(0, Math.min(fillerChance, 100));
    }

    private List<MeteorParadigmComponent> getNewFillerList(List<MeteorParadigmComponent> fillerList,
            List<Reagent> reagents) {
        List<MeteorParadigmComponent> reagentFillers = getFillerList(reagents);
        reagentFillers = removeBlocksMissingRequiredReagents(reagentFillers, reagents);
        if (!reagentFillers.isEmpty()) {
            return reagentFillers;
        }
        List<MeteorParadigmComponent> newFillers = removeBlocksMissingRequiredReagents(fillerList, reagents);
        if (newFillers.isEmpty()) { // Use the default if every filler requires a reagent
            newFillers.add(MeteorRegistry.getDefaultMeteorParadigmComponent());
        }
        return newFillers;
    }

    private List<MeteorParadigmComponent> removeBlocksMissingRequiredReagents(List<MeteorParadigmComponent> blockList,
            List<Reagent> reagents) {
        ArrayList<MeteorParadigmComponent> newList = new ArrayList<>();
        for (MeteorParadigmComponent mpc : blockList) {
            if (mpc.checkForReagent(reagents)) {
                newList.add(mpc);
            }
        }
        return newList;
    }

    private void setMeteorBlock(int x, int y, int z, World world, List<MeteorParadigmComponent> blockList,
            int totalListWeight) {
        int randNum = world.rand.nextInt(totalListWeight);
        for (MeteorParadigmComponent mpc : blockList) {
            randNum -= mpc.getWeight();

            if (randNum < 0) {
                ItemStack blockStack = mpc.getBlock();
                if (blockStack != null && blockStack.getItem() instanceof ItemBlock) {
                    ((ItemBlock) blockStack.getItem())
                            .placeBlockAt(blockStack, null, world, x, y, z, 0, 0, 0, 0, blockStack.getItemDamage());
                    if (AlchemicalWizardry.isGregTechLoaded) setGTOresNaturalIfNeeded(world, x, y, z);
                    world.markBlockForUpdate(x, y, z);
                    break;
                }
            }
        }
    }

    @Optional.Method(modid = "gregtech")
    private static void setGTOresNaturalIfNeeded(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityOres) {
            ((TileEntityOres) tileEntity).mNatural = true;
        }
    }
}
