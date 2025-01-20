package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import cpw.mods.fml.common.registry.GameRegistry;

public class MeteorComponent {

    private final int weight;
    private final ItemStack itemStack;
    private final ArrayList<Reagent> reagent;
    public static MeteorComponent defaultMeteorBlock;

    public static void setDefaultMeteorBlock() {
        String string = AlchemicalWizardry.defaultMeteorBlock;
        String[] split = string.split(":");
        ItemStack stack = null;
        if (!string.isEmpty() && split.length >= 3) {
            stack = GameRegistry.findItemStack(split[0], split[1], 1);
        }
        if (stack != null) {
            Items.feather.setDamage(stack, Integer.parseInt(split[2]));
        } else {
            stack = new ItemStack(Blocks.stone);
        }
        defaultMeteorBlock = new MeteorComponent(stack, 1);
    }

    public MeteorComponent(ItemStack stack, int weight) {
        this(stack, weight, new ArrayList<>());
    }

    public MeteorComponent(ItemStack stack, int weight, ArrayList<Reagent> reagent) {
        this.itemStack = stack;
        this.weight = weight;
        this.reagent = reagent;
    }

    public static List<MeteorComponent> parseStringArray(String[] blockArray) {
        List<MeteorComponent> addList = new ArrayList<>();
        if (blockArray == null) {
            return addList;
        }
        for (String blockName : blockArray) {
            MeteorComponent component = parseString(blockName);
            if (component != null) {
                addList.add(component);
            }
        }
        return addList;
    }

    // modId:itemName:meta:weight(:reagent1, reagent2, ... optional)
    private static final Pattern itemNamePattern = Pattern.compile("(.*):(.*):(\\d+):(\\d+)(:.*)?");
    // OREDICT:oreDictName:weight(:reagent1, reagent2, ... optional)
    private static final Pattern oredictPattern = Pattern.compile("OREDICT:(.*):(\\d+)(:.*)?");

    public static MeteorComponent parseString(String blockName) {
        Matcher matcher = itemNamePattern.matcher(blockName);
        if (matcher.matches()) {
            String modID = matcher.group(1);
            String itemName = matcher.group(2);
            int meta = Integer.parseInt(matcher.group(3));
            int weight = Integer.parseInt(matcher.group(4));
            String reagent = matcher.group(5);

            ArrayList<Reagent> reagentList = MeteorReagent.parseReagents(reagent, blockName);

            ItemStack stack = GameRegistry.findItemStack(modID, itemName, 1);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                stack.setItemDamage(meta);
                return new MeteorComponent(stack, weight, reagentList);
            }

        } else if ((matcher = oredictPattern.matcher(blockName)).matches()) {
            String oreDict = matcher.group(1);
            int weight = Integer.parseInt(matcher.group(2));
            String reagent = matcher.group(3);

            ArrayList<Reagent> reagentList = MeteorReagent.parseReagents(reagent, blockName);

            List<ItemStack> list = OreDictionary.getOres(oreDict);
            for (ItemStack stack : list) {
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    return new MeteorComponent(stack, weight, reagentList);
                }
            }

        }
        AlchemicalWizardry.logger.warn("Unable to add Meteor Component \"{}\"", blockName);
        AlchemicalWizardry.logger.warn(
                "Valid formats are \"modId:itemName:meta:weight(:reagent1, reagent2, ... optional)\" and \"OREDICT:oreDictName:weight(:reagent1, reagent2, ... optional)\".");
        return null;
    }

    public int getWeight() {
        return this.weight;
    }

    public static int getTotalListWeight(List<MeteorComponent> blockList) {
        int totalWeight = 0;
        for (MeteorComponent component : blockList) {
            totalWeight += component.getWeight();
        }
        return totalWeight;
    }

    public ItemStack getBlock() {
        return itemStack;
    }

    public ArrayList<Reagent> getRequiredReagents() {
        return reagent;
    }

    public boolean checkForReagent(List<Reagent> reagentList) {
        if (reagent.isEmpty()) {
            return true;
        }
        for (Reagent r1 : reagentList) {
            for (Reagent r2 : reagent) {
                if (r1.equals(r2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
