package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistry;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningRegistryComponent;
import WayofTime.alchemicalWizardry.common.IDemon;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.PlinthComponent;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;

public class TEPlinth extends TEInventory {

    public static final int sizeInv = 1;

    private boolean isActive;
    private boolean paradigm;

    private ItemStack[] ring1Inv;
    private ItemStack[] ring2Inv;
    private ItemStack[] ring3Inv;

    private int progressInterval;
    private int progress;

    public static List<PlinthComponent> pedestalPositions = new ArrayList<>();

    public TEPlinth() {
        super(sizeInv);
        this.ring1Inv = new ItemStack[6];
        this.ring2Inv = new ItemStack[6];
        this.ring3Inv = new ItemStack[6];
        isActive = false;
        progress = 0;
        progressInterval = 50;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList ring1TagList = tag.getTagList("ring1Inv", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < ring1TagList.tagCount(); i++) {
            NBTTagCompound items = ring1TagList.getCompoundTagAt(i);
            int slot = items.getByte("Slot");

            if (slot >= 0 && slot < inv.length) {
                ring1Inv[slot] = ItemStack.loadItemStackFromNBT(items);
            }
        }

        NBTTagList ring2TagList = tag.getTagList("ring2Inv", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < ring2TagList.tagCount(); i++) {
            NBTTagCompound items = ring2TagList.getCompoundTagAt(i);
            int slot = items.getByte("Slot");

            if (slot >= 0 && slot < inv.length) {
                ring2Inv[slot] = ItemStack.loadItemStackFromNBT(items);
            }
        }

        NBTTagList ring3TagList = tag.getTagList("ring3Inv", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < ring3TagList.tagCount(); i++) {
            NBTTagCompound items = ring3TagList.getCompoundTagAt(i);
            int slot = items.getByte("Slot");

            if (slot >= 0 && slot < inv.length) {
                ring3Inv[slot] = ItemStack.loadItemStackFromNBT(items);
            }
        }

        progress = tag.getInteger("progress");
        progressInterval = tag.getInteger("progressInterval");
        isActive = tag.getBoolean("isActive");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList ring1ItemList = new NBTTagList();

        for (int i = 0; i < ring1Inv.length; i++) {
            if (ring1Inv[i] != null) {
                NBTTagCompound items = new NBTTagCompound();
                items.setByte("Slot", (byte) i);
                ring1Inv[i].writeToNBT(items);
                ring1ItemList.appendTag(items);
            }
        }

        tag.setTag("ring1Inv", ring1ItemList);
        NBTTagList ring2ItemList = new NBTTagList();

        for (int i = 0; i < ring2Inv.length; i++) {
            if (ring2Inv[i] != null) {
                NBTTagCompound items = new NBTTagCompound();
                items.setByte("Slot", (byte) i);
                ring2Inv[i].writeToNBT(items);
                ring2ItemList.appendTag(items);
            }
        }

        tag.setTag("ring2Inv", ring1ItemList);
        NBTTagList ring3ItemList = new NBTTagList();

        for (int i = 0; i < ring3Inv.length; i++) {
            if (ring3Inv[i] != null) {
                NBTTagCompound items = new NBTTagCompound();
                items.setByte("Slot", (byte) i);
                ring3Inv[i].writeToNBT(items);
                ring3ItemList.appendTag(items);
            }
        }

        tag.setTag("ring3Inv", ring1ItemList);
        tag.setInteger("progress", progress);
        tag.setInteger("progressInterval", progressInterval);
        tag.setBoolean("isActive", isActive);
    }

    @Override
    public String getInventoryName() {
        return "TEPlinth";
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    // Logic for the actual block is under here
    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.isRemote) {
            return;
        }

