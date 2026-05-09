package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.world.World;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record DemonCrosspath(int xCoord, int yLevel, int zCoord) {

    public void createCrosspath(World world) {}
}
