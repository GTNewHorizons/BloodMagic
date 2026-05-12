package WayofTime.alchemicalWizardry.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import WayofTime.alchemicalWizardry.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class POVArmourModelWrapper extends ModelRenderer {

    private final ModelRenderer armModel;
    private final ResourceLocation resource;

    public POVArmourModelWrapper(ModelBase model) {
        super(model);
        ItemStack plateStack = new ItemStack(ModItems.boundPlateFire);
        ModelBiped bipedModel = ModItems.boundPlateFire
                .getArmorModel(Minecraft.getMinecraft().thePlayer, plateStack, 1);
        armModel = bipedModel.bipedRightArm;
        resource = new ResourceLocation(
                ModItems.boundPlateFire.getArmorTexture(plateStack, Minecraft.getMinecraft().thePlayer, 1, "POV"));
        addBox(0, 0, 0, 0, 0, 0); // Adds in a blank box as it's required in certain cases such as rendering arrows in
                                  // entities
    }

    @Override
    public void render(float partialTicks) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
        armModel.rotateAngleX = this.rotateAngleX;
        armModel.rotateAngleY = this.rotateAngleY;
        armModel.rotateAngleZ = this.rotateAngleZ;
        armModel.render(partialTicks);

        GL11.glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(ClientEventHandler.currentPlayerTexture);
    }
}
