package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilLava extends ItemBucket implements ArmourUpgrade, ISigil {

    /**
     * field for checking if the bucket has been filled.
     */
    private Block isFull = Blocks.lava;

    private int energyUsed;

    public SigilLava() {
        super(Blocks.lava);
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilLavaCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:LavaSigil");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.lavasigil.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.lavasigil.desc2"));
        addBindingInformation(par1ItemStack, par3List);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (world.isRemote || !IBindable.checkAndSetItemOwner(stack, player) || player.isSneaking()) {
            return false;
        }

        if (!world.canMineBlock(player, x, y, z)) {
            return false;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFluidHandler) {
            FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
            int amount = ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(side), fluid, false);

            if (amount > 0 && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
                ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(side), fluid, true);
            }

            return false;
        } else if (tile instanceof TESocket) {
            return false;
        }

        {
            if (side == 0) {
                --y;
            }

            if (side == 1) {
                ++y;
            }

            if (side == 2) {
                --z;
            }

            if (side == 3) {
                ++z;
            }

            if (side == 4) {
                --x;
            }

            if (side == 5) {
                ++x;
            }

            if (!player.canPlayerEdit(x, y, z, side, stack)) {
                return false;
            }

            if (this.canPlaceContainedLiquid(world, x, y, z, x, y, z)
                    && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
                return this.tryPlaceContainedLiquid(world, x, y, z, x, y, z);
            }
        }

        return false;
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9,
            int par10) {
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid()) {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10) == Blocks.lava
                || par1World.getBlock(par8, par9, par10) == Blocks.flowing_lava)
                && par1World.getBlockMetadata(par8, par9, par10) == 0) {
                    return false;
                } else {
                    par1World.setBlock(par8, par9, par10, this.isFull, 0, 3);
                    return true;
                }
    }

    public boolean canPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9,
            int par10) {
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid()) {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10) == Blocks.lava
                || par1World.getBlock(par8, par9, par10) == Blocks.flowing_lava)
                && par1World.getBlockMetadata(par8, par9, par10) == 0) {
                    return false;
                } else {
                    return true;
                }
    }

    protected void setEnergyUsed(int par1int) {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed() {
        return this.energyUsed;
    }

    protected boolean syphonBatteries(ItemStack ist, EntityPlayer player, int damageToBeDone) {
        if (!player.capabilities.isCreativeMode) {
            boolean usedBattery = false;
            IInventory inventory = player.inventory;

            for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);

                if (stack == null) {
                    continue;
                }
                if (stack.getItem() instanceof EnergyBattery && !usedBattery) {
                    if (stack.getItemDamage() <= stack.getMaxDamage() - damageToBeDone) {
                        stack.setItemDamage(stack.getItemDamage() + damageToBeDone);
                        usedBattery = true;
                    }
                }
            }

            return usedBattery;
        } else {
            return true;
        }
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 9, true));
        player.extinguish();
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
