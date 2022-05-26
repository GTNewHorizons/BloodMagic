package WayofTime.alchemicalWizardry.client.nei;

import java.util.List;

import codechicken.nei.PositionedStack;

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
