package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.block.IOrientable;

public class TEOrientable extends TileEntity implements IOrientable {

    protected ForgeDirection inputFace;
    protected ForgeDirection outputFace;

    public TEOrientable() {
        this.inputFace = ForgeDirection.DOWN;
        this.outputFace = ForgeDirection.UP;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.setInputDirection(ForgeDirection.getOrientation(tag.getInteger("inputFace")));
        this.setOutputDirection(ForgeDirection.getOrientation(tag.getInteger("outputFace")));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("inputFace", TEOrientable.getIntForForgeDirection(this.getInputDirection()));
        tag.setInteger("outputFace", TEOrientable.getIntForForgeDirection(this.getOutputDirection()));
    }

    @Override
    public ForgeDirection getInputDirection() {
        return this.inputFace;
    }

    @Override
    public ForgeDirection getOutputDirection() {
        return this.outputFace;
    }

    @Override
    public void setInputDirection(ForgeDirection direction) {
        this.inputFace = direction;
    }

    @Override
    public void setOutputDirection(ForgeDirection direction) {
        this.outputFace = direction;
    }

    public static int getIntForForgeDirection(ForgeDirection direction) {
        return switch (direction) {
            case UP -> 1;
            case NORTH -> 2;
            case SOUTH -> 3;
            case WEST -> 4;
            case EAST -> 5;
            default -> 0;
        };
    }

    @Override
    public Packet getDescriptionPacket() {
        return NewPacketHandler.getPacket(this);
    }

    public boolean isSideRendered(ForgeDirection side) {
        return side.equals(this.getInputDirection()) || side.equals(this.getOutputDirection());
    }

    public String getResourceLocationForMeta(int meta) {
        return "";
    }
}
