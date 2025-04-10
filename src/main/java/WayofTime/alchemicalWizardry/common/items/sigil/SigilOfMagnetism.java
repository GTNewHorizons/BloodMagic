package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
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

    private int tickDelay = 300;

    public SigilOfMagnetism() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilMagnetismCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofmagnetism.desc"));
        addBindingInformation(par1ItemStack, par3List);
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

        ISigil.toggleSigil(par1ItemStack, par2World, par3EntityPlayer, getEnergyUsed(), tickDelay);

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

            IBindable.passiveDrain(par1ItemStack, par2World, par3EntityPlayer, tickDelay, getEnergyUsed());

            int range = 5;
            int verticalRange = 5;
            float posX = Math.round(par3Entity.posX);
            float posY = (float) (par3Entity.posY - par3Entity.getEyeHeight());
            float posZ = Math.round(par3Entity.posZ);
            List<EntityItem> entities = par3EntityPlayer.worldObj.getEntitiesWithinAABB(
                    EntityItem.class,
                    AxisAlignedBB.getBoundingBox(
                            posX - 0.5f,
                            posY - 0.5f,
                            posZ - 0.5f,
                            posX + 0.5f,
                            posY + 0.5f,
                            posZ + 0.5f).expand(range, verticalRange, range));
            List<EntityXPOrb> xpOrbs = par3EntityPlayer.worldObj.getEntitiesWithinAABB(
                    EntityXPOrb.class,
                    AxisAlignedBB.getBoundingBox(
                            posX - 0.5f,
                            posY - 0.5f,
                            posZ - 0.5f,
                            posX + 0.5f,
                            posY + 0.5f,
                            posZ + 0.5f).expand(range, verticalRange, range));

            for (EntityItem entity : entities) {
                if (entity != null && !par2World.isRemote) {
                    entity.onCollideWithPlayer(par3EntityPlayer);
                }
            }

            for (EntityXPOrb xpOrb : xpOrbs) {
                if (xpOrb != null && !par2World.isRemote) {
                    xpOrb.onCollideWithPlayer(par3EntityPlayer);
                }
            }
        }
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
