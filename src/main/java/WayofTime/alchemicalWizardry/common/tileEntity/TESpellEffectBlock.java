package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellEffect;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public class TESpellEffectBlock extends TESpellBlock {

    @Override
    protected void applySpellChange(SpellParadigm parad) {
        parad.addBufferedEffect(this.getSpellEffect());
    }

    public SpellEffect getSpellEffect() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return switch (meta) {
            case 1 -> new SpellEffect(ComplexSpellType.ICE);
            case 2 -> new SpellEffect(ComplexSpellType.WIND);
            case 3 -> new SpellEffect(ComplexSpellType.EARTH);
            default -> new SpellEffect(ComplexSpellType.FIRE);
        };
    }

    @Override
    public String getResourceLocationForMeta(int meta) {
        return switch (meta) {
            case 0 -> "alchemicalwizardry:textures/models/SpellEffectFire.png";
            case 1 -> "alchemicalwizardry:textures/models/SpellEffectIce.png";
            case 2 -> "alchemicalwizardry:textures/models/SpellEffectWind.png";
            case 3 -> "alchemicalwizardry:textures/models/SpellEffectEarth.png";
            default -> "";
        };
    }
}
