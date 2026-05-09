package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderIceDemon extends RenderLiving {

    private static final ResourceLocation ICE_DEMON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/IceDemon.png");

    public RenderIceDemon(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return ICE_DEMON;
    }
}
