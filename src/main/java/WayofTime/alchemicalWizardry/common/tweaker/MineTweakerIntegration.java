package WayofTime.alchemicalWizardry.common.tweaker;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.mc1710.MineTweakerMod;
import minetweaker.util.IEventHandler;

/**
 * MineTweaker3 Integration by joshie *
 */
public class MineTweakerIntegration {

    public static void register() {
        MineTweakerAPI.registerClass(Alchemy.class);
        MineTweakerAPI.registerClass(Binding.class);
        MineTweakerAPI.registerClass(BloodAltar.class);
        MineTweakerAPI.registerClass(BloodOrb.class);
        MineTweakerAPI.registerClass(FallingTower.class);
        MineTweakerAPI.registerClass(HarvestMoon.class);

        MineTweakerImplementationAPI.onPostReload(new HandleLateAdditionsAndRemovals());
        // GTNH-Only features (post 3.1.0)
        final ArtifactVersion mtVersion = Loader.instance().getIndexedModList().get(MineTweakerMod.MODID)
                .getProcessedVersion();
        try {
            if (VersionRange.createFromVersionSpec("[3.2.4,)").containsVersion(mtVersion)) {
                GTNHIntegration.register();
            }
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }

    public static class GTNHIntegration {

        public static void register() {
            MineTweakerImplementationAPI.onRollbackEvent(new HandleLateAdditionsAndRemovals());
        }
    }

    public static class HandleLateAdditionsAndRemovals
            implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent> {

        @Override
        public void handle(MineTweakerImplementationAPI.ReloadEvent event) {
            BloodOrb.applyAdditionsAndRemovals();
        }
    }
}
