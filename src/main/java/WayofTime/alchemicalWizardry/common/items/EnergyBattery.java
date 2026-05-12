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

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergyBattery extends Item implements ArmourUpgrade, IBindable, IBloodOrb {

    private final int maxEssence;
    protected int orbLevel;

    public EnergyBattery(int damage) {
        super();
        setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        maxEssence = damage;
        orbLevel = 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBattery");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.energybattery.desc"));
        tooltip.add(StatCollector.translateToLocalFormatted("tooltip.energybattery.capacity", this.getMaxEssence()));
        addBindingInformation(item, tooltip);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player)) {
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
        NBTTagCompound itemTag = item.getTagCompound();

        if (SpellHelper.isFakePlayer(world, player)) {
            return item;
        }

        if (itemTag == null || itemTag.getString("ownerName").isEmpty()) {
            return item;
        }

        if (world.isRemote) {
            return item;
        }

        if (itemTag.getString("ownerName").equals(SpellHelper.getUsername(player))) {
            SoulNetworkHandler.setMaxOrbToMax(itemTag.getString("ownerName"), this.orbLevel);
        }

        SoulNetworkHandler.addCurrentEssenceToMaximum(itemTag.getString("ownerName"), 200, this.getMaxEssence());
        EnergyItems.hurtPlayer(player, 200);
        return item;
    }

    /*
     * @return the damage that was not deducted
     */
    public int damageItem(ItemStack item, int par2int) {
        if (par2int == 0) {
            return 0;
        }

        int before = this.getDamage(item);
        this.setDamage(item, this.getDamage(item) + par2int);
        return par2int - (this.getDamage(item) - before);
    }

    protected void damagePlayer(World world, EntityPlayer player, int damage) {
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
    public int getMaxEssence() {
        return this.maxEssence;
    }

    @Override
    public int getOrbLevel() {
        return orbLevel;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {}

    @Override
    public boolean isUpgrade() {
        return false;
    }

    @Override
    public int getEnergyForTenSeconds() {
        return 0;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    // @SideOnly(Side.SERVER)
    @Deprecated
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

        return data.currentEssence;
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
        return false;
    }
}
