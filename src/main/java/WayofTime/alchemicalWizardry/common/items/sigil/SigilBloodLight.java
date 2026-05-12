package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityBloodLightProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilBloodLight extends EnergyItems implements IHolding, ArmourUpgrade, ISigil {

    public SigilBloodLight() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilBloodLightCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.bloodlightsigil.desc"));
        addBindingInformation(item, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodLightSigil");
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int par4, int par5, int par6, int par7,
            float par8, float par9, float par10) {
        if (!IBindable.checkAndSetItemOwner(item, player)
                || !EnergyItems.syphonBatteries(item, player, getEnergyUsed())) {
            return true;
        }

        if (world.isRemote) {
            return true;
        }

        if (par7 == 0 && world.isAirBlock(par4, par5 - 1, par6)) {
            world.setBlock(par4, par5 - 1, par6, ModBlocks.blockBloodLight);
        }

        if (par7 == 1 && world.isAirBlock(par4, par5 + 1, par6)) {
            world.setBlock(par4, par5 + 1, par6, ModBlocks.blockBloodLight);
        }

        if (par7 == 2 && world.isAirBlock(par4, par5, par6 - 1)) {
            world.setBlock(par4, par5, par6 - 1, ModBlocks.blockBloodLight);
        }

        if (par7 == 3 && world.isAirBlock(par4, par5, par6 + 1)) {
            world.setBlock(par4, par5, par6 + 1, ModBlocks.blockBloodLight);
        }

        if (par7 == 4 && world.isAirBlock(par4 - 1, par5, par6)) {
            world.setBlock(par4 - 1, par5, par6, ModBlocks.blockBloodLight);
        }

        if (par7 == 5 && world.isAirBlock(par4 + 1, par5, par6)) {
            world.setBlock(par4 + 1, par5, par6, ModBlocks.blockBloodLight);
        }

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!EnergyItems.syphonBatteries(item, player, getEnergyUsed() * 5)) {
            return item;
        }

        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityBloodLightProjectile(world, player, 10));
        }

        return item;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 9, true));
    }

    @Override
    public boolean isUpgrade() {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds() {
        return 25;
    }
}
