package WayofTime.alchemicalWizardry.client.nei;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import codechicken.nei.ItemList;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI {

    private static ArrayList<Item> bloodOrbs = null;
    private static ArrayList<Item> byCapacity = null;

    public static final ResourceLocation ARROW_TEXTURE = new ResourceLocation(
            "alchemicalwizardry",
            "gui/nei/arrow.png");

    public synchronized static ArrayList<Item> getBloodOrbs() {
        if (bloodOrbs == null) {
            bloodOrbs = collectAllBloodOrbs();
        }
        return bloodOrbs;
    }

    private static ArrayList<Item> collectAllBloodOrbs() {
        ArrayList<Item> bloodOrbsTemp = new ArrayList<>();
        for (ItemStack item : ItemList.items) {
            if (item != null && item.getItem() instanceof IBloodOrb) {
                bloodOrbsTemp.add(item.getItem());
            }
        }
        if (bloodOrbsTemp.isEmpty()) {
            // If there is no NEI cache - go to item registry
            for (Object anItemRegistry : Item.itemRegistry) {
                Item item = (Item) anItemRegistry;
                if (item instanceof IBloodOrb) {
                    bloodOrbsTemp.add(item);
                }
            }
        }
        return bloodOrbsTemp;
    }

    public synchronized static ArrayList<Item> getOrbsByCapacity() {
        if (byCapacity == null) {
            byCapacity = new ArrayList<>(getBloodOrbs());
            byCapacity.sort((a, b) -> {
                if (a instanceof IBloodOrb orbA && b instanceof IBloodOrb orbB) {
                    return Integer.compare(orbA.getMaxEssence(), orbB.getMaxEssence());
                }
                return 0;
            });
        }
        return byCapacity;
    }

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new NEIAlchemyRecipeHandler());
        API.registerUsageHandler(new NEIAlchemyRecipeHandler());
        API.registerRecipeHandler(new NEIAltarRecipeHandler());
        API.registerUsageHandler(new NEIAltarRecipeHandler());
        API.registerRecipeHandler(new NEIBloodOrbShapedHandler());
        API.registerUsageHandler(new NEIBloodOrbShapedHandler());
        API.registerRecipeHandler(new NEIBloodOrbShapelessHandler());
        API.registerUsageHandler(new NEIBloodOrbShapelessHandler());
        API.registerRecipeHandler(new NEIBindingRitualHandler());
        API.registerUsageHandler(new NEIBindingRitualHandler());
        API.registerRecipeHandler(new NEIMeteorRecipeHandler());
        API.registerUsageHandler(new NEIMeteorRecipeHandler());
        API.registerRecipeHandler(new NEICalcinatorHandler());
        API.registerUsageHandler(new NEICalcinatorHandler());
    }

    @Override
    public String getName() {
        return "Blood Magic NEI";
    }

    @Override
    public String getVersion() {
        return "1.3";
    }
}
