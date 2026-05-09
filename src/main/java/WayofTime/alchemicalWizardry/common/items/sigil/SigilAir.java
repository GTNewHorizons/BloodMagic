package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilAir extends EnergyItems implements ArmourUpgrade, ISigil {

    public SigilAir() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilAirCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.airsigil.desc"));
        addBindingInformation(item, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:AirSigil");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (world.isRemote && this.isItemUnusable(item)) {
            return item;
        }

        Vec3 vec = player.getLookVec();
        double wantedVelocity = 1.7;

        if (player.isPotionActive(AlchemicalWizardry.customPotionBoost)) {
            int i = player.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
            wantedVelocity += (1 + i) * (0.35);
        }

        player.motionX = vec.xCoord * wantedVelocity;
        player.motionY = vec.yCoord * wantedVelocity;
        player.motionZ = vec.zCoord * wantedVelocity;
        world.playSoundEffect(
                (float) player.posX + 0.5F,
                (float) player.posY + 0.5F,
                (float) player.posZ + 0.5F,
                "random.fizz",
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        player.fallDistance = 0;

        if (!player.capabilities.isCreativeMode) {
            if (!EnergyItems.syphonBatteries(item, player, getEnergyUsed())) {
                if (!world.isRemote) {
                    this.setIsItemUnusable(item, true);
                }
            } else {
                if (!world.isRemote) {
                    this.setIsItemUnusable(item, false);
                }
            }
        } else {
            return item;
        }

        return item;
    }

    public boolean isItemUnusable(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        return tag.getBoolean("unusable");
    }

    public void setIsItemUnusable(ItemStack stack, boolean bool) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        tag.setBoolean("unusable", bool);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        player.fallDistance = 0;
    }

    @Override
    public boolean isUpgrade() {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds() {
        return 50;
    }
}
