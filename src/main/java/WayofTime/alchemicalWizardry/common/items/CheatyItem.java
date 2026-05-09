package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CheatyItem extends Item implements IBindable {

    public CheatyItem() {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBattery");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.mode.creative"));
        tooltip.add(StatCollector.translateToLocal("tooltip.cheatyitem.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.cheatyitem.desc2"));
        addBindingInformation(item, tooltip);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player instanceof FakePlayer) {
            return item;
        }

        if (world != null) {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect(
                    (float) posX + 0.5F,
                    (float) posY + 0.5F,
                    (float) posZ + 0.5F,
                    "random.fizz",
                    0.5F,
                    2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            SpellHelper.sendIndexedParticleToAllAround(
                    world,
                    posX,
                    posY,
                    posZ,
                    20,
                    world.provider.dimensionId,
                    4,
                    posX,
                    posY,
                    posZ);
        }

        if (player.worldObj.isRemote) {
            return item;
        }

        NBTTagCompound itemTag = item.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").isEmpty()) {
            return item;
        }

        if (player.isSneaking()) {
            SoulNetworkHandler.setCurrentEssence(itemTag.getString("ownerName"), 0);
        } else {
            SoulNetworkHandler.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 1000000, Integer.MAX_VALUE);
        }
        return item;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    public int getCurrentEssence(ItemStack item) {
        if (item == null) {
            return 0;
        }

        NBTTagCompound itemTag = item.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").isEmpty()) {
            return 0;
        }

        String owner = itemTag.getString("ownerName");
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null) {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        return (data.currentEssence);
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }
}
