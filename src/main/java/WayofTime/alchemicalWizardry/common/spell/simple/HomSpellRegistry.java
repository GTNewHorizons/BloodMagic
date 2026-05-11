package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class HomSpellRegistry {

    public static List<HomSpellComponent> spellList = new ArrayList<>();

    public static void registerBasicSpell(ItemStack item, HomSpell spell) {
        spellList.add(new HomSpellComponent(item, spell));
    }

    public static HomSpell getSpellForItemStack(ItemStack testItem) {
        if (testItem == null) {
            return null;
        }

        for (HomSpellComponent hsc : spellList) {
            ItemStack item = hsc.item();

            if (item != null && item.getItem() instanceof ItemBlock && testItem.getItem() == item.getItem()) {
                return hsc.spell();
            }
        }

        return null;
    }
}
