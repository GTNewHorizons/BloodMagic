package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.compat.BloodMagicWailaPlugin;
import WayofTime.alchemicalWizardry.compat.IBloodMagicWailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TETeleposer extends TEInventory implements IBloodMagicWailaProvider {

    public static final int sizeInv = 1;

    private int resultID;
    private int resultDamage;

    public boolean hasRedstone;

    public TETeleposer() {
        super(sizeInv);
        resultID = 0;
        resultDamage = 0;
        hasRedstone = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        resultID = par1NBTTagCompound.getInteger("resultID");
        resultDamage = par1NBTTagCompound.getInteger("resultDamage");
        hasRedstone = par1NBTTagCompound.getBoolean("hasRedstone");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("resultID", resultID);
        par1NBTTagCompound.setInteger("resultDamage", resultDamage);
        par1NBTTagCompound.setBoolean("hasRedstone", hasRedstone);
    }

    @Override
    public String getInventoryName() {
        return "TETeleposer";
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public void activate() {
        if (worldObj.isRemote) return;

        ItemStack focus = this.getStackInSlot(0);

        if (focus == null || !(focus.getItem() instanceof TelepositionFocus focusItem)) {
            return;
        }

        World targetWorld = focusItem.getWorld(focus);
        if (targetWorld == null) return;

        int x = focusItem.xCoord(focus);
        int y = focusItem.yCoord(focus);
        int z = focusItem.zCoord(focus);

        if (!(targetWorld.getTileEntity(x, y, z) instanceof TETeleposer target) || target == this) {
            return;
        }

        int damage = (int) (0.5f
                * Math.sqrt(Math.pow(xCoord - x, 2) + Math.pow(yCoord - y + 1, 2) + Math.pow(zCoord - z, 2)));

        int size = focusItem.getFocusLevel();

        List<EntityLivingBase> localEntities = getEntitiesInArea(worldObj, xCoord, yCoord, zCoord, size);
        List<EntityLivingBase> targetEntities = getEntitiesInArea(targetWorld, x, y, z, size);

        int entityCount = localEntities.size() + targetEntities.size();

        if (!EnergyItems.canSyphonInContainer(focus, damage * entityCount)) return;

        int movedBlocks = moveBlocks(size, targetWorld, x, y, z);

        int cost = Math.max(damage * movedBlocks + damage * entityCount, 2000);

        SoulNetworkHandler.syphonFromNetwork(focus, cost);

        if (targetWorld.equals(worldObj)) {
            teleportEntitiesSameWorld(localEntities, this.xCoord, this.yCoord, this.zCoord, x, y, z);
            teleportEntitiesSameWorld(targetEntities, x, y, z, this.xCoord, this.yCoord, this.zCoord);
        } else {
            teleportEntitiesToOtherWorld(
                    localEntities,
                    worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord,
                    targetWorld,
                    x,
                    y,
                    z);
            teleportEntitiesToOtherWorld(
                    targetEntities,
                    targetWorld,
                    x,
                    y,
                    z,
                    worldObj,
                    this.xCoord,
                    this.yCoord,
                    this.zCoord);

        }
    }

    private List<EntityLivingBase> getEntitiesInArea(World world, int x, int y, int z, int size) {
        double half = (size * 2 - 1) / 2.0;

        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
                x + 0.5 - half,
                y + 1,
                z + 0.5 - half,
                x + 0.5 + half,
                y + 1 + (size * 2 - 1),
                z + 0.5 + half);

        return world.getEntitiesWithinAABB(EntityLivingBase.class, box);
    }

    private int moveBlocks(int focusLevel, World targetWorld, int x, int y, int z) {
        int transportCount = 0;
        int range = focusLevel - 1;

        for (int k = 0; k <= (focusLevel * 2 - 2); k++) {
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    if (BlockTeleposer.swapBlocks(
                            this,
                            worldObj,
                            targetWorld,
                            xCoord + i,
                            yCoord + 1 + k,
                            zCoord + j,
                            x + i,
                            y + 1 + k,
                            z + j)) {
                        transportCount++;
                    }
                }
            }
        }
        return transportCount;
    }

    private void teleportEntitiesSameWorld(List<EntityLivingBase> list, int sourceX, int sourceY, int sourceZ,
            int destX, int destY, int destZ) {
        String sound = "mob.endermen.portal";
        for (EntityLivingBase e : list) {
            e.setPositionAndUpdate(e.posX - sourceX + destX, e.posY - sourceY + destY, e.posZ - sourceZ + destZ);
            e.worldObj.playSoundEffect(e.posX, e.posY, e.posZ, sound, 1.0F, 1.0F);
        }
    }

    private void teleportEntitiesToOtherWorld(List<EntityLivingBase> list, World sourceWorld, int sourceX, int sourceY,
            int sourceZ, World destWorld, int destX, int destY, int destZ) {
        for (EntityLivingBase e : list) {
            SpellHelper.teleportEntityToDim(
                    sourceWorld,
                    destWorld.provider.dimensionId,
                    e.posX - sourceX + destX,
                    e.posY - sourceY + destY,
                    e.posZ - sourceZ + destZ,
                    e);
        }
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
            for (int i = 0; i < 1; i++) {
                if (intData[2] != 0) {
                    ItemStack is = new ItemStack(Item.getItemById(intData[i * 3]), intData[2], intData[1]);
                    inv[i] = is;
                } else {
                    inv[i] = null;
                }
            }
        }
    }

    public int[] buildIntDataList() {
        int[] sortList = new int[3];

        for (ItemStack is : inv) {
            if (is != null) {
                sortList[0] = Item.getIdFromItem(is.getItem());
                sortList[1] = is.getItemDamage();
                sortList[2] = is.stackSize;
            } else {
                for (int i = 0; i < 3; i++) {
                    sortList[i] = 0;
                }
            }
        }

        return sortList;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
        return itemstack.getItem() instanceof TelepositionFocus;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (!config.getConfig(BloodMagicWailaPlugin.WAILA_CONFIG_TELEPOSER)) return;
        final ItemStack contained = getStackInSlot(0);
        if (contained.getItem() instanceof TelepositionFocus focus) {
            currenttip.add(contained.getDisplayName());

            NBTTagCompound itemTag = IBindable.getTag(contained);
            currenttip.add(
                    StatCollector.translateToLocal("tooltip.alchemy.coords") + " "
                            + itemTag.getInteger("xCoord")
                            + ", "
                            + itemTag.getInteger("yCoord")
                            + ", "
                            + itemTag.getInteger("zCoord"));
            currenttip.add(
                    StatCollector.translateToLocal("tooltip.alchemy.dimension") + " "
                            + focus.getDimensionID(contained));
        }
    }
}
