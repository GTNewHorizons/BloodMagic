package WayofTime.alchemicalWizardry.api;

import net.minecraft.nbt.NBTTagCompound;

public record ColourAndCoords(int red, int green, int blue, int intensity, int x, int y, int z) {

    public static ColourAndCoords readFromNBT(NBTTagCompound tag) {
        return new ColourAndCoords(
                tag.getInteger("red"),
                tag.getInteger("green"),
                tag.getInteger("blue"),
                tag.getInteger("intensity"),
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("red", red);
        tag.setInteger("green", green);
        tag.setInteger("blue", blue);
        tag.setInteger("intensity", intensity);
        tag.setInteger("x", x);
        tag.setInteger("y", y);
        tag.setInteger("z", z);

        return tag;
    }
}
