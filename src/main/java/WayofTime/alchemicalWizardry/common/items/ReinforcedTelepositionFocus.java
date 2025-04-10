package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ReinforcedTelepositionFocus extends TelepositionFocus {

    public ReinforcedTelepositionFocus() {
        super(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ReinforcedTeleposerFocus");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.reinforcedtelepfocus.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.reinforcedtelepfocus.desc2"));
        addBindingInformation(par1ItemStack, par3List);

        NBTTagCompound itemTag = IBindable.getTag(par1ItemStack);
        par3List.add(
                StatCollector.translateToLocal("tooltip.alchemy.coords") + " "
                        + itemTag.getInteger("xCoord")
                        + ", "
                        + itemTag.getInteger("yCoord")
                        + ", "
                        + itemTag.getInteger("zCoord"));
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.dimension") + " " + getDimensionID(par1ItemStack));
    }
}
