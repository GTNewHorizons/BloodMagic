package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEHomHeart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlankSpell extends EnergyItems {

    public BlankSpell() {
        super();
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BlankSpell");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.blankspell.desc"));
        addBindingInformation(item, tooltip);

        if (!(item.getTagCompound() == null)) {
            NBTTagCompound itemTag = item.getTagCompound();

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

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (world.isRemote) {
            return item;
        }
        World dim = DimensionManager.getWorld(getDimensionID(item));

        if (dim == null) {
            return item;
        }
        NBTTagCompound itemTag = item.getTagCompound();
        TileEntity tileEntity = dim.getTileEntity(
                itemTag.getInteger("xCoord"),
                itemTag.getInteger("yCoord"),
                itemTag.getInteger("zCoord"));

        if (!(tileEntity instanceof TEHomHeart homHeart)) {
            return item;
        }
        if (!homHeart.canCastSpell(item, world, player)) {
            return item;
        }
        if (EnergyItems.syphonBatteries(item, player, homHeart.getCostForSpell())) {
            EnergyItems.syphonBatteries(item, player, homHeart.castSpell(item, world, player));
        }
        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        return item;
    }

    public int getDimensionID(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.getTagCompound().getInteger("dimensionId");
    }
}
