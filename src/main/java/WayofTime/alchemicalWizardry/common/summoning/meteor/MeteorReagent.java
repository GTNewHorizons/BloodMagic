package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;

public class MeteorReagent {

    public boolean disableExplosions = false;
    public boolean invertExplosionBlockDamage = false;
    public int radiusChange = 0;
    public int fillerChanceChange = 0;
    public float rawFillerChanceChange = 0;
    public List<MeteorComponent> filler = new ArrayList<>();

    public static ArrayList<Reagent> parseReagents(String reagent, String blockName) {
        ArrayList<Reagent> reagentList = new ArrayList<>();
        if (reagent != null) {
            String[] reagents = reagent.substring(1).split(", ?");
            for (String str : reagents) {
                Reagent r = ReagentRegistry.getReagentForKey(str);
                if (r == null) {
                    AlchemicalWizardry.logger.warn("Unable to add reagent \"{}\" for {}.", str, blockName);
                    continue;
                }
                reagentList.add(r);
            }
        }
        return reagentList;
    }
}
