package WayofTime.alchemicalWizardry.common.summoning.meteor;

import static WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry.reagentList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;

public class MeteorReagentRegistry {

    public static Map<Reagent, MeteorReagent> reagents = new HashMap<>();

    public static void loadConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(MeteorComponent.class, new MeteorComponentAdapter()).create();
        File file = new File("config/BloodMagic/meteors/reagents/");
        File[] files = file.listFiles();
        if (files != null) {
            for (String reagent : reagentList.keySet()) {
                try {
                    File f = new File("config/BloodMagic/meteors/reagents/" + reagent + ".json");
                    if (!f.isFile()) {
                        continue;
                    }
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    MeteorReagent r = gson.fromJson(br, MeteorReagent.class);
                    if (r == null) {
                        continue;
                    }
                    reagents.put(ReagentRegistry.getReagentForKey(reagent), r);
                } catch (FileNotFoundException | JsonSyntaxException e) {
                    AlchemicalWizardry.logger.warn("Error adding reagent {}", reagent);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the one largest radius increase from all given reagents (positive value in config).
     */
    public static int getLargestRadiusIncrease(List<Reagent> reagentList) {
        int increase = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            int change = reagents.get(r).radiusChange;
            if (change > increase) {
                increase = change;
            }
        }
        return increase;
    }

    /**
     * @return the one largest radius decrease from all given reagents (negative value in config).
     */
    public static int getLargestRadiusDecrease(List<Reagent> reagentList) {
        int decrease = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            int change = reagents.get(r).radiusChange;
            if (change < decrease) {
                decrease = change;
            }
        }
        return decrease;
    }

    /**
     * @return the one largest filler chance increase from all given reagents (positive value in config).
     */
    public static int getLargestFillerChanceIncrease(List<Reagent> reagentList) {
        int increase = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            int change = reagents.get(r).fillerChanceChange;
            if (change > increase) {
                increase = change;
            }
        }
        return increase;
    }

    /**
     * @return the one largest filler chance decrease from all given reagents (negative value in config).
     */
    public static int getLargestFillerChanceDecrease(List<Reagent> reagentList) {
        int decrease = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            int change = reagents.get(r).fillerChanceChange;
            if (change < decrease) {
                decrease = change;
            }
        }
        return decrease;
    }

    /**
     * @return the one largest raw filler chance increase from all given reagents (positive value in config).
     */
    public static float getLargestRawFillerChanceIncrease(List<Reagent> reagentList) {
        float increase = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            float change = reagents.get(r).rawFillerChanceChange;
            if (change > increase) {
                increase = change;
            }
        }
        return increase;
    }

    /**
     * @return the one largest raw filler chance decrease from all given reagents (negative value in config).
     */
    public static float getLargestRawFillerChanceDecrease(List<Reagent> reagentList) {
        float decrease = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            float change = reagents.get(r).rawFillerChanceChange;
            if (change < decrease) {
                decrease = change;
            }
        }
        return decrease;
    }

    /**
     * @return a list of the MeteorComponents that the given reagents will use to replace filler.
     */
    public static List<MeteorComponent> getFillerList(List<Reagent> reagentList) {
        List<MeteorComponent> fillerList = new ArrayList<>();
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            List<MeteorComponent> filler = reagents.get(r).filler;
            if (filler != null && !filler.isEmpty()) {
                fillerList.addAll(filler);
            }
        }
        return fillerList;
    }

    /**
     * @return false if any of the given reagents disable explosions, otherwise true
     */
    public static boolean doExplosions(List<Reagent> reagentList) {
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            if (reagents.get(r).disableExplosions) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the value of the config AlchemicalWizardry.doMeteorsDestroyBlocks if no reagent inverts explosion block
     *         damage. If any reagent inverts explosion block damage, the inverse of the config
     *         AlchemicalWizardry.doMeteorsDestroyBlocks is returned.
     */
    public static boolean doMeteorsDestroyBlocks(List<Reagent> reagentList) {
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            if (reagents.get(r).invertExplosionBlockDamage) {
                return !AlchemicalWizardry.doMeteorsDestroyBlocks;
            }
        }
        return AlchemicalWizardry.doMeteorsDestroyBlocks;
    }
}
