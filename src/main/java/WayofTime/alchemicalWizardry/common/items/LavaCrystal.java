package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LavaCrystal extends EnergyItems {

    public LavaCrystal() {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setUnlocalizedName("lavaCrystal");
        setEnergyUsed(25);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:LavaCrystal");
    }

    /*
     * Used to have the item contain itself.
     */
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        syphonWhileInContainer(itemStack, this.getEnergyUsed());
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        IBindable.checkAndSetItemOwner(item, player);
        return item;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.lavacrystal.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.lavacrystal.desc2"));
        addBindingInformation(item, tooltip);
    }

    public boolean hasEnoughEssence(ItemStack itemStack) {
        if (itemStack.getTagCompound() != null && !(itemStack.getTagCompound().getString("ownerName").isEmpty())) {
            String ownerName = itemStack.getTagCompound().getString("ownerName");

            if (FMLCommonHandler.instance().getMinecraftServerInstance() == null) {
                return false;
            }

            WorldProvider provider = DimensionManager.getProvider(0);
            if (provider == null || provider.worldObj == null) {
                return false;
            }
            World world = provider.worldObj;
            LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

            if (data == null) {
                data = new LifeEssenceNetwork(ownerName);
                world.setItemData(ownerName, data);
            }

            if (data.currentEssence >= this.getEnergyUsed()) {
                return true;
            }
        }
        return false;
    }
}
