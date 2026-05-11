package WayofTime.alchemicalWizardry.common.renderer.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianWind;

public class RenderMinorDemonGruntGuardian extends RenderLiving {

    private static final ResourceLocation NORMAL_GUARDIAN = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGruntGuardian_normal.png");
    private static final ResourceLocation FIRE_GUARDIAN = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGruntGuardian_fire.png");
    private static final ResourceLocation ICE_GUARDIAN = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGruntGuardian_ice.png");
    private static final ResourceLocation WIND_GUARDIAN = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGruntGuardian_wind.png");
    private static final ResourceLocation EARTH_GUARDIAN = new ResourceLocation(
            "alchemicalwizardry",
            "textures/models/MinorDemonGruntGuardian_earth.png");

    public RenderMinorDemonGruntGuardian(ModelBase mainModel, float shadowSize) {
        super(mainModel, shadowSize);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return switch (entity) {
            case EntityMinorDemonGruntGuardianFire _ -> FIRE_GUARDIAN;
            case EntityMinorDemonGruntGuardianWind _ -> WIND_GUARDIAN;
            case EntityMinorDemonGruntGuardianIce _ -> ICE_GUARDIAN;
            case EntityMinorDemonGruntGuardianEarth _ -> EARTH_GUARDIAN;
            default -> NORMAL_GUARDIAN;
        };

    }
}
