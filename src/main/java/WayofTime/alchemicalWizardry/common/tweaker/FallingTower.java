package WayofTime.alchemicalWizardry.common.tweaker;

import static WayofTime.alchemicalWizardry.common.tweaker.MTHelper.toStack;

import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorParadigm;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * MineTweaker3 Falling Tower Paradigm Handler by hilburn *
 */
@ZenClass("mods.bloodmagic.FallingTower")
public class FallingTower {

    @ZenMethod
    public static void addFocus(IItemStack stack, int radius, String[] components) {
        MineTweakerAPI.apply(new Add(toStack(stack), radius, 10000, components));
    }

    @ZenMethod
    public static void addFocus(IItemStack stack, int radius, String components) {
        MineTweakerAPI.apply(new Add(toStack(stack), radius, 10000, components.split("\\s*,\\s*")));
    }

    @ZenMethod
    public static void addFocus(IItemStack stack, int radius, int cost, String[] components) {
        MineTweakerAPI.apply(new Add(toStack(stack), radius, cost, components));
    }

    @ZenMethod
    public static void addFocus(IItemStack stack, int radius, int cost, String components) {
        MineTweakerAPI.apply(new Add(toStack(stack), radius, cost, components.split("\\s*,\\s*")));
    }

    @ZenMethod
    public static void addFocus(IItemStack stack, int radius, int cost, String components, String filler,
            int fillerChance) {
        MineTweakerAPI.apply(
                new Add(
                        toStack(stack),
                        radius,
                        cost,
                        components.split("\\s*,\\s*"),
                        filler.split("\\s*,\\s*"),
                        fillerChance));
    }

    @ZenMethod
    public static void removeFocus(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Add implements IUndoableAction {

        private MeteorParadigm paradigm;

        public Add(ItemStack stack, int radius, int cost, String[] components) {
            new Add(stack, radius, cost, components, null, 0);
        }

        public Add(ItemStack stack, int radius, int cost, String[] components, String[] filler, int fillerChance) {
            paradigm = new MeteorParadigm(stack, radius, cost, fillerChance);
            paradigm.componentList = MeteorParadigm.parseStringArray(components);
            if (filler != null) {
                paradigm.fillerList = MeteorParadigm.parseStringArray(filler);
            }
        }

        @Override
        public void apply() {
            MeteorRegistry.registerMeteorParadigm(paradigm);
        }

        @Override
        public boolean canUndo() {
            return MeteorRegistry.paradigmList != null;
        }

        @Override
        public void undo() {
            MeteorRegistry.paradigmList.remove(paradigm);
        }

        @Override
        public String describe() {
            return "Adding Falling Tower Focus for " + paradigm.focusStack.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Removing Falling Tower Focus for " + paradigm.focusStack.getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    private static class Remove implements IUndoableAction {

        private final ItemStack focus;
        private MeteorParadigm paradigm;

        public Remove(ItemStack focus) {
            this.focus = focus;
        }

        @Override
        public void apply() {
            for (Iterator<MeteorParadigm> itr = MeteorRegistry.paradigmList.iterator(); itr.hasNext();) {
                MeteorParadigm paradigm = itr.next();
                if (OreDictionary.itemMatches(paradigm.focusStack, focus, false)) {
                    this.paradigm = paradigm;
                    itr.remove();
                    break;
                }
            }
        }

        @Override
        public boolean canUndo() {
            return MeteorRegistry.paradigmList != null && paradigm != null;
        }

        @Override
        public void undo() {
            MeteorRegistry.paradigmList.add(paradigm);
        }

        @Override
        public String describe() {
            return "Removing Falling Tower Focus for " + focus.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Restoring Falling Tower Focus for " + focus.getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
