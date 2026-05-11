package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderShade extends RenderLiving {

    private static final ResourceLocation SHADE = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/ShadeMob.png");

    public RenderShade(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return SHADE;
    }
}
