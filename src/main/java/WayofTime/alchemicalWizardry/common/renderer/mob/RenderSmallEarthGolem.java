package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSmallEarthGolem extends RenderLiving {

    private static final ResourceLocation SMALL_EARTH_GOLEM = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/SmallEarthGolem.png");

    public RenderSmallEarthGolem(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return SMALL_EARTH_GOLEM;
    }
}
