package WayofTime.alchemicalWizardry.common.tweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;

/**
 * MineTweaker3 Integration by joshie *
 */
public class MineTweakerIntegration 
{
    public static void register() 
    {
        MineTweakerAPI.registerClass(Alchemy.class);
        MineTweakerAPI.registerClass(Binding.class);
        MineTweakerAPI.registerClass(BloodAltar.class);
        MineTweakerAPI.registerClass(BloodOrb.class);
        MineTweakerAPI.registerClass(FallingTower.class);
        MineTweakerAPI.registerClass(HarvestMoon.class);

        MineTweakerImplementationAPI.onRollbackEvent(new HandleLateAdditionsAndRemovals());
        MineTweakerImplementationAPI.onPostReload(new HandleLateAdditionsAndRemovals());
    }
    public static class HandleLateAdditionsAndRemovals implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent>
    {
        @Override
        public void handle(MineTweakerImplementationAPI.ReloadEvent event)
        {
            BloodOrb.applyAdditionsAndRemovals();
        }
    }


}
