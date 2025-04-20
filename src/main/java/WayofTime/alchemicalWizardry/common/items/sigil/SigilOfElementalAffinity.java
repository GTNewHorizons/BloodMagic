package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfElementalAffinity extends EnergyItems implements ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    public SigilOfElementalAffinity() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilElementalAffinityCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofelementalaffinity.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofelementalaffinity.desc2"));
        addBindingInformation(par1ItemStack, par3List);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfTheFastMiner");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:ElementalSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:ElementalSigil_deactivated");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (IBindable.isActive(stack)) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1) {
        if (par1 == 1) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking()) {
            return par1ItemStack;
        }

        if (toggleSigil(par1ItemStack, par2World, par3EntityPlayer)) {
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 0, true));
        }

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (IBindable.isActive(par1ItemStack)) {
            par3EntityPlayer.fallDistance = 0;
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true));
            par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 0, true));
        }

        checkPassiveDrain(par1ItemStack, par2World, par3EntityPlayer);
    }
}
