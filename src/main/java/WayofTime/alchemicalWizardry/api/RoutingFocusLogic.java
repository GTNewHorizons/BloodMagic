package WayofTime.alchemicalWizardry.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class RoutingFocusLogic {

    public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack) {
        return (keyStack != null && keyStack.getItem() != null
                && checkedStack != null
                && keyStack.getItem() == checkedStack.getItem()
                && (!keyStack.getItem().getHasSubtypes() || keyStack.getItemDamage() == checkedStack.getItemDamage()));
    }

    public boolean doesItemMatch(boolean previous, ItemStack keyStack, ItemStack checkedStack) {
        return previous || this.getDefaultMatch(keyStack, checkedStack);
    }

    public List<String> getDescription() {
        return new ArrayList<>(
                Collections.singletonList(
                        StatCollector.translateToLocal("tooltip.routingFocus.logic") + " "
                                + StatCollector.translateToLocal("item.outputRoutingFocus.default.logic")));
    }
}
