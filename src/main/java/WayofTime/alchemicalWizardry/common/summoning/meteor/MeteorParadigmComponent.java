package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;

public class MeteorParadigmComponent {

    protected int weight;
    protected ItemStack itemStack;
    protected ArrayList<Reagent> reagent = new ArrayList<>();

    public MeteorParadigmComponent(ItemStack stack, int weight) {
        this(stack, weight, new ArrayList<>());
    }

    public MeteorParadigmComponent(ItemStack stack, int weight, ArrayList<Reagent> reagent) {
        this.itemStack = stack;
        this.weight = weight;
        this.reagent = reagent;
    }

    public int getWeight() {
        return this.weight;
    }

    public ItemStack getBlock() {
        return itemStack;
    }

    public ArrayList<Reagent> getRequiredReagents() {
        return reagent;
    }

    public boolean checkForReagent(List<Reagent> reagentList) {
        if (reagent.isEmpty()) {
            return true;
        }
        for (Reagent r1 : reagentList) {
            for (Reagent r2 : reagent) {
                if (r1.equals(r2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
