package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IRitualDiviner;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.IRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRitualDiviner extends EnergyItems implements IRitualDiviner {

    private final int maxMetaData;

    public ItemRitualDiviner() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(100);
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.maxMetaData = 4;
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:RitualDiviner");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.ritualdiviner.desc"));

        if (this.getMaxRuneDisplacement(stack) == 1) {
            tooltip.add(StatCollector.translateToLocal("tooltip.ritualdiviner.canplace"));
        } else if (this.getMaxRuneDisplacement(stack) >= 2) {
            tooltip.add(StatCollector.translateToLocal("tooltip.ritualdiviner.canplacedawn"));

        } else {
            tooltip.add(StatCollector.translateToLocal("tooltip.ritualdiviner.cannotplace"));
        }

        tooltip.add(
                StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " "
                        + this.getNameForDirection(this.getDirection(stack)));

        boolean sneaking = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (sneaking) {
            if (!(stack.getTagCompound() == null)) {
                String ritualID = this.getCurrentRitual(stack);
                // TODO
                addBindingInformation(stack, tooltip);
                tooltip.add(StatCollector.translateToLocal("tooltip.alchemy.ritualid") + " " + ritualID);
                List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(stack));
                if (ritualList == null) {
                    return;
                }

                int blankStones = 0;
                int airStones = 0;
                int waterStones = 0;
                int fireStones = 0;
                int earthStones = 0;
                int duskStones = 0;
                int dawnStones = 0;

                for (RitualComponent rc : ritualList) {
                    switch (rc.stoneType()) {
                        case RitualComponent.BLANK -> blankStones++;
                        case RitualComponent.AIR -> airStones++;
                        case RitualComponent.WATER -> waterStones++;
                        case RitualComponent.FIRE -> fireStones++;
                        case RitualComponent.EARTH -> earthStones++;
                        case RitualComponent.DUSK -> duskStones++;
                        case RitualComponent.DAWN -> dawnStones++;
                    }
                }

                int totalStones = blankStones + airStones
                        + waterStones
                        + fireStones
                        + earthStones
                        + duskStones
                        + dawnStones;

                tooltip.add(
                        EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.ritualdiviner.blankstones")
                                + " "
                                + blankStones);
                tooltip.add(
                        EnumChatFormatting.AQUA + StatCollector.translateToLocal("tooltip.ritualdiviner.airstones")
                                + " "
                                + airStones);
                tooltip.add(
                        EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.ritualdiviner.waterstones")
                                + " "
                                + waterStones);
                tooltip.add(
                        EnumChatFormatting.RED + StatCollector.translateToLocal("tooltip.ritualdiviner.firestones")
                                + " "
                                + fireStones);
                tooltip.add(
                        EnumChatFormatting.DARK_GREEN
                                + StatCollector.translateToLocal("tooltip.ritualdiviner.earthstones")
                                + " "
                                + earthStones);
                tooltip.add(
                        EnumChatFormatting.DARK_PURPLE
                                + StatCollector.translateToLocal("tooltip.ritualdiviner.duskstones")
                                + " "
                                + duskStones);
                tooltip.add(
                        EnumChatFormatting.GOLD + StatCollector.translateToLocal("tooltip.ritualdiviner.dawnstones")
                                + " "
                                + dawnStones);
                tooltip.add(
                        EnumChatFormatting.UNDERLINE
                                + StatCollector.translateToLocal("tooltip.ritualdiviner.totalStones")
                                + " "
                                + totalStones);
            }
        } else {
            tooltip.add(
                    EnumChatFormatting.AQUA + "-"
                            + StatCollector.translateToLocal("tooltip.ritualdiviner.moreinfo")
                            + "-");
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return super.getItemStackDisplayName(stack);
        }
        String ritualID = this.getCurrentRitual(stack);
        if (ritualID.isEmpty()) {
            return super.getItemStackDisplayName(stack);
        }
        return StatCollector
                .translateToLocalFormatted("bm.string.ritualDiviner", Rituals.getLocalizedNameOfRitual(ritualID));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7,
            float par8, float par9, float par10) {
        if (!IBindable.checkAndSetItemOwner(stack, player)) return false;

        if (placeRitualStoneAtMasterStone(stack, player, world, x, y, z)) {
            this.setStoredLocation(stack, new Int3(x, y, z));
            return true;
        } else if (!(world.getBlock(x, y, z) instanceof IRitualStone
                || world.getBlock(x, y, z) instanceof IMasterRitualStone) && !player.isSneaking()) {
                    if (world.isRemote) {
                        return false;
                    }
                    this.cycleDirection(stack);
                    player.addChatComponentMessage(
                            new ChatComponentText(
                                    StatCollector.translateToLocal("tooltip.ritualdiviner.ritualtunedto") + " "
                                            + this.getNameForDirection(this.getDirection(stack))));
                    return true;
                }

        return false;
    }

    public boolean placeRitualStoneAtMasterStone(ItemStack stack, EntityPlayer player, World world, int x, int y,
            int z) {
        int direction = this.getDirection(stack);

        ItemStack[] playerInventory = player.inventory.mainInventory;
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TEMasterStone) {
            List<RitualComponent> ritualList = Rituals.getRitualList(this.getCurrentRitual(stack));
            if (ritualList == null) {
                return false;
            }

            int playerInvRitualStoneLocation = -1;

            for (int i = 0; i < playerInventory.length; i++) {
                if (playerInventory[i] == null) {
                    continue;
                }

                if (new ItemStack(ModBlocks.ritualStone).isItemEqual(playerInventory[i])) {
                    playerInvRitualStoneLocation = i;
                    break;
                }
            }

            for (RitualComponent rc : ritualList) {
                if (world.isAirBlock(x + rc.getX(direction), y + rc.y(), z + rc.getZ(direction))) {
                    if (playerInvRitualStoneLocation >= 0 || player.capabilities.isCreativeMode) {
                        if (rc.stoneType() > this.maxMetaData + this.getMaxRuneDisplacement(stack)) {
                            world.playAuxSFX(200, x, y + 1, z, 0);
                            return true;
                        }

                        if (!player.capabilities.isCreativeMode) {
                            player.inventory.decrStackSize(playerInvRitualStoneLocation, 1);
                        }

                        if (EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
                            world.setBlock(
                                    x + rc.getX(direction),
                                    y + rc.y(),
                                    z + rc.getZ(direction),
                                    ModBlocks.ritualStone,
                                    rc.stoneType(),
                                    3);

                            if (world.isRemote) {
                                world.playAuxSFX(2005, x, y + 1, z, 0);

                                return true;
                            }
                        }

                        return true;
                    }
                } else {
                    Block block = world.getBlock(x + rc.getX(direction), y + rc.y(), z + rc.getZ(direction));

                    if (block == ModBlocks.ritualStone) {
                        int metadata = world
                                .getBlockMetadata(x + rc.getX(direction), y + rc.y(), z + rc.getZ(direction));

                        if (metadata != rc.stoneType() && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
                            if (rc.stoneType() > this.maxMetaData + this.getMaxRuneDisplacement(stack)) {
                                world.playAuxSFX(200, x, y + 1, z, 0);
                                return true;
                            }

                            world.setBlockMetadataWithNotify(
                                    x + rc.getX(direction),
                                    y + rc.y(),
                                    z + rc.getZ(direction),
                                    rc.stoneType(),
                                    3);
                            return true;
                        }
                    } else {
                        world.playAuxSFX(0, x, y + 1, z, 0);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        if (entity instanceof EntityPlayer && hasStoredLocation(stack) && world.getTotalWorldTime() % 5 == 0) {
            Int3 loc = getStoredLocation(stack);

            int x = loc.xCoord;
            int y = loc.yCoord;
            int z = loc.zCoord;

            if (!this.placeRitualStoneAtMasterStone(stack, (EntityPlayer) entity, world, x, y, z)) {
                this.voidStoredLocation(stack);
            }
        }
    }

    public void setStoredLocation(ItemStack stack, Int3 location) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        NBTTagCompound locTag = location.writeToNBT(new NBTTagCompound());
        locTag.setBoolean("isStored", true);

        tag.setTag("location", locTag);
    }

    public void voidStoredLocation(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("location")) {
            this.setStoredLocation(stack, new Int3(0, 0, 0));
            return;
        }

        NBTTagCompound locTag = (NBTTagCompound) tag.getTag("location");
        if (locTag != null) {
            locTag.setBoolean("isStored", false);
        }
    }

    public Int3 getStoredLocation(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("location")) {
            return new Int3(0, 0, 0);
        }

        NBTTagCompound locTag = (NBTTagCompound) tag.getTag("location");

        return Int3.readFromNBT(locTag);
    }

    public boolean hasStoredLocation(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("location")) {
            return false;
        }

        NBTTagCompound locTag = (NBTTagCompound) tag.getTag("location");

        return locTag.getBoolean("isStored");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(stack, player) && player.isSneaking()) {
            rotateRituals(world, player, stack, true);
        }

        return stack;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer player) {
            if (!IBindable.checkAndSetItemOwner(stack, player)) return true;
            if (!player.isSwingInProgress && player.isSneaking()) {
                rotateRituals(player.worldObj, player, stack, false);
            }
        }

        return false;
    }

    public void rotateRituals(World world, EntityPlayer player, ItemStack stack, boolean next) {
        this.voidStoredLocation(stack);
        String currentRitualID = this.getCurrentRitual(stack);

        this.setCurrentRitual(
                stack,
                next ? Rituals.getNextRitualKey(currentRitualID) : Rituals.getPreviousRitualKey(currentRitualID));

        if (world.isRemote) {
            IChatComponent chatmessagecomponent = new ChatComponentText(
                    StatCollector.translateToLocal("message.ritual.currentritual") + " "
                            + Rituals.getLocalizedNameOfRitual(this.getCurrentRitual(stack)));
            player.addChatComponentMessage(chatmessagecomponent);
        }
    }

    @Override
    public String getCurrentRitual(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }

        return stack.getTagCompound().getString("ritualID");
    }

    @Override
    public void setCurrentRitual(ItemStack stack, String ritualID) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setString("ritualID", ritualID);
    }

    /**
     * @return 0 if it can place the 4 elemental runes, 1 if it can also place dusk, 2 if it can also place dawn
     */
    @Override
    public int getMaxRuneDisplacement(ItemStack stack) {
        return stack.getItemDamage();
    }

    /**
     * @param displacement 0 if it can place the 4 elemental runes, 1 if it can also place dusk, 2 if it can also place
     *                     dawn
     */
    @Override
    public void setMaxRuneDisplacement(ItemStack stack, int displacement) {
        stack.setItemDamage(displacement);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list) {
        list.add(new ItemStack(id));

        ItemStack duskRitualDivinerStack = new ItemStack(id);
        this.setMaxRuneDisplacement(duskRitualDivinerStack, 1);
        list.add(duskRitualDivinerStack);

        ItemStack dawnRitualDivinerStack = new ItemStack(id);
        this.setMaxRuneDisplacement(dawnRitualDivinerStack, 2);
        list.add(dawnRitualDivinerStack);
    }

    @Override
    public int getDirection(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getInteger("direction");
    }

    @Override
    public void setDirection(ItemStack itemStack, int direction) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setInteger("direction", direction);
    }

    @Override
    public int cycleDirection(ItemStack itemStack) {
        this.voidStoredLocation(itemStack);
        int direction = this.getDirection(itemStack);

        if (direction < 4) {
            direction = Math.max(1, direction + 1);
        } else {
            direction = 1;
        }

        this.setDirection(itemStack, direction);

        return direction;
    }

    @Override
    public String getNameForDirection(int direction) {
        return switch (direction) {
            case 1 -> StatCollector.translateToLocal("message.ritual.side.north");
            case 2 -> StatCollector.translateToLocal("message.ritual.side.east");
            case 3 -> StatCollector.translateToLocal("message.ritual.side.south");
            case 4 -> StatCollector.translateToLocal("message.ritual.side.west");
            default -> "";
        };
    }
}
