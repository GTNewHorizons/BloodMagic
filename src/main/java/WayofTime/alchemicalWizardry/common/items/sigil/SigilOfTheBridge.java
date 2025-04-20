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
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfTheBridge extends EnergyItems implements ArmourUpgrade, ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    public SigilOfTheBridge() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilBridgeCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofthebridge.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofthebridge.desc2"));
        addBindingInformation(par1ItemStack, par3List);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BridgeSigil_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BridgeSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:BridgeSigil_deactivated");
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

        toggleSigil(par1ItemStack, par2World, par3EntityPlayer);

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
            checkPassiveDrain(par1ItemStack, par2World, par3EntityPlayer);
            if (!par3EntityPlayer.onGround && !par3EntityPlayer.isSneaking()) {
                return;
            }

            int range = 2;
            int verticalOffset = -1;

            if (par3EntityPlayer.isSneaking()) {
                verticalOffset--;
            }

            if (par2World.isRemote) {
                verticalOffset--;
            }

            int posX = (int) Math.round(par3Entity.posX - 0.5f);
            int posY = (int) par3Entity.posY;
            int posZ = (int) Math.round(par3Entity.posZ - 0.5f);
            int incremented = 0;

            for (int ix = posX - range; ix <= posX + range; ix++) {
                for (int iz = posZ - range; iz <= posZ + range; iz++) {
                    {
                        Block block = par2World.getBlock(ix, posY + verticalOffset, iz);

                        if (par2World.isAirBlock(ix, posY + verticalOffset, iz)) {
                            par2World.setBlock(ix, posY + verticalOffset, iz, ModBlocks.spectralBlock, 0, 3);

                            TileEntity tile = par2World.getTileEntity(ix, posY + verticalOffset, iz);
                            if (tile instanceof TESpectralBlock) {
                                ((TESpectralBlock) tile).setDuration(100);
                            }

                            if (par2World.rand.nextInt(2) == 0) {
                                incremented++;
                            }
                        } else if (block == ModBlocks.spectralBlock) {
                            TileEntity tile = par2World.getTileEntity(ix, posY + verticalOffset, iz);
                            if (tile instanceof TESpectralBlock) {
                                ((TESpectralBlock) tile).setDuration(100);
                            }
                        }
                    }
                }
            }

            this.incrimentLPUSed(par1ItemStack, incremented);
        }
    }

    public int getLPUsed(ItemStack par1ItemStack) {
        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.getTagCompound().getInteger("LPUsed");
    }

    public void incrimentLPUSed(ItemStack par1ItemStack, int addedLP) {
        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound()
                .setInteger("LPUsed", par1ItemStack.getTagCompound().getInteger("LPUsed") + addedLP);
    }

    public void setLPUsed(ItemStack par1ItemStack, int newLP) {
        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setInteger("LPUsed", newLP);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        if (!player.onGround && !player.isSneaking()) {
            return;
        }

        int range = 2;
        int verticalOffset = -2;

        if (player.isSneaking()) {
            verticalOffset--;
        }

        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);
        for (int ix = posX - range; ix <= posX + range; ix++) {
            for (int iz = posZ - range; iz <= posZ + range; iz++) {
                Block block = world.getBlock(ix, posY + verticalOffset, iz);

                if (world.isAirBlock(ix, posY + verticalOffset, iz)) {
                    world.setBlock(ix, posY + verticalOffset, iz, ModBlocks.spectralBlock, 0, 3);

                    TileEntity tile = world.getTileEntity(ix, posY + verticalOffset, iz);
                    if (tile instanceof TESpectralBlock) {
                        ((TESpectralBlock) tile).setDuration(100);
                    }
                } else if (block == ModBlocks.spectralBlock) {
                    TileEntity tile = world.getTileEntity(ix, posY + verticalOffset, iz);
                    if (tile instanceof TESpectralBlock) {
                        ((TESpectralBlock) tile).setDuration(100);
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
        return 100;
    }
}
