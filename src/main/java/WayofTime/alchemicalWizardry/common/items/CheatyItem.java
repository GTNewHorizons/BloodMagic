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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.mode.creative"));
        par3List.add(StatCollector.translateToLocal("tooltip.cheatyitem.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.cheatyitem.desc2"));
        addBindingInformation(par1ItemStack, par3List);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        World world = par3EntityPlayer.worldObj;

        if (!EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer)
                || par3EntityPlayer instanceof FakePlayer) {
            return par1ItemStack;
        }

        if (world != null) {
            double posX = par3EntityPlayer.posX;
            double posY = par3EntityPlayer.posY;
            double posZ = par3EntityPlayer.posZ;
            world.playSoundEffect(
                    (double) ((float) posX + 0.5F),
                    (double) ((float) posY + 0.5F),
                    (double) ((float) posZ + 0.5F),
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

        if (par3EntityPlayer.worldObj.isRemote) {
            return par1ItemStack;
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals("")) {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isSneaking()) {
            SoulNetworkHandler.setCurrentEssence(itemTag.getString("ownerName"), 0);
        } else {
            SoulNetworkHandler.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 1000000, Integer.MAX_VALUE);
        }
        return par1ItemStack;
    }

    /*
     * @return the damage that was not deducted
     */
    public int damageItem(ItemStack par1ItemStack, int par2int) {
        if (par2int == 0) {
            return 0;
        }

        int before = this.getDamage(par1ItemStack);
        this.setDamage(par1ItemStack, this.getDamage(par1ItemStack) + par2int);
        return par2int - (this.getDamage(par1ItemStack) - before);
    }

    protected void damagePlayer(World world, EntityPlayer player, int damage) {
        if (world != null) {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect(
                    (double) ((float) posX + 0.5F),
                    (double) ((float) posY + 0.5F),
                    (double) ((float) posZ + 0.5F),
                    "random.fizz",
                    0.5F,
                    2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            float f = 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;

            for (int l = 0; l < 8; ++l) {
                world.spawnParticle(
                        "reddust",
                        posX + Math.random() - Math.random(),
                        posY + Math.random() - Math.random(),
                        posZ + Math.random() - Math.random(),
                        f1,
                        f2,
                        f3);
            }
        }

        if (!player.capabilities.isCreativeMode) {
            for (int i = 0; i < damage; i++) {
                player.setHealth((player.getHealth() - 1));
            }
        }

        if (player.getHealth() <= 0) {
            player.inventory.dropAllItems();
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    public int getCurrentEssence(ItemStack par1ItemStack) {
        if (par1ItemStack == null) {
            return 0;
        }

        NBTTagCompound itemTag = par1ItemStack.getTagCompound();

        if (itemTag == null || itemTag.getString("ownerName").equals("")) {
            return 0;
        }

        String owner = itemTag.getString("ownerName");
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null) {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        return (currentEssence);
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }
}
