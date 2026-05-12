package WayofTime.alchemicalWizardry.client.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RenderHelper {

    public static int lpBarX = 12;
    public static int lpBarY = 75;

    public static int zLevel = 0;

    private static final int xOffsetDefault = 50;
    public static int xOffset = xOffsetDefault;
    private static final int yOffsetDefault = 2;
    public static int yOffset = yOffsetDefault;
    private static final int yOffsetBottomCenterDefault = 41;
    public static int yOffsetBottomCenter = yOffsetBottomCenterDefault;
    private static final boolean applyXOffsetToCenterDefault = true;
    public static boolean applyXOffsetToCenter = applyXOffsetToCenterDefault;
    private static final boolean applyYOffsetToMiddleDefault = false;
    public static boolean applyYOffsetToMiddle = applyYOffsetToMiddleDefault;

    public static String listMode = "horizontal";
    public static String alignMode = "bottomcenter";
    public static Minecraft mc = Minecraft.getMinecraft();

    private static ScaledResolution scaledResolution;

    public static void onTickInGame(Minecraft mc) {
        if (!(mc.inGameHasFocus || mc.currentScreen == null || mc.currentScreen instanceof GuiChat)
                || mc.gameSettings.showDebugInfo) {
            return;
        }
        EntityPlayer player = mc.thePlayer;
        if (SpellHelper.canPlayerSeeAlchemy(player)) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            displayArmorStatus(mc);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        ItemStack stack = player.inventory.armorItemInSlot(2);
        if (stack != null && stack.getItem() instanceof OmegaArmour) {
            int maxAmount = (int) APISpellHelper.getPlayerMaxReagentAmount(player);

            if (maxAmount > 0) {
                float val = APISpellHelper.getPlayerCurrentReagentAmount(player);
                ReagentStack reagentStack = new ReagentStack(APISpellHelper.getPlayerReagentType(player), (int) val);

                if (reagentStack.amount > 0) {
                    renderOmegaReagentHUD(mc, reagentStack, maxAmount);
                }
            }
        }

        if (SpellHelper.canPlayerSeeLPBar(player)) {
            int max = APISpellHelper.getPlayerMaxLPTag(player);

            if (max > 1) {
                renderLPHUD(mc, APISpellHelper.getPlayerLPTag(player), max);
            }
        }

        float maxHP = APISpellHelper.getCurrentAdditionalMaxHP(player);
        if (maxHP > 0) {
            renderHPHUD(mc, APISpellHelper.getCurrentAdditionalHP(player), maxHP);
        }
    }

    private static void renderLPHUD(Minecraft mc, int lpAmount, int maxAmount) {
        GL11.glPushMatrix();
        int xSize = 32;
        int ySize = 32;
        int amount = Math.max((int) (256 * ((double) (maxAmount - lpAmount) / maxAmount)), 0);
        int x = (lpBarX - xSize / 2) * 8;
        int y = (lpBarY - ySize / 2) * 8;
        ResourceLocation test2 = new ResourceLocation("alchemicalwizardry", "textures/gui/container1.png");
        GL11.glColor4f(1, 0, 0, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        mc.renderEngine.bindTexture(test2);
        GL11.glScalef(1f / 8f, 1f / 8f, 1f / 8f);
        GL11.glPushMatrix();
        drawTexturedModalRect(x, y + amount, 0, amount, 256, 256 - amount);
        GL11.glPopMatrix();
        ResourceLocation test = new ResourceLocation("alchemicalwizardry", "textures/gui/lpVial.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(test);
        GL11.glPushMatrix();
        drawTexturedModalRect(x, y, 0, 0, 256, 256);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private static void renderHPHUD(Minecraft mc, float hpAmount, float maxAmount) {
        GL11.glPushMatrix();
        int xSize = 32;
        int ySize = 32;

        int amount = Math.max((int) (256 * ((double) (hpAmount) / maxAmount)), 0);

        int x = (lpBarX + 8 - xSize / 2) * 8;
        int y = (lpBarY + 32 - ySize / 2) * 8;

        ResourceLocation test2 = new ResourceLocation("alchemicalwizardry", "textures/gui/HPBar2.png");
        GL11.glColor4f(1, 0, 0, 1.0F);
        mc.getTextureManager().bindTexture(test2);

        GL11.glScalef(1f / 8f, 1f / 8f, 1f / 8f);

        drawTexturedModalRect(x, y, amount, 0, amount, 256);

        ResourceLocation test = new ResourceLocation("alchemicalwizardry", "textures/gui/HPBar1.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(test);

        drawTexturedModalRect(x, y, 0, 0, 256, 256);

        GL11.glPopMatrix();
    }

    private static List<HUDElement> getHUDElements(Minecraft mc) {
        List<HUDElement> elements = new ArrayList<>();

        MovingObjectPosition movingobjectposition = mc.objectMouseOver;
        World world = mc.theWorld;

        if (movingobjectposition == null) {
            return elements;
        }
        if (movingobjectposition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return elements;
        }
        int x = movingobjectposition.blockX;
        int y = movingobjectposition.blockY;
        int z = movingobjectposition.blockZ;

        TileEntity tile = world.getTileEntity(x, y, z);

        if (!(tile instanceof IReagentHandler relay)) {
            return elements;
        }

        ReagentContainerInfo[] infos = relay
                .getContainerInfo(ForgeDirection.getOrientation(movingobjectposition.sideHit));

        if (infos == null) {
            return elements;
        }
        for (ReagentContainerInfo info : infos) {
            if (info == null || info.reagent == null || info.reagent.reagent == null) {
                continue;
            }

            ItemStack itemStack = ReagentRegistry.getItemForReagent(info.reagent.reagent);

            if (itemStack != null) elements.add(new HUDElement(itemStack, 16, 16, 2, info.reagent.amount));
        }

        return elements;
    }

    private static int getX(int width) {
        if (alignMode.toLowerCase().contains("center"))
            return scaledResolution.getScaledWidth() / 2 - width / 2 + (applyXOffsetToCenter ? xOffset : 0);
        else if (alignMode.toLowerCase().contains("right")) return scaledResolution.getScaledWidth() - width - xOffset;
        else return xOffset;
    }

    private static int getY(int rowCount, int height) {
        if (alignMode.toLowerCase().contains("middle"))
            return (scaledResolution.getScaledHeight() / 2) - ((rowCount * height) / 2)
                    + (applyYOffsetToMiddle ? yOffset : 0);
        else if (alignMode.equalsIgnoreCase("bottomleft") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffset;
        else if (alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffsetBottomCenter;
        else return yOffset;
    }

    private static int getElementsWidth(List<HUDElement> elements) {
        int r = 0;
        for (HUDElement he : elements) r += he.width();

        return r;
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, (float) (textureX) * f, (float) (textureY + height) * f);
        tessellator.addVertexWithUV(
                x + width,
                y + height,
                zLevel,
                (float) (textureX + width) * f,
                (float) (textureY + height) * f);
        tessellator.addVertexWithUV(x + width, y, zLevel, (float) (textureX + width) * f, (float) (textureY) * f);
        tessellator.addVertexWithUV(x, y, zLevel, (float) (textureX) * f, (float) (textureY) * f);
        tessellator.draw();
    }

    private static void renderOmegaReagentHUD(Minecraft mc, ReagentStack reagentStack, int maxAmount) {
        GL11.glPushMatrix();
        Reagent reagent = reagentStack.reagent;
        int xSize = 32;
        int ySize = 32;

        int amount = Math.max((int) (256 * ((double) (maxAmount - reagentStack.amount) / maxAmount)), 0);

        int x = (lpBarX + 16 - xSize / 2) * 8;
        int y = (lpBarY - ySize / 2) * 8;

        GL11.glScalef(1f / 8f, 1f / 8f, 1f / 8f);

        ResourceLocation test2 = new ResourceLocation("alchemicalwizardry", "textures/gui/container1.png");
        GL11.glColor4ub((byte) reagent.red(), (byte) reagent.green(), (byte) reagent.blue(), (byte) 255);
        mc.getTextureManager().bindTexture(test2);

        drawTexturedModalRect(x, y + amount, 0, amount, 256, 256 - amount);

        ResourceLocation test = new ResourceLocation("alchemicalwizardry", "textures/gui/container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(test);

        drawTexturedModalRect(x, y, 0, 0, 256, 256);

        GL11.glPopMatrix();
    }

    private static void displayArmorStatus(Minecraft mc) {
        List<HUDElement> elements = getHUDElements(mc);

        if (elements.isEmpty()) {
            return;
        }
        int yOffset = 16;

        if (listMode.equalsIgnoreCase("vertical")) {
            int yBase = getY(elements.size(), yOffset);

            for (HUDElement e : elements) {
                e.renderToHud((alignMode.toLowerCase().contains("right") ? getX(0) : getX(e.width())), yBase);
                yBase += yOffset;
            }
        } else if (listMode.equalsIgnoreCase("horizontal")) {
            int totalWidth = getElementsWidth(elements);
            int yBase = getY(1, yOffset);
            int xBase = getX(totalWidth);
            int prevX = 0;

            for (HUDElement e : elements) {
                e.renderToHud(xBase + prevX + (alignMode.toLowerCase().contains("right") ? e.width() : 0), yBase);
                prevX += (e.width());
            }
        } // else if (listMode.equalsIgnoreCase("compound"))
        {
            // TODO
        }
    }
}
