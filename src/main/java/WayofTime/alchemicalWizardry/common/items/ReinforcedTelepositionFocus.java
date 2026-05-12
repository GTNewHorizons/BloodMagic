package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
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
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.reinforcedtelepfocus.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.reinforcedtelepfocus.desc2"));
        addBindingInformation(item, tooltip);

        NBTTagCompound itemTag = IBindable.getTag(item);
        tooltip.add(
                StatCollector.translateToLocal("tooltip.alchemy.coords") + " "
                        + itemTag.getInteger("xCoord")
                        + ", "
                        + itemTag.getInteger("yCoord")
                        + ", "
                        + itemTag.getInteger("zCoord"));
        tooltip.add(StatCollector.translateToLocal("tooltip.alchemy.dimension") + " " + getDimensionID(item));
    }
}
