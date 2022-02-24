package WayofTime.alchemicalWizardry.client.nei;

import java.util.ArrayList;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import codechicken.nei.ItemList;
import net.minecraft.item.Item;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraft.item.ItemStack;

public class NEIConfig implements IConfigureNEI {
	private static ArrayList<Item> bloodOrbs = null;

	public static ArrayList<Item> getBloodOrbs() {
		if (bloodOrbs == null) {
			synchronized (NEIConfig.class) {
				if (bloodOrbs == null) {
					bloodOrbs = collectAllBloodOrbs();
				}
			}
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
			// If there is NEI no cache - go to item registry
			for (Object anItemRegistry : Item.itemRegistry) {
				Item item = (Item) anItemRegistry;
				if (item instanceof IBloodOrb) {
					bloodOrbsTemp.add(item);
				}
			}
		}
		return bloodOrbsTemp;
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
