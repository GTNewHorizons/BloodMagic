package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilLava extends ItemBucket implements ArmourUpgrade, ISigil {

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
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.lavasigil.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.lavasigil.desc2"));
        addBindingInformation(item, tooltip);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
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
        if (tile instanceof IFluidHandler handler) {
            FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
            int amount = handler.fill(ForgeDirection.getOrientation(side), fluid, false);

            if (amount > 0 && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
                handler.fill(ForgeDirection.getOrientation(side), fluid, true);
            }

            return false;
        } else if (tile instanceof TESocket) {
            return false;
        }

        switch (side) {
            case 0 -> --y;
            case 1 -> ++y;
            case 2 -> --z;
            case 3 -> ++z;
            case 4 -> --x;
            case 5 -> ++x;
        }

        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        if (this.canPlaceContainedLiquid(world, x, y, z)
                && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
            return this.tryPlaceContainedLiquid(world, x, y, z);
        }

        return false;
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    @Override
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        world.setBlock(x, y, z, Blocks.lava, 0, 3);
        return true;
    }

    public boolean canPlaceContainedLiquid(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (!world.isAirBlock(x, y, z) && block.getMaterial().isSolid()) {
            return false;
        }
        return (block != Blocks.lava && block != Blocks.flowing_lava) || world.getBlockMetadata(x, y, z) != 0;
    }

    protected void setEnergyUsed(int energy) {
        this.energyUsed = energy;
    }

    protected int getEnergyUsed() {
        return this.energyUsed;
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
