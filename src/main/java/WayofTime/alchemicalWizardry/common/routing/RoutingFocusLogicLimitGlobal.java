package WayofTime.alchemicalWizardry.common.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.common.items.routing.ILimitedRoutingFocus;

public class RoutingFocusLogicLimitGlobal extends RoutingFocusLogicLimit {

    public int limit = 0;

    public RoutingFocusLogicLimitGlobal(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ILimitedRoutingFocus) {
            limit = ((ILimitedRoutingFocus) stack.getItem()).getRoutingFocusLimit(stack);
        }
    }

    @Override
    public int getRoutingLimit() {
        return limit;
    }

    @Override
    public boolean getDefaultMatch(ItemStack keyStack, ItemStack checkedStack) {
        return true;
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(
                Collections.singletonList(
                        StatCollector.translateToLocal("tooltip.routingFocus.logic") + " "
                                + StatCollector.translateToLocal("item.outputRoutingFocus.global.logic")));
    }
}
