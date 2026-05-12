package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.api.BlockStack;

public record AltarComponent(int x, int y, int z, List<BlockStack> validBlocks, boolean isBloodRune,
        boolean isUpgradeSlot) {

    public Block getBlock() {
        if (anyBlockMatches()) {
            return Blocks.air;
        }
        return validBlocks.getFirst().block();
    }

    public int getMetadata() {
        if (anyBlockMatches()) {
            return OreDictionary.WILDCARD_VALUE;
        }
        return validBlocks.getFirst().meta();
    }

    public boolean matches(Block block, int meta) {
        if (anyBlockMatches()) return true;

        for (BlockStack pair : validBlocks) {
            if (pair.block() == block && (pair.meta() == meta || pair.meta() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }
        return false;
    }

    public boolean anyBlockMatches() {
        return validBlocks.isEmpty();
    }
}
