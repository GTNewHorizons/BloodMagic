package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofelementalaffinity.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofelementalaffinity.desc2"));
        addBindingInformation(item, tooltip);
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
    public IIcon getIconFromDamage(int meta) {
        if (meta == 1) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (toggleSigil(item, world, player)) {
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true));
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 0, true));
        }

        return item;
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean held) {
        if (!(entity instanceof EntityPlayer player)) {
            return;
        }

        if (IBindable.isActive(item)) {
            player.fallDistance = 0;
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 0, true));
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 0, true));
        }

        checkPassiveDrain(item, world, player);
    }
}
