package WayofTime.alchemicalWizardry.api.spell;

import net.minecraft.nbt.NBTTagCompound;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;

/**
 * New wrapper class to enclose the ComplexSpellEffect
 */
public class SpellEffect {

    public ComplexSpellType type;
    public ComplexSpellModifier modifier;

    protected int powerEnhancement;
    protected int costEnhancement;
    protected int potencyEnhancement;

    public SpellEffect() {
        this(ComplexSpellType.FIRE);
    }

    public SpellEffect(ComplexSpellType type) {
        this(type, ComplexSpellModifier.DEFAULT);
    }

    public SpellEffect(ComplexSpellType type, ComplexSpellModifier modifier) {
        this.type = type;
        this.modifier = modifier;

        this.powerEnhancement = 0;
        this.potencyEnhancement = 0;
        this.costEnhancement = 0;
    }

    public void enhanceEffect(SpellEnhancement enh) {
        if (enh != null) {
            switch (enh.getState()) {
                case SpellEnhancement.POWER -> this.powerEnhancement++;
                case SpellEnhancement.EFFICIENCY -> this.costEnhancement++;
                case SpellEnhancement.POTENCY -> this.potencyEnhancement++;
            }
        }
    }

    public void modifyEffect(ComplexSpellModifier mod) {
        if (mod != null) {
            this.modifier = mod;
        }
    }

    public void modifyParadigm(SpellParadigm parad) // When modifying the paradigm it will instead get the class name
                                                    // and ask the registry
    {
        if (parad == null) {
            return;
        }

        Class<? extends SpellParadigm> paraClass = parad.getClass();

        ComplexSpellEffect effect = SpellEffectRegistry
                .getSpellEffect(paraClass, type, modifier, powerEnhancement, potencyEnhancement, costEnhancement);

        if (effect != null) {
            effect.modifyParadigm(parad);
        }
    }

    public int getCostOfEffect(SpellParadigm parad) {
        if (parad == null) {
            return 0;
        }

        Class<? extends SpellParadigm> paraClass = parad.getClass();

        ComplexSpellEffect effect = SpellEffectRegistry
                .getSpellEffect(paraClass, type, modifier, powerEnhancement, potencyEnhancement, costEnhancement);

        if (effect == null) {
            return 0;
        }

        return effect.getCostOfEffect();
    }

    public NBTTagCompound getTag() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Class", this.getClass().getName());
        tag.setString("type", SpellEffectRegistry.getKeyForType(type));
        tag.setString("modifier", SpellEffectRegistry.getKeyForModifier(modifier));
        tag.setInteger("power", powerEnhancement);
        tag.setInteger("cost", costEnhancement);
        tag.setInteger("potency", potencyEnhancement);

        return tag;
    }

    public static SpellEffect getEffectFromTag(NBTTagCompound tag) {
        try {
            Class<?> clazz = Class.forName(tag.getString("Class"));
            try {
                Object obj = clazz.newInstance();
                if (obj instanceof SpellEffect eff) {
                    eff.type = SpellEffectRegistry.getTypeForKey(tag.getString("type"));
                    eff.modifier = SpellEffectRegistry.getModifierForKey(tag.getString("modifier"));
                    eff.powerEnhancement = tag.getInteger("power");
                    eff.costEnhancement = tag.getInteger("cost");
                    eff.potencyEnhancement = tag.getInteger("potency");

                    return eff;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                AlchemicalWizardry.logger.error(e);
            }
        } catch (ClassNotFoundException e) {
            AlchemicalWizardry.logger.error(e);
        }
        return null;
    }

    public int getPowerEnhancements() {
        return this.powerEnhancement;
    }

    public int getPotencyEnhancements() {
        return this.potencyEnhancement;
    }

    public int getCostEnhancements() {
        return this.costEnhancement;
    }
}
