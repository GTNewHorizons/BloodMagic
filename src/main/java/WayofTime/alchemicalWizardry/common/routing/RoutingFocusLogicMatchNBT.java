package WayofTime.alchemicalWizardry.common.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;

public class RoutingFocusLogicMatchNBT extends RoutingFocusLogic {

    public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack) {
        return (keyStack != null && keyStack.getItem() != null
                && checkedStack != null
                && keyStack.getItem() == checkedStack.getItem()
                && (!keyStack.getItem().getHasSubtypes() || keyStack.getItemDamage() == checkedStack.getItemDamage())
                && ItemStack.areItemStackTagsEqual(keyStack, checkedStack));
    }

    @Override
    public boolean doesItemMatch(boolean previous, ItemStack keyStack, ItemStack checkedStack) {
        return previous && this.getDefaultMatch(keyStack, checkedStack);
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(
                Collections.singletonList(
                        StatCollector.translateToLocal("tooltip.routingFocus.logic") + " "
                                + StatCollector.translateToLocal("item.outputRoutingFocus.matchNBT.logic")));
    }
}
