package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBileDemon extends RenderLiving {

    private static final ResourceLocation BILE_DEMON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/BileDemon.png");

    public RenderBileDemon(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return BILE_DEMON;
    }
}
