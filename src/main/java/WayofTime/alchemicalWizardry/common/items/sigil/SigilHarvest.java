package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.harvest.HarvestRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilHarvest extends EnergyItems implements IHolding, ArmourUpgrade, ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    public SigilHarvest() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilHarvestCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.harvestsigil.desc"));
        addBindingInformation(item, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:HarvestGoddessSigil_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:HarvestGoddessSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:HarvestGoddessSigil_deactivated");
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

        toggleSigil(item, world, player);

        return item;
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean held) {
        if ((!(entity instanceof EntityPlayer player)) || world.isRemote) {
            return;
        }

        if (IBindable.isActive(item)) {
            int range = 3;
            int verticalRange = 1;
            int posX = (int) Math.round(player.posX - 0.5f);
            int posY = (int) player.posY;
            int posZ = (int) Math.round(player.posZ - 0.5f);

            for (int x = posX - range; x <= posX + range; x++) {
                for (int z = posZ - range; z <= posZ + range; z++) {
                    for (int y = posY - verticalRange; y <= posY + verticalRange; y++) {
                        HarvestRegistry.harvestBlock(world, x, y, z);
                    }
                }
            }
        }

        checkPassiveDrain(item, world, player);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        if (world.isRemote) {
            return;
        }
        int range = 3;
        int verticalRange = 1;
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int x = posX - range; x <= posX + range; x++) {
            for (int z = posZ - range; z <= posZ + range; z++) {
                for (int y = posY - verticalRange; y <= posY + verticalRange; y++) {
                    HarvestRegistry.harvestBlock(world, x, y, z);
                }
            }
        }
    }

    @Override
    public boolean isUpgrade() {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds() {
        return 500;
    }
}
