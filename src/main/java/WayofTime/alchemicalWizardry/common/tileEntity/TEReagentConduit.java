package WayofTime.alchemicalWizardry.common.tileEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.api.ColourAndCoords;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.alchemy.energy.TileSegmentedReagentHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityParticleBeam;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TEReagentConduit extends TileSegmentedReagentHandler {

    public List<ColourAndCoords> destinationList; // These are offsets
    public Map<Reagent, List<Int3>> reagentTargetList;
    public Map<Reagent, Integer> reagentTankDesignationList;
    public int tickRate = 20; // Rate that the reagents are sent

    int hasChanged = 0;
    public boolean affectedByRedstone = true;

    public int maxConnextions = 5;

    public int renderCount = 0;

    public TEReagentConduit() {
        this(2, 2000);
    }

    public TEReagentConduit(int numberOfTanks, int size) {
        super(numberOfTanks, size);

        destinationList = new LinkedList<>();
        reagentTargetList = new HashMap<>();
        reagentTankDesignationList = new HashMap<>();
    }

    public Int3 getColour() {
        int[] redMap = new int[this.tanks.length];
        int[] greenMap = new int[this.tanks.length];
        int[] blueMap = new int[this.tanks.length];

        for (int i = 0; i < this.tanks.length; i++) {
            ReagentContainer container = this.tanks[i];
            if (container != null && container.getReagent() != null) {
                Reagent reagent = container.getReagent().reagent;

                redMap[i] = reagent.red();
                greenMap[i] = reagent.green();
                blueMap[i] = reagent.blue();
            }
        }

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int i = 0; i < this.tanks.length; i++) {
            red += redMap[i];
            green += greenMap[i];
            blue += blueMap[i];
        }

        red /= this.tanks.length;
        green /= this.tanks.length;
        blue /= this.tanks.length;

        return new Int3(red, green, blue);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("hasChanged", hasChanged);

        NBTTagList tagList = new NBTTagList();

        for (ColourAndCoords colourAndCoords : destinationList) {
            NBTTagCompound savedTag = new NBTTagCompound();
            tagList.appendTag(colourAndCoords.writeToNBT(savedTag));
        }

        tag.setTag("destinationList", tagList);

        NBTTagList reagentTagList = new NBTTagList(); // TODO

        for (Entry<Reagent, List<Int3>> entry : reagentTargetList.entrySet()) {
            NBTTagCompound savedTag = new NBTTagCompound();
            savedTag.setString("reagent", ReagentRegistry.getKeyForReagent(entry.getKey()));

            NBTTagList coordinateTagList = new NBTTagList();

            for (Int3 coord : entry.getValue()) {
                NBTTagCompound coordinateTag = new NBTTagCompound();

                coord.writeToNBT(coordinateTag);

                coordinateTagList.appendTag(coordinateTag);
            }

            savedTag.setTag("coordinateList", coordinateTagList);

            reagentTagList.appendTag(savedTag);
        }

        tag.setTag("reagentTargetList", reagentTagList);

        NBTTagList tankDesignationList = new NBTTagList();

        for (Entry<Reagent, Integer> entry : this.reagentTankDesignationList.entrySet()) {
            NBTTagCompound savedTag = new NBTTagCompound();

            savedTag.setString("reagent", ReagentRegistry.getKeyForReagent(entry.getKey()));
            savedTag.setInteger("integer", entry.getValue());

            tankDesignationList.appendTag(savedTag);
        }

        tag.setTag("tankDesignationList", tankDesignationList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        hasChanged = tag.getInteger("hasChanged");

        NBTTagList tagList = tag.getTagList("destinationList", Constants.NBT.TAG_COMPOUND);

        destinationList = new LinkedList<>();

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);

            destinationList.add(ColourAndCoords.readFromNBT(savedTag));
        }

        reagentTargetList = new HashMap<>();

        NBTTagList reagentTagList = tag.getTagList("reagentTargetList", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < reagentTagList.tagCount(); i++) {
            NBTTagCompound savedTag = reagentTagList.getCompoundTagAt(i);

            Reagent reagent = ReagentRegistry.getReagentForKey(savedTag.getString("reagent"));

            List<Int3> coordList = new LinkedList<>();

            NBTTagList coordinateList = savedTag.getTagList("coordinateList", Constants.NBT.TAG_COMPOUND);

            for (int j = 0; j < coordinateList.tagCount(); j++) {
                coordList.add(Int3.readFromNBT(coordinateList.getCompoundTagAt(j)));
            }

            reagentTargetList.put(reagent, coordList);
        }

        reagentTankDesignationList = new HashMap<>();

        NBTTagList tankDesignationList = tag.getTagList("tankDesignationList", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < tankDesignationList.tagCount(); i++) {
            NBTTagCompound savedTag = tankDesignationList.getCompoundTagAt(i);

            this.reagentTankDesignationList.put(
                    ReagentRegistry.getReagentForKey(savedTag.getString("reagent")),
                    savedTag.getInteger("integer"));
        }
    }

    public void readClientNBT(NBTTagCompound tag) {
        NBTTagList tagList = tag.getTagList("destinationList", Constants.NBT.TAG_COMPOUND);

        destinationList = new LinkedList<>();

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound savedTag = tagList.getCompoundTagAt(i);

            destinationList.add(ColourAndCoords.readFromNBT(savedTag));
        }

        NBTTagList reagentTagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

        int size = reagentTagList.tagCount();
        this.tanks = new ReagentContainer[size];

        for (int i = 0; i < size; i++) {
            NBTTagCompound savedTag = reagentTagList.getCompoundTagAt(i);
            this.tanks[i] = ReagentContainer.readFromNBT(savedTag);
        }
    }

    public void writeClientNBT(NBTTagCompound tag) {
        NBTTagList tagList = new NBTTagList();

        for (ColourAndCoords colourAndCoords : destinationList) {
            NBTTagCompound savedTag = new NBTTagCompound();
            tagList.appendTag(colourAndCoords.writeToNBT(savedTag));
        }

        tag.setTag("destinationList", tagList);

        NBTTagList reagentTagList = new NBTTagList();

        for (ReagentContainer tank : this.tanks) {
            NBTTagCompound savedTag = new NBTTagCompound();
            if (tank != null) {
                tank.writeToNBT(savedTag);
            }
            reagentTagList.appendTag(savedTag);
        }

        tag.setTag("reagentTanks", reagentTagList);
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (hasChanged > 1) {
                hasChanged = 1;
            } else if (hasChanged == 1) {
                hasChanged = 0;
            }

            if (worldObj.getWorldTime() % 100 == 99) {
                this.updateColourList();
            }

            if (affectedByRedstone && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
                return;
            }

            int totalTransfered = 0;

            for (Entry<Reagent, List<Int3>> entry : this.reagentTargetList.entrySet()) {
                for (Int3 coord : entry.getValue()) {
                    if (totalTransfered >= this.tickRate) {
                        break;
                    }

                    ReagentStack maxDrainAmount = this.drain(
                            ForgeDirection.UNKNOWN,
                            new ReagentStack(entry.getKey(), this.tickRate - totalTransfered),
                            false);

                    if (maxDrainAmount == null) {
                        continue;
                    }

                    int amountLeft = maxDrainAmount.amount;

                    if (amountLeft <= 0) {
                        continue;
                    }

                    int x = xCoord + coord.x();
                    int y = yCoord + coord.y();
                    int z = zCoord + coord.z();

                    TileEntity tile = worldObj.getTileEntity(x, y, z);
                    if (tile instanceof IReagentHandler) {
                        int amount = Math.min(
                                ((IReagentHandler) tile).fill(ForgeDirection.UNKNOWN, maxDrainAmount, false),
                                amountLeft);
                        if (amount > 0) {
                            amountLeft -= amount;
                            totalTransfered += amount;

                            ReagentStack stack = this
                                    .drain(ForgeDirection.UNKNOWN, new ReagentStack(entry.getKey(), amount), true);
                            ((IReagentHandler) tile).fill(ForgeDirection.UNKNOWN, stack, true);
                        }
                    }
                }
            }
        } else {
            if (affectedByRedstone && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
                return;
            }

            renderCount++;

            if (worldObj.getWorldTime() % 100 != 0) {
                return;
            }

            this.sendPlayerStuffs();
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendPlayerStuffs() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (SpellHelper.canPlayerSeeAlchemy(player)) {
            for (ColourAndCoords colourSet : this.destinationList) {
                if (!(worldObj.getTileEntity(
                        xCoord + colourSet.x(),
                        yCoord + colourSet.y(),
                        zCoord + colourSet.z()) instanceof IReagentHandler)) {
                    continue;
                }
                EntityParticleBeam beam = new EntityParticleBeam(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
                double velocity = Math
                        .sqrt(Math.pow(colourSet.x(), 2) + Math.pow(colourSet.y(), 2) + Math.pow(colourSet.z(), 2));
                double wantedVel = 0.3d;
                beam.setVelocity(
                        wantedVel * colourSet.x() / velocity,
                        wantedVel * colourSet.y() / velocity,
                        wantedVel * colourSet.z() / velocity);
                beam.setColour(colourSet.red() / 255f, colourSet.green() / 255f, colourSet.blue() / 255f);
                beam.setDestination(xCoord + colourSet.x(), yCoord + colourSet.y(), zCoord + colourSet.z());
                worldObj.spawnEntityInWorld(beam);
            }
        }
    }

    public void updateColourList() {
        if (worldObj.isRemote) {
            return;
        }

        List<ColourAndCoords> newList = this.compileListForReagentTargets(this.reagentTargetList);

        if (newList != null && !newList.equals(destinationList)) {
            this.destinationList = newList;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public List<ColourAndCoords> compileListForReagentTargets(Map<Reagent, List<Int3>> map) {
        List<ColourAndCoords> list = new LinkedList<>();

        for (Entry<Reagent, List<Int3>> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                Reagent reagent = entry.getKey();
                if (reagent == null) {
                    continue;
                }
                List<Int3> coords = entry.getValue();
                for (Int3 coord : coords) {
                    if (coord == null) {
                        continue;
                    }
                    list.add(
                            new ColourAndCoords(
                                    reagent.red(),
                                    reagent.green(),
                                    reagent.blue(),
                                    reagent.intensity(),
                                    coord.x(),
                                    coord.y(),
                                    coord.z()));
                }
            }
        }

        return list;
    }

    public boolean addDestinationViaOffset(int red, int green, int blue, int intensity, int xOffset, int yOffset,
            int zOffset) {
        if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
            return false;
        }

        this.destinationList.add(new ColourAndCoords(red, green, blue, intensity, xOffset, yOffset, zOffset));

        return true;
    }

    public boolean addDestinationViaActual(int red, int green, int blue, int intensity, int x, int y, int z) {
        return this.addDestinationViaOffset(
                red,
                green,
                blue,
                intensity,
                x - this.xCoord,
                y - this.yCoord,
                z - this.zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeClientNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 90210, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        readClientNBT(packet.func_148857_g());
    }

    public int getTotalConnections() {
        int total = 0;
        for (List<Int3> list : this.reagentTargetList.values()) {
            if (list != null) {
                total += list.size();
            }
        }
        return total;
    }

    public boolean hasReagentDestination(Reagent reagent, int x, int y, int z) {
        int xOffset = x - this.xCoord;
        int yOffset = y - this.yCoord;
        int zOffset = z - this.zCoord;
        if (this.reagentTargetList.containsKey(reagent)) {
            List<Int3> coords = this.reagentTargetList.get(reagent);
            if (coords != null) {
                return coords.contains(new Int3(xOffset, yOffset, zOffset));
            }
        }
        return false;
    }

    public boolean addReagentDestinationViaOffset(Reagent reagent, int xOffset, int yOffset, int zOffset) {
        if (getTotalConnections() >= this.maxConnextions) {
            return false;
        }

        if (xOffset == 0 && yOffset == 0 && zOffset == 0) {
            return false;
        }

        Int3 newCoord = new Int3(xOffset, yOffset, zOffset);

        if (this.reagentTargetList.containsKey(reagent)) {
            List<Int3> coordList = this.reagentTargetList.get(reagent);
            if (coordList == null) {
                List<Int3> newCoordList = new LinkedList<>();
                newCoordList.add(newCoord);
                this.reagentTargetList.put(reagent, newCoordList);
            } else {
                if (coordList.contains(newCoord)) {
                    return false;
                }
                coordList.add(newCoord);
            }

            return true;
        } else {
            List<Int3> newCoordList = new LinkedList<>();
            newCoordList.add(newCoord);
            this.reagentTargetList.put(reagent, newCoordList);

            return true;
        }
    }

    public boolean addReagentDestinationViaActual(Reagent reagent, int x, int y, int z) {
        return (this.addReagentDestinationViaOffset(reagent, x - xCoord, y - yCoord, z - zCoord));
    }

    public boolean removeReagentDestinationViaOffset(Reagent reagent, int xOffset, int yOffset, int zOffset) {
        if (this.reagentTargetList.containsKey(reagent)) {
            List<Int3> coords = this.reagentTargetList.get(reagent);
            if (coords != null) {
                Int3 reference = new Int3(xOffset, yOffset, zOffset);

                return coords.remove(reference);
            }
        }
        return false;
    }

    public boolean removeReagentDestinationViaActual(Reagent reagent, int x, int y, int z) {
        return this.removeReagentDestinationViaOffset(reagent, x - xCoord, y - yCoord, z - zCoord);
    }

    @Override
    public int fill(ForgeDirection from, ReagentStack resource, boolean doFill) {
        if (doFill && !worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            hasChanged = 2;
        }

        return super.fill(from, resource, doFill);
    }

    @Override
    public ReagentStack drain(ForgeDirection from, ReagentStack resource, boolean doDrain) {
        if (doDrain && !worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            hasChanged = 2;
        }

        return super.drain(from, resource, doDrain);
    }
}
