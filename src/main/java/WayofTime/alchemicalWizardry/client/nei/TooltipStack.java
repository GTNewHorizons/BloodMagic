package WayofTime.alchemicalWizardry.client.nei;

import codechicken.nei.PositionedStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TooltipStack extends PositionedStack {

    private final List<String> tooltips;

    public TooltipStack(Object object, int x, int y, boolean genPerms, List<String> tooltips) {
        super(object, x, y, genPerms);
        this.tooltips = tooltips;
    }

    public TooltipStack(Object object, int x, int y, List<String> tooltips) {
        this(object, x, y, true, tooltips);
    }

    public List<String> getTooltips() {
        return tooltips;
    }
}
