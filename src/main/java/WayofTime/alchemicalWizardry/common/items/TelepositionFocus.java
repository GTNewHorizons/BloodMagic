package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TelepositionFocus extends EnergyItems {

    private final int focusLevel;

    public TelepositionFocus(int focusLevel) {
        super();
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.focusLevel = focusLevel;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:TeleposerFocus");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.telepositionfocus.desc"));
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

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        IBindable.checkAndSetItemOwner(item, player);
        return item;
    }

    public int getDimensionID(ItemStack itemStack) {
        return IBindable.getTag(itemStack).getInteger("dimensionId");
    }

    public World getWorld(ItemStack itemStack) {
        return FMLCommonHandler.instance().getMinecraftServerInstance()
                .worldServerForDimension(getDimensionID(itemStack));
    }

    public int xCoord(ItemStack itemStack) {
        return IBindable.getTag(itemStack).getInteger("xCoord");
    }

    public int yCoord(ItemStack itemStack) {
        return IBindable.getTag(itemStack).getInteger("yCoord");
    }

    public int zCoord(ItemStack itemStack) {
        return IBindable.getTag(itemStack).getInteger("zCoord");
    }

    public int getFocusLevel() {
        return this.focusLevel;
    }
}
