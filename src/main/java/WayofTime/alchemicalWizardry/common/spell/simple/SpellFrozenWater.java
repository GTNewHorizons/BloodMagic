package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SpellFrozenWater extends HomSpell {

    public Random itemRand = new Random();

    public SpellFrozenWater() {
        super();
        this.setEnergies(100, 200, 150, 100);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveRangedEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new IceProjectile(world, player, 6));
        }

        return item;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveMeleeEnergy());
        }

        for (int i = -2; i <= 2; i++) {
            world.spawnEntityInWorld(
                    new IceProjectile(
                            world,
                            player,
                            6,
                            2,
                            player.posX,
                            player.posY + player.getEyeHeight(),
                            player.posZ,
                            player.rotationYaw + i * 5F,
                            player.rotationPitch));
        }

        return item;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, getDefensiveEnergy());
        }

        float yaw = player.rotationYaw;
        float pitch = player.rotationPitch;
        int range = 2;

        if (pitch > 40F) {
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    if (world.isAirBlock((int) player.posX + i, (int) player.posY - 1, (int) player.posZ + j)) {
                        world.setBlock((int) player.posX + i, (int) player.posY - 1, (int) player.posZ + j, Blocks.ice);
                    }
                }
            }

            return item;
        } else if (pitch < -40F) {
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    if (world.isAirBlock((int) player.posX + i, (int) player.posY + 3, (int) player.posZ + j)) {
                        world.setBlock((int) player.posX + i, (int) player.posY + 3, (int) player.posZ + j, Blocks.ice);
                    }
                }
            }

            return item;
        }

        if ((yaw >= 315 && yaw < 360) || (yaw >= 0 && yaw < 45)) {
            for (int i = -range; i <= range; i++) {
                for (int j = 0; j < range * 2 + 1; j++) {
                    if (world.isAirBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ + 2)) {
                        world.setBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ + 2, Blocks.ice);
                    }
                }
            }
        } else if (yaw >= 45 && yaw < 135) {
            for (int i = -range; i <= range; i++) {
                for (int j = 0; j < range * 2 + 1; j++) {
                    if (world.isAirBlock((int) player.posX - 2, (int) player.posY + j, (int) player.posZ + i)) {
                        world.setBlock((int) player.posX - 2, (int) player.posY + j, (int) player.posZ + i, Blocks.ice);
                    }
                }
            }
        } else if (yaw >= 135 && yaw < 225) {
            for (int i = -range; i <= range; i++) {
                for (int j = 0; j < range * 2 + 1; j++) {
                    if (world.isAirBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ - 2)) {
                        world.setBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ - 2, Blocks.ice);
                    }
                }
            }
        } else {
            for (int i = -range; i <= range; i++) {
                for (int j = 0; j < range * 2 + 1; j++) {
                    if (world.isAirBlock((int) player.posX + 2, (int) player.posY + j, (int) player.posZ + i)) {
                        world.setBlock((int) player.posX + 2, (int) player.posY + j, (int) player.posZ + i, Blocks.ice);
                    }
                }
            }
        }

        return item;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        int radius = 3;

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    Block block = world
                            .getBlock((int) player.posX + i - 1, (int) player.posY + j, (int) player.posZ + k);
                    if (block == Blocks.water || block == Blocks.flowing_water) {
                        world.setBlock(
                                (int) player.posX + i - 1,
                                (int) player.posY + j,
                                (int) player.posZ + k,
                                Blocks.ice);
                    }
                }
            }
        }
        return item;
    }
}