        if (!isActive()) {
            if (getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof EnergyBattery orb) {
                int bloodOrbLevel = orb.getOrbLevel();

                if (SummoningRegistry.isRecipeValid(
                        bloodOrbLevel,
                        composeItemsForRingAndParadigm(1, true),
                        composeItemsForRingAndParadigm(2, true),
                        composeItemsForRingAndParadigm(3, true))) {
                    SummoningRegistryComponent src = SummoningRegistry.getRegistryComponent(
                            bloodOrbLevel,
                            composeItemsForRingAndParadigm(1, true),
                            composeItemsForRingAndParadigm(2, true),
                            composeItemsForRingAndParadigm(3, true));
                    isActive = true;
                    paradigm = true;
                    progress = 0;
                    if (src != null) {
                        ring1Inv = src.getRingRecipeForRing(1);
                        ring2Inv = src.getRingRecipeForRing(2);
                        ring3Inv = src.getRingRecipeForRing(3);
                    }
                } else if (SummoningRegistry.isRecipeValid(
                        bloodOrbLevel,
                        composeItemsForRingAndParadigm(1, false),
                        composeItemsForRingAndParadigm(2, false),
                        composeItemsForRingAndParadigm(3, false))) {
                            SummoningRegistryComponent src = SummoningRegistry.getRegistryComponent(
                                    bloodOrbLevel,
                                    composeItemsForRingAndParadigm(1, false),
                                    composeItemsForRingAndParadigm(2, false),
                                    composeItemsForRingAndParadigm(3, false));
                            isActive = true;
                            paradigm = false;
                            progress = 0;
                            if (src != null) {
                                ring1Inv = src.getRingRecipeForRing(1);
                                ring2Inv = src.getRingRecipeForRing(2);
                                ring3Inv = src.getRingRecipeForRing(3);
                            }
                        } else {
                            isActive = false;
                            progress = 0;
                        }
            }
        } else {
            if (getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof EnergyBattery orb) {
                if (progress % progressInterval == 0) {
                    int ring = (progress / progressInterval) / 6 + 1;
                    int slot = (progress / progressInterval) % 6;
                    ItemStack itemStack = switch (ring) {
                        case 1 -> this.ring1Inv[slot];
                        case 2 -> this.ring2Inv[slot];
                        case 3 -> this.ring3Inv[slot];
                        default -> null;
                    };

                    if (itemStack == null) {
                        progress += progressInterval;
                    } else {
                        if (this.deleteItemStackInRing(ring, itemStack)) {
                            progress++;
                        }
                    }
                } else {
                    progress++;
                }

                if (progress >= progressInterval * 18) {
                    int bloodOrbLevel = orb.getOrbLevel();
                    EntityLivingBase entity = SummoningRegistry
                            .getEntity(worldObj, bloodOrbLevel, ring1Inv, ring2Inv, ring3Inv);

                    if (entity != null) {
                        entity.setPosition(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
                        worldObj.spawnEntityInWorld(entity);

                        if (entity instanceof IDemon) {
                            ((IDemon) entity).setSummonedConditions();
                        }

                        worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 3, false);
                        // deleteItemsInRing(1);
                        isActive = false;
                        progress = 0;

                        if (worldObj != null) {
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }
                    }
                }
            }
        }
    }

    public boolean deleteItemStackInRing(int ring, ItemStack itemStack) {
        if (itemStack == null) {
            return true;
        }

        int i = 0;
        if (paradigm) {

            for (PlinthComponent pc : pedestalPositions) {
                if (i < 6 && pc.getRing() == ring) {
                    TileEntity tileEntity = worldObj
                            .getTileEntity(xCoord + pc.xOffset, yCoord + pc.yOffset, zCoord + pc.zOffset);

                    if (tileEntity instanceof TEPedestal) {
                        ItemStack possibleItem = ((TEPedestal) tileEntity).getStackInSlot(0);

                        if (possibleItem == null) {
                            i++;
                            continue;
                        }

                        boolean test = false;

                        if (possibleItem.getItem() instanceof ItemBlock) {
                            if (itemStack.getItem() instanceof ItemBlock) {
                                test = true;
                            }
                        } else if (!(itemStack.getItem() instanceof ItemBlock)) {
                            test = true;
                        }

                        if (test) {
                            if (itemStack.getItem() == possibleItem.getItem()
                                    && (itemStack.getItemDamage() == possibleItem.getItemDamage()
                                            || itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                                ((TEPedestal) tileEntity).decrStackSize(0, 1);
                                if (((TEPedestal) tileEntity).getStackInSlot(0) != null
                                        && ((TEPedestal) tileEntity).getStackInSlot(0).stackSize == 0) {
                                    ((TEPedestal) tileEntity).setInventorySlotContents(0, null);
                                }
                                ((TEPedestal) tileEntity).onItemDeletion();
                                worldObj.markBlockForUpdate(
                                        xCoord + pc.xOffset,
                                        yCoord + pc.yOffset,
                                        zCoord + pc.zOffset);
                                return true;
                            }
                        }

                        i++;
                    }
                }
            }
        } else {

            for (PlinthComponent pc : pedestalPositions) {
                if (i < 6 && pc.getRing() == ring) {
                    TileEntity tileEntity = worldObj
                            .getTileEntity(xCoord + pc.zOffset, yCoord + pc.yOffset, zCoord + pc.xOffset);

                    if (tileEntity instanceof TEPedestal) {
                        ItemStack possibleItem = ((TEPedestal) tileEntity).getStackInSlot(0);

                        if (possibleItem == null) {
                            i++;
                            continue;
                        }

                        boolean test = false;

                        if (possibleItem.getItem() instanceof ItemBlock) {
                            if (itemStack.getItem() instanceof ItemBlock) {
                                test = true;
                            }
                        } else if (!(itemStack.getItem() instanceof ItemBlock)) {
                            test = true;
                        }

                        if (test) {
                            if (itemStack.getItem() == possibleItem.getItem()
                                    && (itemStack.getItemDamage() == possibleItem.getItemDamage()
                                            || itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                                ((TEPedestal) tileEntity).decrStackSize(0, 1);
                                ((TEPedestal) tileEntity).onItemDeletion();
                                worldObj.markBlockForUpdate(
                                        xCoord + pc.zOffset,
                                        yCoord + pc.yOffset,
                                        zCoord + pc.zOffset);
                                return true;
                            }
                        }

                        i++;
                    }
                }
            }
        }

        return false;
    }

    public ItemStack[] composeItemsForRingAndParadigm(int ring, boolean paradigm) {
        ItemStack[] composed = new ItemStack[6];

        int i = 0;
        if (paradigm) {

            for (PlinthComponent pc : pedestalPositions) {
                if (i < 6 && pc.getRing() == ring) {
                    TileEntity tileEntity = worldObj
                            .getTileEntity(xCoord + pc.xOffset, yCoord + pc.yOffset, zCoord + pc.zOffset);

                    if (tileEntity instanceof TEPedestal) {
                        composed[i] = ((TEPedestal) tileEntity).getStackInSlot(0);
                        i++;
                    }
                }
            }
        } else {

            for (PlinthComponent pc : pedestalPositions) {
                if (i < 6 && pc.getRing() == ring) {
                    TileEntity tileEntity = worldObj
                            .getTileEntity(xCoord + pc.zOffset, yCoord + pc.yOffset, zCoord + pc.xOffset);

                    if (tileEntity instanceof TEPedestal) {
                        composed[i] = ((TEPedestal) tileEntity).getStackInSlot(0);
                        i++;
                    }
                }
            }
        }

        return composed;
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
        int[] sortList = new int[3];
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
        return slot == 0;
    }

    public static void initialize() {
        pedestalPositions.add(new PlinthComponent(1, 0, -2, 1));
        pedestalPositions.add(new PlinthComponent(2, 0, 0, 1));
        pedestalPositions.add(new PlinthComponent(1, 0, +2, 1));
        pedestalPositions.add(new PlinthComponent(-1, 0, -2, 1));
        pedestalPositions.add(new PlinthComponent(-2, 0, 0, 1));
        pedestalPositions.add(new PlinthComponent(-1, 0, +2, 1));
        pedestalPositions.add(new PlinthComponent(3, 1, -5, 2));
        pedestalPositions.add(new PlinthComponent(6, 1, 0, 2));
        pedestalPositions.add(new PlinthComponent(3, 1, +5, 2));
        pedestalPositions.add(new PlinthComponent(-3, 1, -5, 2));
        pedestalPositions.add(new PlinthComponent(-6, 1, 0, 2));
        pedestalPositions.add(new PlinthComponent(-3, 1, +5, 2));
        pedestalPositions.add(new PlinthComponent(0, 2, -9, 3));
        pedestalPositions.add(new PlinthComponent(7, 2, -4, 3));
        pedestalPositions.add(new PlinthComponent(7, 2, +4, 3));
        pedestalPositions.add(new PlinthComponent(0, 2, +9, 3));
        pedestalPositions.add(new PlinthComponent(-7, 2, -4, 3));
        pedestalPositions.add(new PlinthComponent(-7, 2, 4, 3));
    }
}
