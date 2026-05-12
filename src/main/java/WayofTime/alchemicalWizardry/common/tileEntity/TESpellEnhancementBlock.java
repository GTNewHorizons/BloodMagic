package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.SpellEnhancement;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementCost;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementPotency;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementPower;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public class TESpellEnhancementBlock extends TESpellBlock {

    @Override
    protected void applySpellChange(SpellParadigm parad) {
        int i = switch (this.enhancementType()) {
            case 0 -> parad.getBufferedEffectPower();
            case 1 -> parad.getBufferedEffectCost();
            case 2 -> parad.getBufferedEffectPotency();
            default -> -1;
        };

        if (i != -1 && i < this.getLimit()) {
            parad.applyEnhancement(getSpellEnhancement());
        } else if (i < this.getLimit()) {
            this.doBadStuff();
        }
    }

    public SpellEnhancement getSpellEnhancement() {
        return switch (enhancementType()) {
            case 0 -> new SpellEnhancementPower();
            case 2 -> new SpellEnhancementPotency();
            default -> new SpellEnhancementCost();
        };
    }

    public int getLimit() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if (meta > 14) return 0;
        return meta % 5 + 1;
    }

    // 0 is power, 1 is cost, 2 is potency, -1 is invalid
    public int enhancementType() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        if (meta > 14) return -1;
        return meta / 5;
    }

    public void doBadStuff() {}

    @Override
    public String getResourceLocationForMeta(int meta) {
        return switch (meta) {
            case 1 -> "alchemicalwizardry:textures/models/SpellEnhancementPower2.png";
            case 2 -> "alchemicalwizardry:textures/models/SpellEnhancementPower3.png";
            case 3 -> "alchemicalwizardry:textures/models/SpellEnhancementPower4.png";
            case 5 -> "alchemicalwizardry:textures/models/SpellEnhancementCost1.png";
            case 6 -> "alchemicalwizardry:textures/models/SpellEnhancementCost2.png";
            case 7 -> "alchemicalwizardry:textures/models/SpellEnhancementCost3.png";
            case 8 -> "alchemicalwizardry:textures/models/SpellEnhancementCost4.png";
            case 10 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency1.png";
            case 11 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency2.png";
            case 12 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency3.png";
            case 13 -> "alchemicalwizardry:textures/models/SpellEnhancementPotency4.png";
            default -> "alchemicalwizardry:textures/models/SpellEnhancementPower1.png";
        };
    }
}
