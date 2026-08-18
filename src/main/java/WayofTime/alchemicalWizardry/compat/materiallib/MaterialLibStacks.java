package WayofTime.alchemicalWizardry.compat.materiallib;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.StackResolver;

/// Resolves a MaterialLib item from the material name and shape token a config entry names, for the `ml:` entry forms
/// of the meteor configs.
///
/// MaterialLib types are named only here, so every caller can gate loading this class on `materiallib` being present.
public final class MaterialLibStacks {

    private MaterialLibStacks() {}

    /// The stack of the named material in the named shape, or null when either name matches nothing or the material
    /// does not generate the shape. A miss is logged by MaterialLib.
    public static ItemStack getStack(String materialName, String shapeToken) {
        return StackResolver.getStack(materialName, shapeToken, 1);
    }
}
