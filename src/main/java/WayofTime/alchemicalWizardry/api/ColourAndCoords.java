package WayofTime.alchemicalWizardry.api;

import net.minecraft.nbt.NBTTagCompound;

public record ColourAndCoords(int red, int green, int blue, int intensity, int x, int y, int z) {

    public static ColourAndCoords readFromNBT(NBTTagCompound tag) {
        return new ColourAndCoords(
                tag.getInteger("colourRed"),
                tag.getInteger("colourGreen"),
                tag.getInteger("colourBlue"),
                tag.getInteger("colourIntensity"),
                tag.getInteger("xCoord"),
                tag.getInteger("yCoord"),
                tag.getInteger("zCoord"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("colourRed", red);
        tag.setInteger("colourGreen", green);
        tag.setInteger("colourBlue", blue);
        tag.setInteger("colourIntensity", intensity);
        tag.setInteger("xCoord", x);
        tag.setInteger("yCoord", y);
        tag.setInteger("zCoord", z);

        return tag;
    }
}
