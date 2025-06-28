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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List,
            boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.telepositionfocus.desc"));
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

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        return par1ItemStack;
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
