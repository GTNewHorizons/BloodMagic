package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class Meteor {
    public String[] ores;
    public int radius;
    public int cost;
    public String focusModId;
    public String focusName;
    public int focusMeta;

    public static void loadConfig()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("config/BloodMagic/meteors");
        File[] files = file.listFiles();
        if (files != null) {
            try {
                for (File f : files) {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    Meteor m = gson.fromJson(br, Meteor.class);
                    MeteorRegistry.registerMeteorParadigm(findItemStack(m.focusModId, m.focusName, m.focusMeta), m.ores, m.radius, m.cost);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static ItemStack findItemStack(String modid, String name, int meta) {
        ItemStack is = GameRegistry.findItemStack(modid, name, 1);
        if (is == null)
            return null;
        Items.feather.setDamage(is, meta);
        return is;
    }
}
