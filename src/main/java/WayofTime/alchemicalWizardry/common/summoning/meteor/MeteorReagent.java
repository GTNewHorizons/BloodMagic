package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class MeteorReagent {

    @Expose
    public boolean disableExplosions = false;
    @Expose
    public boolean invertExplosionBlockDamage = false;
    @Expose
    public int radiusChange = 0;
    @Expose
    public int fillerChanceChange = 0;
    @Expose
    public int rawFillerChanceChange = 0;
    @Expose
    public String[] filler = {};
    public List<MeteorParadigmComponent> parsedFiller = new ArrayList<>();

}
