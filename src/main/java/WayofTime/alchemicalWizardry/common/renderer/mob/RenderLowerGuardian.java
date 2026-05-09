package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderLowerGuardian extends RenderLiving {

    private static final ResourceLocation LOWER_GUARDIAN = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/LowerGuardian.png");

    public RenderLowerGuardian(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return LOWER_GUARDIAN;
    }
}
