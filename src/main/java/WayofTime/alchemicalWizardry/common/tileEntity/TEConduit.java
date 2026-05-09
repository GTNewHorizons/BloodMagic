package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;

import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;

public class TEConduit extends TESpellBlock {

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
    }

    // Logic for the actual block is under here
    @Override
    public void updateEntity() {}

    @Override
    protected void applySpellChange(SpellParadigm parad) {
        return;
    }
}
