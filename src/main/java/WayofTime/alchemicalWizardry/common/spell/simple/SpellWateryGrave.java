package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.WaterProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellWateryGrave extends HomSpell {

    Random itemRand = new Random();

    public SpellWateryGrave() {
        super();
        this.setEnergies(250, 350, 500, 750);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack item, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveRangedEnergy());
        }

        world.spawnEntityInWorld(new WaterProjectile(world, player, 8));
        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        return item;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack item, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveMeleeEnergy());
        }

        if (!world.isRemote) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    world.spawnEntityInWorld(
                            new WaterProjectile(
                                    world,
                                    player,
                                    3,
                                    3,
                                    player.posX,
                                    player.posY + player.getEyeHeight(),
                                    player.posZ,
                                    player.rotationYaw + i * 10F,
                                    player.rotationPitch + j * 5F));
                }
            }
        }

        return item;
    }

    @Override
    public ItemStack onDefensiveRightClick(ItemStack item, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getDefensiveEnergy());
        }

        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                player.posX,
                player.posY,
                player.posZ,
                (player.posX + 1),
                (player.posY + 2),
                (player.posZ + 1)).expand(3, 3, 3);
        List<EntityLivingBase> list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        for (EntityLivingBase entityLiving : list) {
            if (entityLiving instanceof EntityPlayer && entityLiving.equals(player)) {
                continue;
            }

            int x = 1;

            if (entityLiving.isImmuneToFire()) {
                x = 2;
            }

            entityLiving.attackEntityFrom(DamageSource.drown, 2 * x);
            entityLiving.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 100, x - 1));
        }

        for (int i = 0; i < 20; i++) {
            SpellHelper.sendParticleToAllAround(
                    world,
                    player.posX,
                    player.posY,
                    player.posZ,
                    30,
                    world.provider.dimensionId,
                    "mobSpell",
                    player.posX + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    player.posY + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    player.posZ + (itemRand.nextFloat() - itemRand.nextFloat()) * 3,
                    0.0F,
                    0.410F,
                    1.0F);
        }

        return item;
    }

    @Override
    public ItemStack onEnvironmentalRightClick(ItemStack item, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getEnvironmentalEnergy());
        }

        int range = 2;

        if (!world.isRemote) {
            for (int i = -range; i <= range; i++) {
                for (int j = -range; j <= range; j++) {
                    if (world.isAirBlock((int) player.posX + i, (int) player.posY, (int) player.posZ + j)) {
                        world.setBlock((int) player.posX + i, (int) player.posY, (int) player.posZ + j, Blocks.water);
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
