package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.RoutingFocusParadigm;
import WayofTime.alchemicalWizardry.api.RoutingFocusPosAndFacing;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.common.items.routing.InputRoutingFocus;
import WayofTime.alchemicalWizardry.common.items.routing.OutputRoutingFocus;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectItemRouting extends RitualEffect {

    private static final Int3[] INPUT_BUFFER_LOCATIONS = { new Int3(1, 0, 0), new Int3(-1, 0, 0), new Int3(0, 0, 1),
            new Int3(0, 0, -1) };

    private static final Int3[] OUTPUT_BUFFER_LOCATIONS = { new Int3(2, 0, 2), new Int3(-2, 0, 2), new Int3(2, 0, -2),
            new Int3(-2, 0, -2) };

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getTotalWorldTime() % 20 != 0) return;

        List<IInventory> outputChestList = findBufferChests(world, x, y, z, OUTPUT_BUFFER_LOCATIONS);
        if (outputChestList.isEmpty()) return;

        List<IInventory> inputFocusChests = findBufferChests(world, x, y, z, INPUT_BUFFER_LOCATIONS);
        if (inputFocusChests.isEmpty()) return;

        for (IInventory outputFocusChests : outputChestList) {
            processOutputFocusInventory(world, outputFocusChests, inputFocusChests);
        }
    }

    private void processOutputFocusInventory(World world, IInventory outputFocusChests,
            List<IInventory> inputFocusChests) {
        RoutingFocusParadigm paradigm = new RoutingFocusParadigm();
        boolean lastItemWasFocus = true;

        for (int i = 0; i < outputFocusChests.getSizeInventory(); i++) {
            ItemStack keyStack = outputFocusChests.getStackInSlot(i);
            if (keyStack == null) continue;

            if (keyStack.getItem() instanceof OutputRoutingFocus outputFocus) {
                if (!lastItemWasFocus) paradigm.clear();
                paradigm.addRoutingFocusPosAndFacing(outputFocus.getPosAndFacing(keyStack));
                paradigm.addLogic(outputFocus.getLogic(keyStack));
                lastItemWasFocus = true;
                continue;
            }
            lastItemWasFocus = false;

            for (RoutingFocusPosAndFacing posAndFacing : paradigm.locationList) {
                if (posAndFacing == null) continue;
                ForgeDirection inputDirection = posAndFacing.facing;
                TileEntity outputChest = world.getTileEntity(
                        posAndFacing.location.xCoord,
                        posAndFacing.location.yCoord,
                        posAndFacing.location.zCoord);

                if (!(outputChest instanceof IInventory outputChestInventory)) continue;

                for (IInventory inputFocusInventory : inputFocusChests) {
                    processInputFocusInventory(
                            world,
                            inputFocusInventory,
                            keyStack,
                            paradigm,
                            outputChestInventory,
                            inputDirection);
                }
            }
        }
    }

    private void processInputFocusInventory(World world, IInventory inputFocusInventory, ItemStack keyStack,
            RoutingFocusParadigm paradigm, IInventory outputChestInventory, ForgeDirection inputDirection) {
        for (int i = 0; i < inputFocusInventory.getSizeInventory(); i++) {
            ItemStack inputFocusStack = inputFocusInventory.getStackInSlot(i);
            if (inputFocusStack == null || !(inputFocusStack.getItem() instanceof InputRoutingFocus inputFocus))
                continue;

            TileEntity inputChest = world.getTileEntity(
                    inputFocus.xCoord(inputFocusStack),
                    inputFocus.yCoord(inputFocusStack),
                    inputFocus.zCoord(inputFocusStack));

            if (!(inputChest instanceof IInventory inputChestInventory)) continue;

            boolean[] canSyphonList = getSyphonableSlots(
                    inputChestInventory,
                    inputFocus.getSetDirection(inputFocusStack));

            for (int j = 0; j < inputChestInventory.getSizeInventory(); j++) {
                if (!canSyphonList[j]) continue;
                ItemStack syphonedStack = inputChestInventory.getStackInSlot(j);
                if (!canExtractItem(inputChestInventory, j, syphonedStack, inputFocus.getSetDirection(inputFocusStack)))
                    continue;

                if (paradigm.doesItemMatch(keyStack, syphonedStack)) {
                    ItemStack newStack = insertStackWithLimit(
                            syphonedStack,
                            outputChestInventory,
                            inputDirection,
                            paradigm.maximumAmount);
                    inputChestInventory.setInventorySlotContents(
                            j,
                            (newStack != null && newStack.stackSize > 0) ? newStack : null);
                }
            }
        }
    }

    private boolean[] getSyphonableSlots(IInventory inventory, ForgeDirection direction) {
        boolean[] result = new boolean[inventory.getSizeInventory()];
        if (inventory instanceof ISidedInventory sidedInv) {
            int[] validSlots = sidedInv.getAccessibleSlotsFromSide(direction.ordinal());
            for (int slot : validSlots) result[slot] = true;
        } else {
            for (int i = 0; i < inventory.getSizeInventory(); i++) result[i] = true;
        }
        return result;
    }

    private boolean canExtractItem(IInventory inventory, int slot, ItemStack stack, ForgeDirection direction) {
        return stack != null && (!(inventory instanceof ISidedInventory)
                || ((ISidedInventory) inventory).canExtractItem(slot, stack, direction.ordinal()));
    }

    private ItemStack insertStackWithLimit(ItemStack stack, IInventory inventory, ForgeDirection direction,
            int maxAmount) {
        return maxAmount <= 0 ? SpellHelper.insertStackIntoInventory(stack, inventory, direction)
                : SpellHelper.insertStackIntoInventory(stack, inventory, direction, maxAmount);
    }

    private List<IInventory> findBufferChests(World world, int x, int y, int z,
            Int3[] locations) {
        List<IInventory> chestList = new ArrayList<>();
        for (Int3 chestLocation : locations) {
            TileEntity tileEntity = world
                    .getTileEntity(x + chestLocation.xCoord, y + chestLocation.yCoord, z + chestLocation.zCoord);
            if (tileEntity instanceof IInventory inv) {
                chestList.add(inv);
            }
        }
        return chestList;
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostPhantomHands[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> itemRoutingRitual = new ArrayList<>();

        this.addCornerRunes(itemRoutingRitual, 1, 0, RitualComponent.BLANK);
        this.addOffsetRunes(itemRoutingRitual, 2, 1, 0, RitualComponent.FIRE);
        this.addParallelRunes(itemRoutingRitual, 4, 0, RitualComponent.WATER);
        this.addParallelRunes(itemRoutingRitual, 5, 0, RitualComponent.EARTH);
        this.addCornerRunes(itemRoutingRitual, 4, 0, RitualComponent.WATER);

        return itemRoutingRitual;
    }
}
