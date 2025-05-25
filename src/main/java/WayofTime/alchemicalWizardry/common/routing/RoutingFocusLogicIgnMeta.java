package WayofTime.alchemicalWizardry.common.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;

public class RoutingFocusLogicIgnMeta extends RoutingFocusLogic {

    @Override
    public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack) {
        return (keyStack != null && checkedStack != null && keyStack.getItem() == checkedStack.getItem());
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(
                Collections.singletonList(
                        StatCollector.translateToLocal("tooltip.routingFocus.logic") + " "
                                + StatCollector.translateToLocal("item.outputRoutingFocus.ignMeta.logic")));
    }
}
