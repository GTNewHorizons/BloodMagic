package WayofTime.alchemicalWizardry.api.soulNetwork;

import net.minecraft.nbt.NBTTagCompound;

public class LifeEssenceNetwork extends net.minecraft.world.WorldSavedData {

    public int currentEssence;
    public int maxEssence;
    public int maxOrb;

    public LifeEssenceNetwork(String par1Str) {
        super(par1Str);
        currentEssence = 0;
        // can be lower than currentEssence (overfilled) with runes of the orb
        maxEssence = 1;
        maxOrb = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        currentEssence = nbttagcompound.getInteger("currentEssence");
        maxEssence = nbttagcompound.getInteger("maxEssence");
        maxOrb = nbttagcompound.getInteger("maxOrb");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("currentEssence", currentEssence);
        nbttagcompound.setInteger("maxEssence", maxEssence);
        nbttagcompound.setInteger("maxOrb", maxOrb);
    }
}
