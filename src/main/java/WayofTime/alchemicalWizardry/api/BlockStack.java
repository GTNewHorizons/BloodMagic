package WayofTime.alchemicalWizardry.api;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameData;

/**
 * A Block with a set metadata. Similar to an ItemStack.
 */
public record BlockStack(Block block, int meta) {

    @Nonnull
    @Override
    public String toString() {
        return GameData.getBlockRegistry().getNameForObject(block) + ":" + meta;
    }
}
