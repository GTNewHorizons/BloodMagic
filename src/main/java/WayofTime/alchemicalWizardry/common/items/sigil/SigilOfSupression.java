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

    private final int radius = 5;
    private final int refresh = 100;

    public SigilOfSupression() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilSuppressionCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofsupression.desc"));
        addBindingInformation(item, tooltip);
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
    public IIcon getIconFromDamage(int meta) {
        if (meta == 1) {
            return activeIcon;
        } else {
            return passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || SpellHelper.isFakePlayer(world, player)) {
            return item;
        }

        if (player.isSneaking()) {
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

        if (SpellHelper.isFakePlayer(world, player)) {
            return;
        }

        if (!IBindable.isActive(item) || (world.isRemote)) {
            return;
        }
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
                        if (tile instanceof TESpectralContainer container) {
                            container.resetDuration(refresh);
                        }
                    }
                }
            }
        }
        checkPassiveDrain(item, world, player);
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
