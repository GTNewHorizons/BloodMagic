package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.SpellEnhancement;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementCost;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementPotency;
import WayofTime.alchemicalWizardry.api.spell.SpellEnhancementPower;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public class TESpellEnhancementBlock extends TESpellBlock {

    @Override
    protected void applySpellChange(SpellParadigm parad) {
        int i = -1;

        i = switch (this.enhancementType()) {
            case 0 -> parad.getBufferedEffectPower();
            case 1 -> parad.getBufferedEffectCost();
            case 2 -> parad.getBufferedEffectPotency();
            default -> i;
        };

        if (i != -1 && i < this.getLimit()) {
            parad.applyEnhancement(getSpellEnhancement());
        } else if (i < this.getLimit()) {
            this.doBadStuff();
        }
    }

    public SpellEnhancement getSpellEnhancement() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return switch (meta) {
            case 0, 1, 2, 3, 4 -> new SpellEnhancementPower();
            case 10, 11, 12, 13, 14 -> new SpellEnhancementPotency();
            default -> new SpellEnhancementCost();
        };
    }

    public int getLimit() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return switch (meta) {
            case 0, 10, 5 -> 1;
            case 1, 11, 6 -> 2;
            case 2, 12, 7 -> 3;
            case 3, 13, 8 -> 4;
            case 4, 14, 9 -> 5;
            default -> 0;
        };
    }

    public int enhancementType() // 0 is power, 1 is cost, 2 is potency
    {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return switch (meta) {
            case 0, 1, 2, 3, 4 -> 0;
            default -> 1;
        };
    }

    public void doBadStuff() {}

    @Override
    public String getResourceLocationForMeta(int meta) {
        return switch (meta) {
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
