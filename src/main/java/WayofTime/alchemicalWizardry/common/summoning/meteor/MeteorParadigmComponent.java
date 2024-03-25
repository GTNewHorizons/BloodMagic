package WayofTime.alchemicalWizardry.common.summoning.meteor;

import net.minecraft.item.ItemStack;

public class MeteorParadigmComponent {

    protected int chance;
    protected ItemStack itemStack;

    public MeteorParadigmComponent(ItemStack stack, int chance) {
        this.itemStack = stack;
        this.chance = chance;
    }

    public int getChance() {
        return this.chance;
    }

    public ItemStack getValidBlockParadigm() {
        return itemStack;
    }
}
