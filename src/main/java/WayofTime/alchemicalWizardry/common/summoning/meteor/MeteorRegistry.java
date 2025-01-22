package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;

public class MeteorRegistry {

    public static List<Meteor> meteorList = new ArrayList<>();

    public static void loadConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(MeteorComponent.class, new MeteorComponentAdapter())
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter()).create();
        File file = new File("config/BloodMagic/meteors");
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    if (f.isDirectory() || !f.getName().endsWith(".json")) {
                        continue;
                    }
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    Meteor m = gson.fromJson(br, Meteor.class);
                    MeteorRegistry.registerMeteor(m);
                } catch (FileNotFoundException | JsonSyntaxException e) {
                    AlchemicalWizardry.logger.warn("Error adding meteor {}", f.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void registerMeteor(Meteor meteor) {
        meteor.validate();
        meteorList.add(meteor);
    }

    public static void registerMeteor(ItemStack stack, String[] componentList, int radius, int cost) {
        registerMeteor(stack, MeteorComponent.parseStringArray(componentList), radius, cost, null, 0);
    }

    public static void registerMeteor(ItemStack stack, List<MeteorComponent> componentList, int radius, int cost,
            List<MeteorComponent> fillerList, int fillerChance) {
        if (stack != null && componentList != null) {
            Meteor meteor = new Meteor(stack, radius, cost, fillerChance, componentList, fillerList);
            meteor.validate();
            meteorList.add(meteor);
        }
    }

    public static void createMeteorImpact(World world, int x, int y, int z, int meteorID, List<Reagent> reagents) {
        if (meteorID < meteorList.size()) {
            meteorList.get(meteorID).createMeteorImpact(world, x, y, z, reagents);
        }
    }

    public static int getMeteorIDForItem(ItemStack stack) {
        if (stack == null) {
            return -1;
        }

        for (int i = 0; i < meteorList.size(); i++) {
            ItemStack focusStack = meteorList.get(i).focusItem;

            if (focusStack != null && focusStack.getItem() == stack.getItem()
                    && (focusStack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                            || focusStack.getItemDamage() == stack.getItemDamage())) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isValidMeteorFocusItem(ItemStack stack) {
        return getMeteorIDForItem(stack) != -1;
    }
}
