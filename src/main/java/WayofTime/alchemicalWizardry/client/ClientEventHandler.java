package WayofTime.alchemicalWizardry.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.sound.SoundEvent;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {

    private final Minecraft mc = FMLClientHandler.instance().getClient();

    public static ResourceLocation currentPlayerTexture = null;

    @SubscribeEvent
    public void onPlayerSoundEvent(SoundEvent event) {
        if (Minecraft.getMinecraft() != null) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (player != null && player.isPotionActive(AlchemicalWizardry.customPotionDeaf)) {
                event.setResult(Result.DENY);
                if (event.isCancelable()) event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onOverlayEvent(RenderBlockOverlayEvent event) {
        if (event.overlayType == RenderBlockOverlayEvent.OverlayType.WATER
                && event.player.isPotionActive(AlchemicalWizardry.customPotionAmphibian.id)) // TODO Placeholder for new
                                                                                             // potion effect
            if (event.isCancelable()) {
                event.setCanceled(true);
            }

        if (event.blockForOverlay == ModBlocks.blockMimic && event.isCancelable()) {
            event.setCanceled(true);
        }
    }
}
