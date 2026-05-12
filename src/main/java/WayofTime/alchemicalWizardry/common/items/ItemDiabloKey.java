package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDiabloKey extends EnergyItems {

    public ItemDiabloKey() {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.setEnergyUsed(1000);
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DiabloKey");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.diablokey.desc"));
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

        if (player.isSneaking()) {
            return item;
        }

        NBTTagCompound itemTag = item.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").isEmpty()) {
            return item;
        }

        String ownerName = itemTag.getString("ownerName");
        ItemStack[] inv = player.inventory.mainInventory;

        for (ItemStack itemStack : inv) {
            if (itemStack == null) {
                continue;
            }

            Item i = itemStack.getItem();

            if (i instanceof ItemDiabloKey) {
                continue;
            }

            if (i instanceof IBindable) {
                IBindable.checkAndSetItemOwner(itemStack, ownerName);
            }
        }
        player.inventoryContainer.detectAndSendChanges();

        return item;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list) {
        list.add(new ItemStack(ModItems.itemKeyOfDiablo));
        ItemStack boundKey = new ItemStack(ModItems.itemKeyOfDiablo);
        IBindable.checkAndSetItemOwner(boundKey, "Server-wide Soul Network");
        list.add(boundKey);
    }
}
