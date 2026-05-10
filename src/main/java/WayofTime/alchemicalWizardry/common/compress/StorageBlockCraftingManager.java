package WayofTime.alchemicalWizardry.common.compress;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;

public class StorageBlockCraftingManager {

    private static final StorageBlockCraftingManager instance = new StorageBlockCraftingManager();
    private List<IRecipe> recipes = new LinkedList<>();

    public static StorageBlockCraftingManager getInstance() {
        return instance;
    }

    public void addStorageBlockRecipes() {
        this.recipes = new StorageBlockCraftingRecipeAssimilator().getPackingRecipes();

        AlchemicalWizardry.logger.info("Total number of compression recipes: {}", this.recipes.size());
    }

    private static ItemStack getRecipe(ItemStack stack, World world, int gridSize, List<IRecipe> list) {
        InventoryCrafting inventory = new InventoryCrafting(new Container() {

            @Override
            public boolean canInteractWith(EntityPlayer player) {
                return false;
            }
        }, gridSize, gridSize);
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, stack);
        }

        return StorageBlockCraftingManager.getInstance().findMatchingRecipe(inventory, world, list);
    }

    private static boolean has22Recipe(ItemStack stack, World world, List<IRecipe> list) {
        return get22Recipe(stack, world, list) != null;
    }

    private static ItemStack get22Recipe(ItemStack stack, World world, List<IRecipe> list) {
        return getRecipe(stack, world, 2, list);
    }

    private static boolean has33Recipe(ItemStack stack, World world, List<IRecipe> list) {
        return get33Recipe(stack, world, list) != null;
    }

    private static ItemStack get33Recipe(ItemStack stack, World world, List<IRecipe> list) {
        return getRecipe(stack, world, 3, list);
    }

    public ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_) {
        return this.findMatchingRecipe(p_82787_1_, p_82787_2_, this.recipes);
    }

    private ItemStack findMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_, List<IRecipe> list) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < p_82787_1_.getSizeInventory(); ++j) {
            ItemStack itemstack2 = p_82787_1_.getStackInSlot(j);

            if (itemstack2 != null) {
                if (i == 0) {
                    itemstack = itemstack2;
                }

                if (i == 1) {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem()
                && itemstack.stackSize == 1
                && itemstack1.stackSize == 1
                && itemstack.getItem().isRepairable()) {
            Item item = itemstack.getItem();
            int j1 = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
            int k = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
            int l = j1 + k + item.getMaxDamage() * 5 / 100;
            int i1 = item.getMaxDamage() - l;

            if (i1 < 0) {
                i1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, i1);
        } else {
            for (IRecipe irecipe : list) {
                if (irecipe.matches(p_82787_1_, p_82787_2_)) {
                    return irecipe.getCraftingResult(p_82787_1_);
                }
            }

            return null;
        }
    }
}
