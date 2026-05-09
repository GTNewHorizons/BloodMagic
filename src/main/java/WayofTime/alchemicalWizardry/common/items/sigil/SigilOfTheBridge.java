package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofthebridge.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofthebridge.desc2"));
        addBindingInformation(item, tooltip);
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
        if (!player.onGround && !player.isSneaking()) {
            return;
        }

        int range = 2;
        int verticalOffset = -1;

        if (player.isSneaking()) {
            verticalOffset--;
        }

        if (world.isRemote) {
            verticalOffset--;
        }

        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        for (int ix = posX - range; ix <= posX + range; ix++) {
            for (int iz = posZ - range; iz <= posZ + range; iz++) {
                {
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
        for (int x = posX - range; x <= posX + range; x++) {
            for (int z = posZ - range; z <= posZ + range; z++) {
                Block block = world.getBlock(x, posY + verticalOffset, z);

                if (world.isAirBlock(x, posY + verticalOffset, z)) {
                    world.setBlock(x, posY + verticalOffset, z, ModBlocks.spectralBlock, 0, 3);
                }
                if (block == ModBlocks.spectralBlock) {
                    TileEntity tile = world.getTileEntity(x, posY + verticalOffset, z);
                    if (tile instanceof TESpectralBlock b) {
                        b.setDuration(100);
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
