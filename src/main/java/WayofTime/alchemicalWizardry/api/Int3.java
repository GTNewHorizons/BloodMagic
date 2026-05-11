package WayofTime.alchemicalWizardry.api;

import net.minecraft.nbt.NBTTagCompound;

public record Int3(int x, int y, int z) {

    public static Int3 readFromNBT(NBTTagCompound tag) {
        return new Int3(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);

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
