package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfMagnetism extends EnergyItems implements ArmourUpgrade, IHolding, ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    public SigilOfMagnetism() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilMagnetismCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofmagnetism.desc"));
        addBindingInformation(item, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfMagnetism_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfMagnetism_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfMagnetism_deactivated");
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
        if (!(entity instanceof EntityPlayer player)) {
            return;
        }

        if (!IBindable.isActive(item)) {
            return;
        }

        checkPassiveDrain(item, world, player);

        int range = 5;
        int verticalRange = 5;
        float posX = Math.round(player.posX);
        float posY = (float) (player.posY - player.getEyeHeight());
        float posZ = Math.round(player.posZ);
        List<EntityItem> entities = player.worldObj.getEntitiesWithinAABB(
                EntityItem.class,
                AxisAlignedBB
                        .getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f)
                        .expand(range, verticalRange, range));
        List<EntityXPOrb> xpOrbs = player.worldObj.getEntitiesWithinAABB(
                EntityXPOrb.class,
                AxisAlignedBB
                        .getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f)
                        .expand(range, verticalRange, range));

        for (EntityItem entityItem : entities) {
            if (entityItem != null && !world.isRemote) {
                entityItem.onCollideWithPlayer(player);
            }
        }

        for (EntityXPOrb xpOrb : xpOrbs) {
            if (xpOrb != null && !world.isRemote) {
                xpOrb.onCollideWithPlayer(player);
            }
        }
    }

    @Override
    public int drainTicks() {
        return 300;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        int range = 5;
        int verticalRange = 5;
        float posX = Math.round(player.posX);
        float posY = (float) (player.posY - player.getEyeHeight());
        float posZ = Math.round(player.posZ);
        List<EntityItem> entities = player.worldObj.getEntitiesWithinAABB(
                EntityItem.class,
                AxisAlignedBB
                        .getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f)
                        .expand(range, verticalRange, range));
        List<EntityXPOrb> xpOrbs = player.worldObj.getEntitiesWithinAABB(
                EntityXPOrb.class,
                AxisAlignedBB
                        .getBoundingBox(posX - 0.5f, posY - 0.5f, posZ - 0.5f, posX + 0.5f, posY + 0.5f, posZ + 0.5f)
                        .expand(range, verticalRange, range));

        for (EntityItem entity : entities) {
            if (entity != null && !world.isRemote) {
                entity.onCollideWithPlayer(player);
            }
        }

        for (EntityXPOrb xpOrb : xpOrbs) {
            if (xpOrb != null && !world.isRemote) {
                xpOrb.onCollideWithPlayer(player);
            }
        }
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
