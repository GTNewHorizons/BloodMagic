package WayofTime.alchemicalWizardry.common.summoning.meteor;

import static WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry.reagentList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
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
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        File file = new File("config/BloodMagic/meteors/reagents/");
        if (!file.isDirectory()) {
            MeteorReagentRegistry.generateDefaultConfig();
        }
        File[] files = file.listFiles();
        if (files != null) {
            try {
                for (String reagent : reagentList.keySet()) {
                    File f = new File("config/BloodMagic/meteors/reagents/" + reagent + ".json");
                    if (!f.isFile()) {
                        continue;
                    }
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    MeteorReagent r = gson.fromJson(br, MeteorReagent.class);
                    if (r == null) {
                        continue;
                    }
                    if (r.filler.length > 0) {
                        r.parsedFiller = MeteorParadigm.parseStringArray(r.filler);
                    }
                    reagents.put(ReagentRegistry.getReagentForKey(reagent), r);
                }
            } catch (FileNotFoundException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    // Returns the one largest radius increase (positive config).
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

    // Returns the one largest radius decrease (negative config).
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

    // Returns the one largest filler chance increase (positive config).
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

    // Returns the one largest filler chance decrease (negative config).
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

    // Returns the one largest raw filler chance increase (positive config).
    public static int getLargestRawFillerChanceIncrease(List<Reagent> reagentList) {
        int increase = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            int change = reagents.get(r).rawFillerChanceChange;
            if (change > increase) {
                increase = change;
            }
        }
        return increase;
    }

    // Returns the one largest raw filler chance decrease (negative config).
    public static int getLargestRawFillerChanceDecrease(List<Reagent> reagentList) {
        int decrease = 0;
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            int change = reagents.get(r).rawFillerChanceChange;
            if (change < decrease) {
                decrease = change;
            }
        }
        return decrease;
    }

    // Returns a list of the blocks that the given reagents will use to replace filler.
    public static List<MeteorParadigmComponent> getFillerList(List<Reagent> reagentList) {
        List<MeteorParadigmComponent> fillerList = new ArrayList<>();
        for (Reagent r : reagentList) {
            if (!reagents.containsKey(r)) {
                continue;
            }
            List<MeteorParadigmComponent> filler = reagents.get(r).parsedFiller;
            if (filler != null && !filler.isEmpty()) {
                fillerList.addAll(filler);
            }
        }
        return fillerList;
    }

    // Returns false if any of the given reagents disable explosions, otherwise true
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
     * Returns the value of the config AlchemicalWizardry.doMeteorsDestroyBlocks if no reagent inverts explosion block
     * damage Returns the inverse of the value of AlchemicalWizardry.doMeteorsDestroyBlocks if any reagent inverts
     * explosion block damage
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

    public static void generateDefaultConfig() {
        Map<String, String[]> lineMap = new HashMap<>();
        lineMap.put("terrae", new String[] { "{", "  \"radiusChange\": 1,", "  \"fillerChanceChange\": 10", "}", });
        lineMap.put(
                "orbisTerrae",
                new String[] { "{", "  \"radiusChange\": 2,", "  \"fillerChanceChange\": 20", "}", });
        lineMap.put("tenebrae", new String[] { "{", "  \"filler\":  [\"minecraft:obsidian:0:180\"]", "}", });
        lineMap.put(
                "incendium",
                new String[] { "{", "  \"filler\":  [", "    \"minecraft:netherrack:0:60\",",
                        "    \"minecraft:glowstone:0:60\",", "    \"minecraft:soul_sand:0:60\"", "  ]", "}", });
        lineMap.put("crystallos", new String[] { "{", "  \"filler\":  [\"minecraft:ice:0:180\"]", "}", });
        try {
            Files.createDirectories(Paths.get("config/BloodMagic/meteors/reagents/"));
            String[] reagents = { "terrae", "orbisTerrae", "tenebrae", "incendium", "crystallos" };
            for (String reagent : reagents) {
                Path path = Paths.get("config/BloodMagic/meteors/reagents/" + reagent + ".json");
                Files.createFile(path);
                Files.write(path, Arrays.asList(lineMap.get(reagent)), StandardOpenOption.WRITE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
