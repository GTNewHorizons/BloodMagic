package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfGrowth extends EnergyItems implements ArmourUpgrade, ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    public SigilOfGrowth() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilGrowthCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofgrowth.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.sigilofgrowth.desc2"));
        addBindingInformation(par1ItemStack, par3List);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:GrowthSigil_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:GrowthSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:GrowthSigil_deactivated");
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
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
            int par5, int par6, int par7, float par8, float par9, float par10) {
        if (IBindable.checkAndSetItemOwner(par1ItemStack, par2EntityPlayer)) {
            if (applyBonemeal(par1ItemStack, par3World, par4, par5, par6, par2EntityPlayer)) {
                EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());

                if (par3World.isRemote) {
                    par3World.playAuxSFX(2005, par4, par5, par6, 0);
                    return true;
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.isSneaking()) {
            return par1ItemStack;
        }

        if (par2World.isRemote) {
            return par1ItemStack;
        }

        toggleSigil(par1ItemStack, par2World, par3EntityPlayer);

        return par1ItemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!(par3Entity instanceof EntityPlayer) || par2World.isRemote) {
            return;
        }

        if (par1ItemStack.getTagCompound() == null) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (IBindable.isActive(par1ItemStack)) {

            checkPassiveDrain(par1ItemStack, par2World, (EntityPlayer) par3Entity);
            int range = 3;
            int verticalRange = 2;
            int posX = (int) Math.round(par3Entity.posX - 0.5f);
            int posY = (int) par3Entity.posY;
            int posZ = (int) Math.round(par3Entity.posZ - 0.5f);

            for (int ix = posX - range; ix <= posX + range; ix++) {
                for (int iz = posZ - range; iz <= posZ + range; iz++) {
                    for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++) {
                        Block block = par2World.getBlock(ix, iy, iz);

                        if (block instanceof IPlantable || block instanceof IGrowable) {
                            if (par2World.rand.nextInt(50) == 0) {
                                block.updateTick(par2World, ix, iy, iz, par2World.rand);
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean applyBonemeal(ItemStack p_150919_0_, World p_150919_1_, int p_150919_2_, int p_150919_3_,
            int p_150919_4_, EntityPlayer player) {
        Block block = p_150919_1_.getBlock(p_150919_2_, p_150919_3_, p_150919_4_);

        BonemealEvent event = new BonemealEvent(player, p_150919_1_, block, p_150919_2_, p_150919_3_, p_150919_4_);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        if (event.getResult() == Result.ALLOW) {
            return true;
        }

        if (block instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) block;

            if (igrowable.func_149851_a(p_150919_1_, p_150919_2_, p_150919_3_, p_150919_4_, p_150919_1_.isRemote)) {
                if (!p_150919_1_.isRemote) {
                    if (igrowable.func_149852_a(p_150919_1_, p_150919_1_.rand, p_150919_2_, p_150919_3_, p_150919_4_)) {
                        igrowable.func_149853_b(p_150919_1_, p_150919_1_.rand, p_150919_2_, p_150919_3_, p_150919_4_);
                    }
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public int drainTicks() {
        return 100;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
        if (world.isRemote) {
            return;
        }

        int range = 5;
        int verticalRange = 2;
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++) {
            for (int iz = posZ - range; iz <= posZ + range; iz++) {
                for (int iy = posY - verticalRange; iy <= posY + verticalRange; iy++) {
                    Block block = world.getBlock(ix, iy, iz);

                    if (block instanceof IPlantable) {
                        if (world.rand.nextInt(100) == 0) {
                            block.updateTick(world, ix, iy, iz, world.rand);
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
        return 50;
    }
}
