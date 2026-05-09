package WayofTime.alchemicalWizardry.api.items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;

/**
 * Shapeless Blood Orb Recipe Handler by joshie *
 */
public class ShapelessBloodOrbRecipe implements IRecipe {

    private ItemStack output;
    private final ArrayList<Object> input = new ArrayList<Object>();

    public ShapelessBloodOrbRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapelessBloodOrbRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapelessBloodOrbRecipe(ItemStack result, Object... recipe) {
        output = result.copy();
        for (Object in : recipe) {
            if (in instanceof IBloodOrb orb) {
                input.add(orb.getOrbLevel());
            } else if (in instanceof ItemStack stack && stack.getItem() instanceof IBloodOrb orb) {
                input.add(orb.getOrbLevel());
            } else if (in instanceof ItemStack stack) {
                input.add(stack.copy());
            } else if (in instanceof Item item) {
                input.add(new ItemStack(item));
            } else if (in instanceof Block block) {
                input.add(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));
            } else if (in instanceof String string) {
                input.add(OreDictionary.getOres(string));
            } else {
                StringBuilder ret = new StringBuilder("Invalid shapeless ore recipe: ");
                for (Object tmp : recipe) {
                    ret.append(tmp).append(", ");
                }
                ret.append(output);
                throw new RuntimeException(ret.toString());
            }
        }
    }

    ShapelessBloodOrbRecipe(ShapelessRecipes recipe, Map<ItemStack, String> replacements) {
        output = recipe.getRecipeOutput();

        for (ItemStack ingred : recipe.recipeItems) {
            Object finalObj = ingred;
            for (Entry<ItemStack, String> replace : replacements.entrySet()) {
                if (OreDictionary.itemMatches(replace.getKey(), ingred, false)) {
                    finalObj = OreDictionary.getOres(replace.getValue());
                    break;
                }
            }
            input.add(finalObj);
        }
    }

    @Override
    public int getRecipeSize() {
        return input.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return output.copy();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world) {
        ArrayList<Object> required = new ArrayList<Object>(input);

        for (int x = 0; x < var1.getSizeInventory(); x++) {
            ItemStack slot = var1.getStackInSlot(x);

            if (slot != null) {
                boolean inRecipe = false;

                for (Object o : required) {
                    boolean match = false;

                    Object next = o;

                    // If target is integer, then we should be check the blood orb value of the item instead
                    if (next instanceof Integer) {
                        if (slot.getItem() instanceof IBloodOrb orb) {
                            if (orb.getOrbLevel() < (Integer) next) {
                                return false;
                            }
                        } else return false;
                        match = true;
                    } else if (next instanceof ItemStack) {
                        match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                    } else if (next instanceof ArrayList) {
                        Iterator<ItemStack> itr = ((ArrayList<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public ArrayList<Object> getInput() {
        return this.input;
    }
}
