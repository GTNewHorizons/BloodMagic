package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilWater extends ItemBucket implements ArmourUpgrade, ISigil {

    private int energyUsed;

    public SigilWater() {
        super(Blocks.water);
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilWaterCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterSigil");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.watersigil.desc"));
        addBindingInformation(stack, tooltip);
    }

    @Override
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
            FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
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

            if (this.canPlaceContainedLiquid(world, x, y, z)
                    && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
                return this.tryPlaceContainedLiquid(world, x, y, z);
            }
        }

        return false;
    }

    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        if (!world.isAirBlock(x, y, z) && world.getBlock(x, y, z).getMaterial().isSolid()) {
            return false;
        }
        if ((world.getBlock(x, y, z) == Blocks.water || world.getBlock(x, y, z) == Blocks.flowing_water)
                && world.getBlockMetadata(x, y, z) == 0) {
            return false;
        }
        if (world.provider.isHellWorld) {
            world.playSoundEffect(
                    x + 0.5D,
                    y + 0.5D,
                    z + 0.5D,
                    "random.fizz",
                    0.5F,
                    2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

            for (int l = 0; l < 8; ++l) {
                world.spawnParticle(
                        "largesmoke",
                        (double) x + Math.random(),
                        (double) y + Math.random(),
                        (double) z + Math.random(),
                        0.0D,
                        0.0D,
                        0.0D);
            }
        } else {
            world.setBlock(x, y, z, Blocks.water, 0, 3);
            world.markBlockForUpdate(x, y, z);
        }

        return true;
    }

    public boolean canPlaceContainedLiquid(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (!world.isAirBlock(x, y, z) && block.getMaterial().isSolid()) {
            return false;
        }
        return (block != Blocks.water && block != Blocks.flowing_water) || world.getBlockMetadata(x, y, z) != 0;
    }

    protected void setEnergyUsed(int par1int) {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed() {
        return this.energyUsed;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 9, true));
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
