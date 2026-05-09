package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.tile.ISpellTile;

public abstract class TESpellBlock extends TEOrientable implements ISpellTile {

    @Override
    public void modifySpellParadigm(SpellParadigm parad) {
        this.applySpellChange(parad);
        TileEntity tile = this.getTileAtOutput();
        if (tile instanceof TESpellBlock outputBlock) {
            outputBlock.modifySpellParadigm(parad);
        }
    }

    protected abstract void applySpellChange(SpellParadigm parad);

    public TESpellBlock getTileAtOutput() {
        ForgeDirection output = this.getOutputDirection();
        int xOffset = output.offsetX;
        int yOffset = output.offsetY;
        int zOffset = output.offsetZ;

        TileEntity tile = worldObj.getTileEntity(xCoord + xOffset, yCoord + yOffset, zCoord + zOffset);

        if (tile instanceof TESpellBlock && ((TESpellBlock) tile).canInputRecieveOutput(output)) {
            return (TESpellBlock) tile;
        }

        return null;
    }

    public boolean canInputRecieve() {
        return true;
    }

    @Override
    public boolean canInputRecieveOutput(ForgeDirection output) {
        return this.canInputRecieve() && this.getInputDirection().getOpposite() == output;
    }
}
