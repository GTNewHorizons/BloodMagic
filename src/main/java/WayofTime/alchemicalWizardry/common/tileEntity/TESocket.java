package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;

import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;

public class TESocket extends TEInventory {

    public static final int sizeInv = 1;

    private int resultID;
    private int resultDamage;

    private boolean isActive;

    public TESocket() {
        super(sizeInv);
        resultID = 0;
        resultDamage = 0;
        isActive = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        resultID = tag.getInteger("resultID");
        resultDamage = tag.getInteger("resultDamage");
        isActive = tag.getBoolean("isActive");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("resultID", resultID);
        tag.setInteger("resultDamage", resultDamage);
        tag.setBoolean("isActive", isActive);
    }

    @Override
    public String getInventoryName() {
        return "TESocket";
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    // Logic for the actual block is under here
    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    public void setActive() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public Packet getDescriptionPacket() {
        return NewPacketHandler.getPacket(this);
    }

    public void handlePacketData(int[] intData) {
        if (intData == null) {
            return;
        }

        if (intData.length == 3) {
            if (intData[2] != 0) {
                ItemStack is = new ItemStack(Item.getItemById(intData[0]), intData[2], intData[1]);
                inv[0] = is;
            } else {
                inv[0] = null;
            }
        }
    }

    public int[] buildIntDataList() {
        int[] sortList = new int[3]; // 1 * 3
        int pos = 0;

        for (ItemStack is : inv) {
            if (is != null) {
                sortList[pos++] = Item.getIdFromItem(is.getItem());
                sortList[pos++] = is.getItemDamage();
                sortList[pos++] = is.stackSize;
            } else {
                sortList[pos++] = 0;
                sortList[pos++] = 0;
                sortList[pos++] = 0;
            }
        }

        return sortList;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return itemstack.getItem() instanceof ArmourUpgrade;
    }
}
