package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofgrowth.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofgrowth.desc2"));
        addBindingInformation(item, tooltip);
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
    public IIcon getIconFromDamage(int meta) {
        if (meta == 1) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int par7,
            float par8, float par9, float par10) {
        if (IBindable.checkAndSetItemOwner(item, player) && applyBonemeal(world, x, y, z, player)) {
            EnergyItems.syphonBatteries(item, player, getEnergyUsed());

            if (world.isRemote) {
                world.playAuxSFX(2005, x, y, z, 0);
                return true;
            }

            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (world.isRemote) {
            return item;
        }

        toggleSigil(item, world, player);

        return item;
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int par4, boolean par5) {
        if (!(entity instanceof EntityPlayer player) || world.isRemote) {
            return;
        }

        if (!IBindable.isActive(item)) {
            return;
        }

        checkPassiveDrain(item, world, player);
        int range = 3;
        int verticalRange = 2;
        int posX = (int) Math.round(entity.posX - 0.5f);
        int posY = (int) entity.posY;
        int posZ = (int) Math.round(entity.posZ - 0.5f);

        for (int x = posX - range; x <= posX + range; x++) {
            for (int z = posZ - range; z <= posZ + range; z++) {
                for (int y = posY - verticalRange; y <= posY + verticalRange; y++) {
                    Block block = world.getBlock(x, y, z);

                    if ((block instanceof IPlantable || block instanceof IGrowable) && world.rand.nextInt(50) == 0) {
                        block.updateTick(world, x, y, z, world.rand);
                    }
                }
            }
        }
    }

    public static boolean applyBonemeal(World world, int x, int y, int z, EntityPlayer player) {
        Block block = world.getBlock(x, y, z);

        BonemealEvent event = new BonemealEvent(player, world, block, x, y, z);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        if (event.getResult() == Result.ALLOW) {
            return true;
        }

        if (block instanceof IGrowable igrowable && igrowable.func_149851_a(world, x, y, z, world.isRemote)) {
            if (!world.isRemote && igrowable.func_149852_a(world, world.rand, x, y, z)) {
                igrowable.func_149853_b(world, world.rand, x, y, z);
            }

            return true;
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

        for (int x = posX - range; x <= posX + range; x++) {
            for (int z = posZ - range; z <= posZ + range; z++) {
                for (int y = posY - verticalRange; y <= posY + verticalRange; y++) {
                    Block block = world.getBlock(x, y, z);

                    if (block instanceof IPlantable && world.rand.nextInt(100) == 0) {
                        block.updateTick(world, x, y, z, world.rand);
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
