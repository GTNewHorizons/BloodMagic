package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBoulderFist extends RenderLiving {

    private static final ResourceLocation BOULDER_FIST = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/BoulderFist.png");

    public RenderBoulderFist(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return BOULDER_FIST;
    }
}
