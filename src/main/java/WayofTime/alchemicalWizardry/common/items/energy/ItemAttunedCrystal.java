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
            name = name + " (" + reagent.name + ")";
        }

        return name;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.attunedcrystal.desc2"));

        if (!(par1ItemStack.getTagCompound() == null)) {
            Reagent reagent = this.getReagent(par1ItemStack);
            if (reagent != null) {
                par3List.add(StatCollector.translateToLocal("tooltip.reagent.selectedreagent") + " " + reagent.name);
            }

            if (this.getHasSavedCoordinates(par1ItemStack)) {
                par3List.add("");
                Int3 coords = this.getCoordinates(par1ItemStack);
                par3List.add(
                        StatCollector.translateToLocal("tooltip.alchemy.coords") + " "
                                + coords.xCoord
                                + ", "
                                + coords.yCoord
                                + ", "
                                + coords.zCoord);
                par3List.add(
                        StatCollector.translateToLocal("tooltip.alchemy.dimension") + " "
                                + getDimension(par1ItemStack));
            }
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
                    return (reagent.getColourRed() * 256 * 256 + reagent.getColourGreen() * 256
                            + reagent.getColourBlue());
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
                player.addChatComponentMessage(new ChatComponentTranslation("message.attunedcrystal.clearing"));
            }

            return itemStack;
        } else {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                TileEntity tile = world.getTileEntity(x, y, z);

                if (!(tile instanceof IReagentHandler)) {
                    return itemStack;
                }

                IReagentHandler relay = (IReagentHandler) tile;

                if (player.isSneaking()) {
                    ReagentContainerInfo[] infos = relay.getContainerInfo(ForgeDirection.UNKNOWN);
                    if (infos != null) {
                        List<Reagent> reagentList = new LinkedList();
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

                        Reagent pastReagent = this.getReagent(itemStack);

                        if (reagentList.size() <= 0) {
                            return itemStack;
                        }

                        int reagentLocation;

                        reagentLocation = reagentList.indexOf(pastReagent);

                        if (reagentLocation == -1 || reagentLocation + 1 >= reagentList.size()) {
                            this.setReagentWithNotification(itemStack, reagentList.get(0), player);
                        } else {
                            this.setReagentWithNotification(itemStack, reagentList.get(reagentLocation + 1), player);
                        }
                    }
                } else {
                    if (this.getHasSavedCoordinates(itemStack)) {
                        Int3 coords = this.getCoordinates(itemStack);
                        int dimension = this.getDimension(itemStack);

                        if (coords == null) {
                            return itemStack;
                        }

                        if (dimension != world.provider.dimensionId || Math.abs(coords.xCoord - x) > maxDistance
                                || Math.abs(coords.yCoord - y) > maxDistance
                                || Math.abs(coords.zCoord - z) > maxDistance) {
                            player.addChatComponentMessage(
                                    new ChatComponentTranslation("message.attunedcrystal.error.toofar"));
                            return itemStack;
                        }

                        TileEntity pastTile = world.getTileEntity(coords.xCoord, coords.yCoord, coords.zCoord);
                        if (!(pastTile instanceof TEReagentConduit)) {
                            player.addChatComponentMessage(
                                    new ChatComponentTranslation("message.attunedcrystal.error.cannotfind"));
                            return itemStack;
                        }

                        Reagent reagent = this.getReagent(itemStack);

                        if (reagent == null) {
                            return itemStack;
                        }

                        TEReagentConduit pastRelay = (TEReagentConduit) pastTile;

                        if (player.isSneaking()) {
                            pastRelay.removeReagentDestinationViaActual(reagent, x, y, z);
                        } else {
                            if (pastRelay.addReagentDestinationViaActual(reagent, x, y, z)) {
                                player.addChatComponentMessage(
                                        new ChatComponentText(
                                                StatCollector.translateToLocal("message.attunedcrystal.linked") + " "
                                                        + reagent.name));
                            } else {
                                player.addChatComponentMessage(
                                        new ChatComponentTranslation("message.attunedcrystal.error.noconnections"));
                            }
                        }
                        world.markBlockForUpdate(coords.xCoord, coords.yCoord, coords.zCoord);
                    } else {
                        int dimension = world.provider.dimensionId;

                        this.setDimension(itemStack, dimension);
                        this.setCoordinates(itemStack, new Int3(x, y, z));

                        player.addChatComponentMessage(new ChatComponentTranslation("message.attunedcrystal.linking"));
                    }
                }
            }
        }

        return itemStack;
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
                            StatCollector.translateToLocal("message.attunedcrystal.setto") + " " + reagent.name));
        }
    }
}
