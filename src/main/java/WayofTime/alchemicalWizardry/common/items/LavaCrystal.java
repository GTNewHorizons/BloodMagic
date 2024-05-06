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
        {
            syphonWhileInContainer(itemStack, this.getEnergyUsed());
            ItemStack copiedStack = itemStack.copy();
            copiedStack.setItemDamage(copiedStack.getItemDamage());
            copiedStack.stackSize = 1;
            return copiedStack;
        }
    }

    @Override
    public boolean hasContainerItem() {
        return true;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        return par1ItemStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.lavacrystal.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.lavacrystal.desc2"));

        if (!(par1ItemStack.getTagCompound() == null)) {
            par3List.add(
                    StatCollector.translateToLocal("tooltip.owner.currentowner") + " "
                            + par1ItemStack.getTagCompound().getString("ownerName"));
        }
    }

    public boolean hasEnoughEssence(ItemStack itemStack) {
        if (itemStack.getTagCompound() != null && !(itemStack.getTagCompound().getString("ownerName").equals(""))) {
            String ownerName = itemStack.getTagCompound().getString("ownerName");

            if (FMLCommonHandler.instance().getMinecraftServerInstance() == null) {
                return false;
            }

            // World world = MinecraftServer.getServer().worldServers[0];
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
