package WayofTime.alchemicalWizardry.common.items.sigil.holding;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfHolding extends EnergyItems implements ISigil {

    private static final int invSize = 5;

    private static final String NBT_CURRENT_SIGIL = "CurrentSigil";

    public SigilOfHolding() {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfHolding");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (stack.getTagCompound() == null) {
            return this.itemIcon;
        }
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null) {
            return this.itemIcon;
        }

        ItemStack item = getCurrentSigil(stack);

        if (item != null) {
            return item.getIconIndex();
        }

        return this.itemIcon;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofholding.desc"));

        if (item.getTagCompound() == null) {
            return;
        }
        addBindingInformation(item, tooltip);

        ItemStack[] inv = getInternalInventory(item);

        if (inv == null) {
            return;
        }

        int currentSlot = getCurrentItem(item);
        ItemStack stack = inv[currentSlot];

        if (stack != null) {
            tooltip.add(StatCollector.translateToLocal("tooltip.item.currentitem") + " " + stack.getDisplayName());
        }

        for (int i = 0; i < invSize; i++) {
            if (inv[i] != null) {
                tooltip.add(
                        StatCollector.translateToLocal("tooltip.item.iteminslot") + " "
                                + (i + 1)
                                + ": "
                                + inv[i].getDisplayName());
            }
        }
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int par4, int par5, int par6, int par7,
            float par8, float par9, float par10) {
        if (!IBindable.checkAndSetItemOwner(item, player)) {
            return false;
        }
        int currentSlot = getCurrentItem(item);
        ItemStack[] inv = getInternalInventory(item);

        if (inv == null) {
            return false;
        }

        ItemStack itemUsed = inv[currentSlot];

        if (itemUsed == null || itemUsed.getItem() == null) {
            return false;
        }

        boolean result = itemUsed.getItem().onItemUse(item, player, world, par4, par5, par6, par7, par8, par9, par10);

        saveInventory(item, inv);

        return result;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(item, player)) {
            if (player.isSneaking()) {
                InventoryHolding.setUUID(item);
                player.openGui(
                        AlchemicalWizardry.instance,
                        3,
                        player.worldObj,
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ);
                return item;
            }

            int currentSlot = getCurrentItem(item);
            ItemStack[] inv = getInternalInventory(item);

            if (inv == null) {
                return item;
            }

            ItemStack itemUsed = inv[currentSlot];

            if (itemUsed == null || itemUsed.getItem() == null) {
                return item;
            }

            itemUsed.getItem().onItemRightClick(itemUsed, world, player);
            saveInventory(item, inv);
        }
        return item;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (!IBindable.checkAndSetItemOwner(stack, player)) {
            return false;
        }
        int currentSlot = getCurrentItem(stack);
        ItemStack[] inv = getInternalInventory(stack);

        if (inv == null) {
            return false;
        }

        ItemStack itemUsed = inv[currentSlot];

        if (itemUsed == null || itemUsed.getItem() == null) {
            return false;
        }

        boolean bool = itemUsed.getItem().onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);

        saveInventory(stack, inv);

        return bool;

    }

    public static int next(int mode) {
        int index = mode + 1;

        if (index >= invSize) {
            index = 0;
        }

        return index;
    }

    public static int prev(int mode) {
        int index = mode - 1;

        if (index < 0) {
            index = invSize;
        }

        return index;
    }

    private static void initModeTag(ItemStack itemStack) {
        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
            itemStack.stackTagCompound.setInteger(NBT_CURRENT_SIGIL, invSize);
        }
    }

    public static ItemStack getCurrentSigil(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof SigilOfHolding) {
            ItemStack[] itemStacks = getInternalInventory(itemStack);
            int currentSlot = getCurrentItem(itemStack);
            if (itemStacks != null) {
                return itemStacks[currentSlot];
            }
        }

        return null;
    }

    public static int getCurrentItem(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof SigilOfHolding) {
            initModeTag(itemStack);
            int currentSigil = itemStack.stackTagCompound.getInteger(NBT_CURRENT_SIGIL);
            currentSigil = MathHelper.clamp_int(currentSigil, 0, invSize);
            return currentSigil;
        }

        return 4;
    }

    public static ItemStack[] getInternalInventory(ItemStack itemStack) {
        initModeTag(itemStack);
        NBTTagCompound tagCompound = itemStack.getTagCompound();

        if (tagCompound == null) {
            return null;
        }

        ItemStack[] inv = new ItemStack[9];
        NBTTagList tagList = tagCompound.getTagList(InventoryHolding.NBT_ITEMS, 10);

        if (tagList == null) {
            return null;
        }

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            int slot = tag.getByte("Slot");

            if (slot >= 0 && slot < invSize) {
                inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }

        return inv;
    }

    public void saveInventory(ItemStack itemStack, ItemStack[] inventory) {
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < invSize; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }

        IBindable.getTag(itemStack).setTag(InventoryHolding.NBT_ITEMS, itemList);
    }

    public static void cycleSigil(ItemStack itemStack, int mode) {
        if (itemStack != null && itemStack.getItem() instanceof SigilOfHolding) {
            initModeTag(itemStack);
            itemStack.stackTagCompound.setInteger(NBT_CURRENT_SIGIL, mode);
        }
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean held) {
        if (!(item.getTagCompound() == null)) {
            this.tickInternalInventory(item, world, entity, slot, held);
        }
    }

    public void tickInternalInventory(ItemStack item, World world, Entity entity, int par4, boolean par5) {
        ItemStack[] inv = getInternalInventory(item);

        if (inv == null) {
            return;
        }

        for (int i = 0; i < invSize; i++) {
            ItemStack stack = inv[i];
            if (stack == null || stack.getItem() == null) {
                continue;
            }

            stack.getItem().onUpdate(stack, world, entity, par4, par5);
        }
    }

    @Override
    public boolean canBeStored() {
        return false;
    }
}
