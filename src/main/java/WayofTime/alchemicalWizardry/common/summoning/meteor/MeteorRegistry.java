package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class MeteorRegistry {

    public static List<MeteorParadigm> paradigmList = new ArrayList<>();

    public static void registerMeteorParadigm(MeteorParadigm paradigm) {
        paradigmList.add(paradigm);
    }

    public static void registerMeteorParadigm(ItemStack stack, String[] componentList, int radius, int cost) {
        registerMeteorParadigm(stack, componentList, radius, cost, null, 0);
    }

    public static void registerMeteorParadigm(ItemStack stack, String[] componentList, int radius, int cost,
            String[] fillerList, int fillerChance) {
        if (stack != null && componentList != null) {
            MeteorParadigm meteor = new MeteorParadigm(stack, radius, cost, fillerChance);
            meteor.parseStringArray(componentList);
            if (fillerList != null && fillerList.length > 0) {
                meteor.parseStringArray(fillerList, true);
            } else {
                meteor.fillerList.add(new MeteorParadigmComponent(new ItemStack(Blocks.stone), 1));
            }
            paradigmList.add(meteor);
        }
    }

    public static void createMeteorImpact(World world, int x, int y, int z, int paradigmID, boolean[] flags) {
        if (paradigmID < paradigmList.size()) {
            paradigmList.get(paradigmID).createMeteorImpact(world, x, y, z, flags);
        }
    }

    public static int getParadigmIDForItem(ItemStack stack) {
        if (stack == null) {
            return -1;
        }

        for (int i = 0; i < paradigmList.size(); i++) {
            ItemStack focusStack = paradigmList.get(i).focusStack;

            if (focusStack != null && focusStack.getItem() == stack.getItem()
                    && (focusStack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                            || focusStack.getItemDamage() == stack.getItemDamage())) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isValidParadigmItem(ItemStack stack) {
        return getParadigmIDForItem(stack) != -1;
    }
}
