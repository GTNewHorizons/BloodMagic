package WayofTime.alchemicalWizardry.api.alchemy.energy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.item.ItemStack;

public class ReagentRegistry {

    public static Map<String, Reagent> reagentList = new LinkedHashMap<>();
    public static Map<ItemStack, ReagentStack> itemToReagentMap = new HashMap<>();

    public static Reagent sanctusReagent;
    public static Reagent incendiumReagent;
    public static Reagent aquasalusReagent;
    public static Reagent magicalesReagent;
    public static Reagent aetherReagent;
    public static Reagent crepitousReagent;
    public static Reagent crystallosReagent;
    public static Reagent terraeReagent;
    public static Reagent tenebraeReagent;
    public static Reagent offensaReagent;
    public static Reagent praesidiumReagent;
    public static Reagent orbisTerraeReagent;
    public static Reagent virtusReagent;
    public static Reagent reductusReagent;
    public static Reagent potentiaReagent;

    public static void initReagents() {
        sanctusReagent = new Reagent("sanctus", 255, 255, 0, 255);
        incendiumReagent = new Reagent("incendium", 255, 0, 0, 255);
        aquasalusReagent = new Reagent("aquasalus", 0, 0, 255, 255);
        magicalesReagent = new Reagent("magicales", 150, 0, 146, 255);
        aetherReagent = new Reagent("aether", 105, 223, 86, 255);
        crepitousReagent = new Reagent("crepitous", 145, 145, 145, 255);
        crystallosReagent = new Reagent("crystallos", 135, 255, 231, 255);
        terraeReagent = new Reagent("terrae", 147, 48, 13, 255);
        tenebraeReagent = new Reagent("tenebrae", 86, 86, 86, 255);
        offensaReagent = new Reagent("offensa", 126, 0, 0, 255);
        praesidiumReagent = new Reagent("praesidium", 135, 135, 135, 255);
        orbisTerraeReagent = new Reagent("orbisTerrae", 32, 94, 14, 255);
        virtusReagent = new Reagent("virtus", 180, 0, 0, 255);
        reductusReagent = new Reagent("reductus", 20, 93, 2, 255);
        potentiaReagent = new Reagent("potentia", 64, 81, 208, 255);

        registerReagent(sanctusReagent);
        registerReagent(incendiumReagent);
        registerReagent(aquasalusReagent);
        registerReagent(magicalesReagent);
        registerReagent(aetherReagent);
        registerReagent(crepitousReagent);
        registerReagent(crystallosReagent);
        registerReagent(terraeReagent);
        registerReagent(tenebraeReagent);
        registerReagent(offensaReagent);
        registerReagent(praesidiumReagent);
        registerReagent(orbisTerraeReagent);
        registerReagent(virtusReagent);
        registerReagent(reductusReagent);
        registerReagent(potentiaReagent);
    }

    public static boolean registerReagent(Reagent reagent) {
        if (reagent == null || reagentList.containsKey(reagent.name())) {
            return false;
        }

        reagentList.put(reagent.name(), reagent);

        return true;
    }

    public static Reagent getReagentForKey(String key) {
        return reagentList.get(key);
    }

    public static String getKeyForReagent(Reagent reagent) {
        if (reagentList.containsValue(reagent)) {
            Set<Entry<String, Reagent>> set = reagentList.entrySet();
            for (Entry<String, Reagent> entry : set) {
                if (entry.getValue().equals(reagent)) {
                    return entry.getKey();
                }
            }
        }

        return "";
    }

    public static void registerItemAndReagent(ItemStack stack, ReagentStack reagentStack) {
        itemToReagentMap.put(stack, reagentStack);
    }

    public static ReagentStack getReagentStackForItem(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        for (Entry<ItemStack, ReagentStack> entry : itemToReagentMap.entrySet()) {
            if (entry.getKey() != null && entry.getKey().isItemEqual(stack)) {
                if (entry.getValue() == null) {
                    return null;
                } else {
                    return entry.getValue().copy();
                }
            }
        }

        return null;
    }

    public static ItemStack getItemForReagent(Reagent reagent) {
        if (reagent == null) {
            return null;
        }

        for (Entry<ItemStack, ReagentStack> entry : itemToReagentMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().reagent == reagent) {
                if (entry.getKey() == null) {
                    return null;
                }
                return entry.getKey().copy();
            }
        }

        return null;
    }
}
