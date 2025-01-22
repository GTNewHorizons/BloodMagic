package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.io.IOException;

import net.minecraft.item.ItemStack;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.common.registry.GameRegistry;

class ItemStackAdapter extends TypeAdapter<ItemStack> {

    @Override
    public ItemStack read(JsonReader reader) throws IOException {
        String str = reader.nextString();
        String[] input = str.split(":");
        if (input.length < 2) {
            AlchemicalWizardry.logger
                    .warn("Unable to read item stack \"{}\". Valid format is \"modid:name(:meta optional)\"", str);
            return null;
        }
        ItemStack itemStack = GameRegistry.findItemStack(input[0], input[1], 1);
        if (itemStack == null) {
            AlchemicalWizardry.logger.warn("Unable to find item stack \"{}\".", str);
            return null;
        }
        if (input.length < 3) {
            return itemStack;
        }
        try {
            itemStack.setItemDamage(Integer.parseInt(input[2]));
        } catch (NumberFormatException e) {
            AlchemicalWizardry.logger
                    .warn("Invalid metadata value {} for item stack {}:{}", input[2], input[0], input[1]);
        }
        return itemStack;
    }

    @Override
    public void write(JsonWriter writer, ItemStack mpc) throws IOException {
        // Not implemented because it is not needed.
    }
}
