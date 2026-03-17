package WayofTime.alchemicalWizardry.common.items.energy;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAttunedCrystal extends Item implements IReagentManipulator {

    public static final int maxDistance = 6;

    public IIcon crystalBody;
    public IIcon crystalLabel;

    public ItemAttunedCrystal() {
        super();
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.hasSubtypes = true;
        this.maxStackSize = 1;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Reagent reagent = this.getReagent(stack);

        String name = super.getItemStackDisplayName(stack);

        if (reagent != null) {
            name = name + " (" + reagent.name() + ")";
        }

        return name;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc2"));
        tooltip.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc3"));
        tooltip.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc4"));
        tooltip.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc5"));

        if (item.getTagCompound() == null) {
            return;
        }
        Reagent reagent = this.getReagent(item);
        if (reagent != null) {
            tooltip.add(StatCollector.translateToLocal("tooltip.reagent.selectedreagent") + " " + reagent.name());
        }

        if (this.getHasSavedCoordinates(item)) {
            tooltip.add("");
            Int3 coords = this.getCoordinates(item);
            tooltip.add(
                    StatCollector.translateToLocal(
                            "tooltip.alchemy.coords") + " " + coords.x() + ", " + coords.y() + ", " + coords.z());
            tooltip.add(StatCollector.translateToLocal("tooltip.alchemy.dimension") + " " + getDimension(item));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.crystalBody = iconRegister.registerIcon("AlchemicalWizardry:AttunedCrystal1");
        this.crystalLabel = iconRegister.registerIcon("AlchemicalWizardry:AttunedCrystal2");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        switch (pass) {
            case 0:
                return 256 * (256 * 255 + 255) + 255;
            case 1:
                Reagent reagent = this.getReagent(stack);
                if (reagent != null) {
                    return (reagent.red() * 256 * 256 + reagent.green() * 256 + reagent.blue());
                }
                break;
        }

        return 256 * (256 * 255 + 255) + 255;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        return switch (pass) {
            case 0 -> this.crystalBody;
            case 1 -> this.crystalLabel;
            default -> this.itemIcon;
        };
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return itemStack;
        }

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null) {
            if (player.isSneaking()) {
                this.setHasSavedCoordinates(itemStack, false);
                if (!itemStack.hasTagCompound()) {
                    itemStack.setTagCompound(new NBTTagCompound());
                }
                itemStack.getTagCompound().setString("reagent", "");
                player.addChatComponentMessage(new ChatComponentTranslation("message.attunedcrystal.clearing"));
            }

            return itemStack;
        }

        if (movingobjectposition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return itemStack;
        }

        int x = movingobjectposition.blockX;
        int y = movingobjectposition.blockY;
        int z = movingobjectposition.blockZ;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof IReagentHandler relay)) {
            return itemStack;
        }

        if (player.isSneaking()) {
            if (this.getHasSavedCoordinates(itemStack)) {
                Int3 coords = this.getCoordinates(itemStack);
                int dimension = this.getDimension(itemStack);

                if (coords == null) {
                    return itemStack;
                }

                // Sneak+right-click saved source block again -> clear all of reagent's connections
                if (coords.x() == x && coords.y() == y && coords.z() == z) {
                    TileEntity pastTile = world.getTileEntity(coords.x(), coords.y(), coords.z());
                    if (!(pastTile instanceof TEReagentConduit pastRelay)) {
                        player.addChatComponentMessage(
                                new ChatComponentTranslation("message.attunedcrystal.error.cannotfind"));
                        return itemStack;
                    }
                    Reagent reagent = this.getReagent(itemStack);
                    if (reagent == null) {
                        player.addChatComponentMessage(
                                new ChatComponentTranslation("message.attunedcrystal.error.noreagent"));
                        return itemStack;
                    }
                    pastRelay.reagentTargetList.remove(reagent);
                    this.setHasSavedCoordinates(itemStack, false);
                    player.addChatComponentMessage(
                            new ChatComponentTranslation("message.attunedcrystal.clearedreagent", reagent.name()));
                    world.markBlockForUpdate(coords.x(), coords.y(), coords.z());
                    return itemStack;
                }

                if (dimension != world.provider.dimensionId || Math.abs(coords.x() - x) > maxDistance
                        || Math.abs(coords.y() - y) > maxDistance
                        || Math.abs(coords.z() - z) > maxDistance) {
                    player.addChatComponentMessage(
                            new ChatComponentTranslation("message.attunedcrystal.error.toofar"));
                    return itemStack;
                }

                TileEntity pastTile = world.getTileEntity(coords.x(), coords.y(), coords.z());
                if (!(pastTile instanceof TEReagentConduit pastRelay)) {
                    player.addChatComponentMessage(
                            new ChatComponentTranslation("message.attunedcrystal.error.cannotfind"));
                    return itemStack;
                }

                Reagent reagent = this.getReagent(itemStack);
                if (reagent == null) {
                    player.addChatComponentMessage(
                            new ChatComponentTranslation("message.attunedcrystal.error.noreagent"));
                    return itemStack;
                }

                boolean removed = pastRelay.removeReagentDestinationViaActual(reagent, x, y, z);
                player.addChatComponentMessage(
                        new ChatComponentTranslation(
                                removed ? "message.attunedcrystal.removed"
                                        : "message.attunedcrystal.error.notremoved"));
                world.markBlockForUpdate(coords.x(), coords.y(), coords.z());
                return itemStack;
            }

            // No saved coords -> cycle reagent selection
            ReagentContainerInfo[] infos = relay.getContainerInfo(ForgeDirection.UNKNOWN);
            if (infos != null) {
                List<Reagent> reagentList = getReagents(infos);
                if (reagentList.isEmpty()) {
                    return itemStack;
                }
                Reagent pastReagent = this.getReagent(itemStack);
                int reagentLocation = reagentList.indexOf(pastReagent);
                if (reagentLocation == -1 || reagentLocation + 1 >= reagentList.size()) {
                    this.setReagentWithNotification(itemStack, reagentList.getFirst(), player);
                } else {
                    this.setReagentWithNotification(itemStack, reagentList.get(reagentLocation + 1), player);
                }
            }
            return itemStack;
        }

        // Not sneaking
        if (this.getHasSavedCoordinates(itemStack)) {
            Int3 coords = this.getCoordinates(itemStack);
            int dimension = this.getDimension(itemStack);

            if (coords == null) {
                return itemStack;
            }

            if (dimension != world.provider.dimensionId || Math.abs(coords.x() - x) > maxDistance
                    || Math.abs(coords.y() - y) > maxDistance
                    || Math.abs(coords.z() - z) > maxDistance) {
                player.addChatComponentMessage(new ChatComponentTranslation("message.attunedcrystal.error.toofar"));
                return itemStack;
            }

            TileEntity pastTile = world.getTileEntity(coords.x(), coords.y(), coords.z());
            if (!(pastTile instanceof TEReagentConduit pastRelay)) {
                player.addChatComponentMessage(
                        new ChatComponentTranslation("message.attunedcrystal.error.cannotfind"));
                return itemStack;
            }

            Reagent reagent = this.getReagent(itemStack);
            if (reagent == null) {
                player.addChatComponentMessage(
                        new ChatComponentTranslation("message.attunedcrystal.error.noreagent"));
                return itemStack;
            }

            if (pastRelay.hasReagentDestination(reagent, x, y, z)) {
                player.addChatComponentMessage(
                        new ChatComponentTranslation("message.attunedcrystal.error.duplicate"));
            } else if (pastRelay.addReagentDestinationViaActual(reagent, x, y, z)) {
                int used = pastRelay.getTotalConnections();
                int max = pastRelay.maxConnextions;
                player.addChatComponentMessage(
                        new ChatComponentTranslation("message.attunedcrystal.linked", reagent.name(), used, max));
            } else {
                player.addChatComponentMessage(
                        new ChatComponentTranslation("message.attunedcrystal.error.noconnections"));
            }
            world.markBlockForUpdate(coords.x(), coords.y(), coords.z());
        } else {
            int dimension = world.provider.dimensionId;
            this.setDimension(itemStack, dimension);
            this.setCoordinates(itemStack, new Int3(x, y, z));

            player.addChatComponentMessage(
                    new ChatComponentTranslation("message.attunedcrystal.linking", x, y, z));
        }

        return itemStack;
    }

    public static List<Reagent> getReagents(ReagentContainerInfo[] infos) {
        List<Reagent> reagentList = new LinkedList<>();
        for (ReagentContainerInfo info : infos) {
            if (info != null) {
                ReagentStack reagentStack = info.reagent;
                if (reagentStack != null) {
                    Reagent reagent = reagentStack.reagent;
                    if (reagent != null) {
                        reagentList.add(reagent);
                    }
                }
            }
        }
        return reagentList;
    }

    public void setCoordinates(ItemStack stack, Int3 coords) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        coords.writeToNBT(tag);

        this.setHasSavedCoordinates(stack, true);
    }

    public void setDimension(ItemStack stack, int dimension) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setInteger("dimension", dimension);
    }

    public Int3 getCoordinates(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        return Int3.readFromNBT(tag);
    }

    public int getDimension(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        return tag.getInteger("dimension");
    }

    public void setHasSavedCoordinates(ItemStack stack, boolean flag) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setBoolean("hasSavedCoordinates", flag);
    }

    public boolean getHasSavedCoordinates(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        return tag.getBoolean("hasSavedCoordinates");
    }

    public Reagent getReagent(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        return ReagentRegistry.getReagentForKey(tag.getString("reagent"));
    }

    public void setReagent(ItemStack stack, Reagent reagent) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.getTagCompound();

        tag.setString("reagent", ReagentRegistry.getKeyForReagent(reagent));
    }

    public void setReagentWithNotification(ItemStack stack, Reagent reagent, EntityPlayer player) {
        this.setReagent(stack, reagent);

        if (reagent != null) {
            player.addChatComponentMessage(
                    new ChatComponentText(
                            StatCollector.translateToLocal("message.attunedcrystal.setto") + " " + reagent.name()));
        }
    }
}
