package WayofTime.alchemicalWizardry.common.spell.complex.effect.cse.earth;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellEffect;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.ComplexSpellType;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;

public class CSEToolDefensiveEarth extends ComplexSpellEffect {

    public CSEToolDefensiveEarth() {
        super(ComplexSpellType.EARTH, ComplexSpellModifier.DEFENSIVE);
    }

    public CSEToolDefensiveEarth(int power, int cost, int potency) {
        this();

        this.powerEnhancement = power;
        this.costEnhancement = cost;
        this.potencyEnhancement = potency;
    }

    @Override
    public void modifyParadigm(SpellParadigm parad) {
        if (parad instanceof SpellParadigmTool) {
            String toolClass = "shovel";

            float digSpeed = switch (this.powerEnhancement) {
                case 1 -> 9.0f;
                case 2 -> 12.0f;
                case 3 -> 16.0f;
                case 4 -> 21.0f;
                case 5 -> 27.0f;
                default -> 7.0f;
            };

            ((SpellParadigmTool) parad).setDigSpeed(toolClass, digSpeed);

            int hlvl = this.potencyEnhancement + 2;
            ((SpellParadigmTool) parad).setHarvestLevel(toolClass, hlvl);
        }
    }

    @Override
    public ComplexSpellEffect copy(int power, int cost, int potency) {
        return new CSEToolDefensiveEarth(power, cost, potency);
    }

    @Override
    public int getCostOfEffect() {
        return (int) (1000 * (1 + this.potencyEnhancement * 0.1f)
                * (1 + this.powerEnhancement * 0.2f)
                * Math.pow(0.85, costEnhancement));
    }
}
