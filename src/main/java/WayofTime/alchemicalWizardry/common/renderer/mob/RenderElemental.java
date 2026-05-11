package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import WayofTime.alchemicalWizardry.common.entity.mob.EntityEarthElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFireElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityHolyElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShadeElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWaterElemental;

public class RenderElemental extends RenderLiving {

    private static final ResourceLocation AIR_BEACON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/AirFloatingBeacon.png");
    private static final ResourceLocation WATER_BEACON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/WaterFloatingBeacon.png");
    private static final ResourceLocation EARTH_BEACON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/EarthFloatingBeacon.png");
    private static final ResourceLocation FIRE_BEACON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/FireFloatingBeacon.png");
    private static final ResourceLocation SHADE_BEACON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/DarkFloatingBeacon.png");
    private static final ResourceLocation HOLY_BEACON = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/HolyFloatingBeacon.png");

    public RenderElemental(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return switch (entity) {
            case EntityWaterElemental _ -> WATER_BEACON;
            case EntityEarthElemental _ -> EARTH_BEACON;
            case EntityFireElemental _ -> FIRE_BEACON;
            case EntityShadeElemental _ -> SHADE_BEACON;
            case EntityHolyElemental _ -> HOLY_BEACON;
            default -> AIR_BEACON;
        };
    }
}
