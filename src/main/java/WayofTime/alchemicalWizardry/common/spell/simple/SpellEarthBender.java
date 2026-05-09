package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.MudProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellEarthBender extends HomSpell {

    Random itemRand = new Random();

    public SpellEarthBender() {
        super();
        this.setEnergies(100, 150, 350, 200);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveRangedEnergy());
        }

        world.spawnEntityInWorld(new MudProjectile(world, player, 8, false));
        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
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

        if (!world.isRemote) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    world.spawnEntityInWorld(
                            new MudProjectile(
                                    world,
                                    player,
                                    3,
                                    3,
                                    player.posX,
                                    player.posY + player.getEyeHeight(),
                                    player.posZ,
                                    player.rotationYaw + i * 10F,
                                    player.rotationPitch + j * 5F,
                                    true));
                }
            }
        }

        return item;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getDefensiveEnergy());
        }

        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;
        int posX = (int) xCoord;
        int posY = (int) yCoord;
        int posZ = (int) zCoord;
        Block blockID = Blocks.stone;

        if (world.isAirBlock(posX, posY + 3, posZ)) {
            world.setBlock(posX, posY + 3, posZ, Blocks.glass);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (world.isAirBlock(posX + i - 1, posY + j, posZ - 2)) {
                    world.setBlock(posX + i - 1, posY + j, posZ - 2, blockID);
                }

                if (world.isAirBlock(posX + 2, posY + j, posZ - 1 + i)) {
                    world.setBlock(posX + 2, posY + j, posZ - 1 + i, blockID);
                }

                if (world.isAirBlock(posX - i + 1, posY + j, posZ + 2)) {
                    world.setBlock(posX - i + 1, posY + j, posZ + 2, blockID);
                }

                if (world.isAirBlock(posX - 2, posY + j, posZ + 1 - i)) {
                    world.setBlock(posX - 2, posY + j, posZ + 1 - i, blockID);
                }

                {
                    if (world.isAirBlock(posX - 1 + i, posY + 3, posZ - 1 + j)) {
                        world.setBlock(posX - 1 + i, posY + 3, posZ - 1 + j, blockID);
                    }
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            SpellHelper.sendParticleToAllAround(
                    world,
                    xCoord,
                    yCoord,
                    zCoord,
                    30,
                    world.provider.dimensionId,
                    "mobSpell",
                    xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    0.0F,
                    0.410F,
                    1.0F);
        }

        return item;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getEnvironmentalEnergy());
        }

        int range = 3;

        if (!world.isRemote) {
            for (int i = -range; i <= range; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -range; k <= range; k++) {
                        if (world.getBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ + k)
                                == Blocks.water
                                || world.getBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ + k)
                                        == Blocks.flowing_water) {
                            int x = world.rand.nextInt(2);

                            if (x == 0) {
                                world.setBlock(
                                        (int) player.posX + i,
                                        (int) player.posY + j,
                                        (int) player.posZ + k,
                                        Blocks.sand);
                            } else {
                                world.setBlock(
                                        (int) player.posX + i,
                                        (int) player.posY + j,
                                        (int) player.posZ + k,
                                        Blocks.dirt);
                            }
                        }
                    }
                }
            }
        }

        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;

        for (int i = 0; i < 16; i++) {
            SpellHelper.sendParticleToAllAround(
                    world,
                    xCoord,
                    yCoord,
                    zCoord,
                    30,
                    world.provider.dimensionId,
                    "mobSpell",
                    xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    0.0F,
                    0.410F,
                    1.0F);
        }

        return item;
    }
}
