package WayofTime.alchemicalWizardry.common.items.sigil.holding;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHolding extends GuiContainer {

    public GuiHolding(EntityPlayer player, InventoryHolding inventoryHolding) {
        super(new ContainerHolding(player, inventoryHolding));
        xSize = 176;
        ySize = 121;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRendererObj.drawString("Sigil of Holding", 52, 4, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        // draw your Gui here, only thing you need to change is the path
        ResourceLocation test = new ResourceLocation("alchemicalwizardry", "gui/SigilOfHolding.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(test);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
