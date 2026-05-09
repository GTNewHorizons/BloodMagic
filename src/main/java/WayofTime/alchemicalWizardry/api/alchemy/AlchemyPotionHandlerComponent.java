package WayofTime.alchemicalWizardry.api.alchemy;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record AlchemyPotionHandlerComponent(ItemStack itemStack, int potionID, int tickDuration) {

    public boolean compareItemStack(ItemStack comparedStack) {
        if (comparedStack != null && itemStack != null) {
            if (comparedStack.getItem() instanceof ItemBlock) {
                if (itemStack.getItem() instanceof ItemBlock) {
                    return comparedStack.getItem().equals(itemStack.getItem())
                            && comparedStack.getItemDamage() == itemStack.getItemDamage();
                }
            } else if (!(itemStack.getItem() instanceof ItemBlock)) {
                return comparedStack.getItem().equals(itemStack.getItem())
                        && comparedStack.getItemDamage() == itemStack.getItemDamage();
            }
        }

        return false;
    }
}
