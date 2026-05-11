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

    @Override
    public boolean equals(Object o) {
        return o instanceof Int3(int x1, int y1, int z1) && x1 == this.x && y1 == this.y && z1 == this.z;
    }

    @Override
    public int hashCode() {
        return this.x + this.y << 8 + this.z << 16;
    }
}
