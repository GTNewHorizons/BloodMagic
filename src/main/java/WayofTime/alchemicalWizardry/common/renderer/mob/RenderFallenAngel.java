package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFallenAngel extends RenderLiving {

    private static final ResourceLocation WINGED_ANGEL = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/WingedAngel.png");

    public RenderFallenAngel(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return WINGED_ANGEL;
    }
}
