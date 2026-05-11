package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntWind;

public class RenderMinorDemonGrunt extends RenderLiving {

    private static final ResourceLocation NORMAL_GRUNT = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGrunt_normal.png");
    private static final ResourceLocation FIRE_GRUNT = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGrunt_fire.png");
    private static final ResourceLocation ICE_GRUNT = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGrunt_ice.png");
    private static final ResourceLocation WIND_GRUNT = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGrunt_wind.png");
    private static final ResourceLocation EARTH_GRUNT = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGrunt_earth.png");

    public RenderMinorDemonGrunt(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return switch (entity) {
            case EntityMinorDemonGruntFire _ -> FIRE_GRUNT;
            case EntityMinorDemonGruntWind _ -> WIND_GRUNT;
            case EntityMinorDemonGruntIce _ -> ICE_GRUNT;
            case EntityMinorDemonGruntEarth _ -> EARTH_GRUNT;
            default -> NORMAL_GRUNT;
        };
    }
}
