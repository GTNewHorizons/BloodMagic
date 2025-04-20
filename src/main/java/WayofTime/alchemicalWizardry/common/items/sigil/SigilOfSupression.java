package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfSupression extends EnergyItems implements ArmourUpgrade, ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    private int radius = 5;
    private int refresh = 100;

    public SigilOfSupression() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilSuppressionCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofsupression.desc"));
        addBindingInformation(par1ItemStack, par3List);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfSupression_deactivated");
        activeIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfSupression_activated");
        passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfSupression_deactivated");
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
            return activeIcon;
        } else {
            return passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer)
                || SpellHelper.isFakePlayer(par2World, par3EntityPlayer)) {
            return par1ItemStack;
        }

        if (par3EntityPlayer.isSneaking()) {
            return par1ItemStack;
        }

        toggleSigil(par1ItemStack, par2World, par3EntityPlayer);

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer)) {
            return;
        }

        if (SpellHelper.isFakePlayer(par2World, (EntityPlayer) par3Entity)) {
            return;
        }

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (IBindable.isActive(par1ItemStack) && (!par2World.isRemote)) {
            Vec3 blockVec = SpellHelper.getEntityBlockVector(par3EntityPlayer);
            int x = (int) blockVec.xCoord;
            int y = (int) blockVec.yCoord;
            int z = (int) blockVec.zCoord;

            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    for (int k = -radius; k <= radius; k++) {
                        if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f)) {
                            continue;
                        }

                        Block block = par2World.getBlock(x + i, y + j, z + k);

                        if (SpellHelper.isBlockFluid(block)) {
                            if (par2World.getTileEntity(x + i, y + j, z + k) != null) {
                                par2World.setBlockToAir(x + i, y + j, z + k);
                            }
                            TESpectralContainer.createSpectralBlockAtLocation(par2World, x + i, y + j, z + k, refresh);
                        } else {
                            TileEntity tile = par2World.getTileEntity(x + i, y + j, z + k);
                            if (tile instanceof TESpectralContainer) {
                                ((TESpectralContainer) tile).resetDuration(refresh);
                            }
                        }
                    }
                }
            }
        }
        checkPassiveDrain(par1ItemStack, par2World, par3EntityPlayer);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        Vec3 blockVec = SpellHelper.getEntityBlockVector(player);
        int x = (int) blockVec.xCoord;
        int y = (int) blockVec.yCoord;
        int z = (int) blockVec.zCoord;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f)) {
                        continue;
                    }

                    Block block = world.getBlock(x + i, y + j, z + k);

                    if (SpellHelper.isBlockFluid(block)) {
                        if (world.getTileEntity(x + i, y + j, z + k) != null) {
                            world.setBlockToAir(x + i, y + j, z + k);
                        }
                        TESpectralContainer.createSpectralBlockAtLocation(world, x + i, y + j, z + k, refresh);
                    } else {
                        TileEntity tile = world.getTileEntity(x + i, y + j, z + k);
                        if (tile instanceof TESpectralContainer) {
                            ((TESpectralContainer) tile).resetDuration(refresh);
                        }
                    }
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
        return 200;
    }
}
