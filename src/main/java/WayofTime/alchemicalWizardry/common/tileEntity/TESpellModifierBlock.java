package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.ComplexSpellModifier;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public class TESpellModifierBlock extends TESpellBlock {

    @Override
    protected void applySpellChange(SpellParadigm parad) {
        parad.modifyBufferedEffect(this.getSpellModifier());
    }

    public ComplexSpellModifier getSpellModifier() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return switch (meta) {
            case 1 -> ComplexSpellModifier.OFFENSIVE;
            case 2 -> ComplexSpellModifier.DEFENSIVE;
            case 3 -> ComplexSpellModifier.ENVIRONMENTAL;
            default -> ComplexSpellModifier.DEFAULT;
        };
    }

    @Override
    public String getResourceLocationForMeta(int meta) {
        return switch (meta) {
            case 2 -> "alchemicalwizardry:textures/models/SpellModifierDefensive.png";
            case 3 -> "alchemicalwizardry:textures/models/SpellModifierEnvironmental.png";
            default -> "alchemicalwizardry:textures/models/SpellModifierDefault.png";
        };
    }
}
