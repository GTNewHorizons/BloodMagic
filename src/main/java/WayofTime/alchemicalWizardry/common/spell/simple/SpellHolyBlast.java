package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.HolyProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellHolyBlast extends HomSpell {

    Random itemRand = new Random();

    public SpellHolyBlast() {
        super();
        this.setEnergies(100, 300, 500, 400);
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
            world.spawnEntityInWorld(new HolyProjectile(world, player, 8));
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

        int distance = 2;
        double yaw = player.rotationYaw / 180 * Math.PI;
        double pitch = player.rotationPitch / 180 * Math.PI;
        double v = Math.sin(yaw) * Math.cos(pitch) * (-distance);
        double v1 = Math.cos(yaw) * Math.cos(pitch) * distance;
        double xCoord = player.posX + v;
        double yCoord = player.posY + player.getEyeHeight() + Math.sin(-pitch) * distance;
        double zCoord = player.posZ + v1;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                xCoord - 0.5 + v,
                yCoord + player.getEyeHeight() + Math.sin(-pitch) * distance,
                zCoord - 0.5 + v1,
                xCoord + v + 0.5,
                yCoord + player.getEyeHeight() + Math.sin(-pitch) * distance + 1,
                zCoord + v1 + 0.5).expand(0.5f, 0.5f, 0.5f);
        List<EntityLivingBase> list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        for (EntityLivingBase entityLiving : list) {
            if (entityLiving instanceof EntityPlayer && entityLiving.equals(player)) {
                continue;
            }

            int i = 1;

            if (entityLiving.isEntityUndead()) {
                i = 3;
            }
            entityLiving.attackEntityFrom(DamageSource.causePlayerDamage(player), 5 * i);
        }

        world.createExplosion(player, xCoord, yCoord, zCoord, (float) (1), false);

        for (int i = 0; i < 5; i++) {
            SpellHelper.sendParticleToAllAround(
                    world,
                    xCoord,
                    yCoord,
                    zCoord,
                    30,
                    world.provider.dimensionId,
                    "mobSpell",
                    xCoord + itemRand.nextFloat() - itemRand.nextFloat(),
                    yCoord + itemRand.nextFloat() - itemRand.nextFloat(),
                    zCoord + itemRand.nextFloat() - itemRand.nextFloat(),
                    1.0F,
                    1.0F,
                    1.0F);
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

        if (!world.isRemote) {
            for (int i = 0; i < 360; i += 18) {
                world.spawnEntityInWorld(
                        new HolyProjectile(
                                world,
                                player,
                                8,
                                3,
                                player.posX,
                                player.posY + (player.height / 2),
                                player.posZ,
                                i,
                                0));
            }
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

        int d0 = 3;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                player.posX,
                player.posY,
                player.posZ,
                (player.posX + 1),
                (player.posY + 2),
                (player.posZ + 1)).expand(d0, d0, d0);
        List<EntityLivingBase> list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        for (EntityLivingBase entityLiving : list) {
            if (entityLiving instanceof EntityPlayer && entityLiving.equals(player)) {
                continue;
            }

            int i = 1;

            if (entityLiving.isEntityUndead()) {
                i = 3;
            }
            entityLiving.attackEntityFrom(DamageSource.causePlayerDamage(player), 5 * i);
        }

        world.createExplosion(player, player.posX, player.posY, player.posZ, (float) (2), false);
        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;

        for (int i = 0; i < 20; i++) {
            SpellHelper.sendParticleToAllAround(
                    world,
                    xCoord,
                    yCoord,
                    zCoord,
                    30,
                    world.provider.dimensionId,
                    "mobSpell",
                    xCoord + itemRand.nextFloat() - itemRand.nextFloat(),
                    yCoord + itemRand.nextFloat() - itemRand.nextFloat(),
                    zCoord + itemRand.nextFloat() - itemRand.nextFloat(),
                    1.0F,
                    1.0F,
                    1.0F);
        }

        return item;
    }
}
