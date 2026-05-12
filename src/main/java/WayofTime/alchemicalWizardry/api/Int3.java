package WayofTime.alchemicalWizardry.api;

import net.minecraft.nbt.NBTTagCompound;

public record Int3(int x, int y, int z) {

    public static Int3 readFromNBT(NBTTagCompound tag) {
        return new Int3(tag.getInteger("xCoord"), tag.getInteger("yCoord"), tag.getInteger("zCoord"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("xCoord", x);
        tag.setInteger("yCoord", y);
        tag.setInteger("zCoord", z);

        return tag;
    }
}
