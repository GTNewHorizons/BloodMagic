package WayofTime.alchemicalWizardry.api.items.interfaces;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public interface IBindable {
    default void addBindingInformation(ItemStack par1ItemStack, List par3List) {
        NBTTagCompound itemTag = par1ItemStack.getTagCompound();
        if (!(itemTag == null)) {
            if (itemTag.getBoolean("isActive")) {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.activated"));
            } else if (itemTag.hasKey("isActive")) {
                par3List.add(StatCollector.translateToLocal("tooltip.sigil.state.deactivated"));
            }

            if (!itemTag.getString("ownerName").equals("")) {
                par3List.add(
                        StatCollector.translateToLocal("tooltip.owner.currentowner") + " "
                                + itemTag.getString("ownerName"));
            }
        }
    }

}
