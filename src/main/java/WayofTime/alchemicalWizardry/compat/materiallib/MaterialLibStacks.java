package WayofTime.alchemicalWizardry.compat.materiallib;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.StackResolver;

/// Resolves a MaterialLib item from a material name and a shape token.
///
/// MaterialLib types are named only in this class, so it loads only where `materiallib` is present.
public final class MaterialLibStacks {

    private MaterialLibStacks() {}

    /// A single item of the named material in the named shape; see [StackResolver#getStack] for the naming rules and
    /// the null cases.
    public static ItemStack getStack(String materialName, String shapeToken) {
        return StackResolver.getStack(materialName, shapeToken, 1);
    }
}
