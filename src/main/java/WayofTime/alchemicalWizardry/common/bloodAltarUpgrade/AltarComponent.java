package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.api.BlockStack;

public class AltarComponent {

    private final int x, y, z;
    private final List<BlockStack> validBlocks;

    private final boolean isBloodRune, isUpgradeSlot;

    public AltarComponent(int x, int y, int z, Block block, int metadata, boolean isBloodRune, boolean isUpgradeSlot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.validBlocks = new ArrayList<>();
        this.validBlocks.add(new BlockStack(block, metadata));
        this.isBloodRune = isBloodRune;
        this.isUpgradeSlot = isUpgradeSlot;
    }

    public AltarComponent(int x, int y, int z, List<BlockStack> validBlocks, boolean isBloodRune,
            boolean isUpgradeSlot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.validBlocks = validBlocks;
        this.isBloodRune = isBloodRune;
        this.isUpgradeSlot = isUpgradeSlot;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Block getBlock() {
        if (anyBlockMatches()) {
            return Blocks.air;
        }
        return validBlocks.get(0).getBlock();
    }

    public int getMetadata() {
        if (anyBlockMatches()) {
            return OreDictionary.WILDCARD_VALUE;
        }
        return validBlocks.get(0).getMeta();
    }

    public boolean isBloodRune() {
        return isBloodRune;
    }

    public boolean isUpgradeSlot() {
        return isUpgradeSlot;
    }

    public List<BlockStack> getValidBlocks() {
        return validBlocks;
    }

    public boolean matches(Block block, int meta) {
        if (anyBlockMatches()) return true;

        for (BlockStack pair : validBlocks) {
            if (pair.getBlock() == block
                    && (pair.getMeta() == meta || pair.getMeta() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }
        return false;
    }

    public boolean anyBlockMatches() {
        return validBlocks.isEmpty();
    }
}
