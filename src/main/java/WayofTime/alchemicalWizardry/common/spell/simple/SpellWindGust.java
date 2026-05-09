package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.WindGustProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellWindGust extends HomSpell {

    Random itemRand = new Random();

    public SpellWindGust() {
        super();
        this.setEnergies(300, 400, 300, 500);
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
            world.spawnEntityInWorld(new WindGustProjectile(world, player, 8));
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

        int distance = 3;
        double yaw = player.rotationYaw / 180 * Math.PI;
        double pitch = player.rotationPitch / 180 * Math.PI;
        double v = Math.cos(yaw) * Math.cos(pitch) * distance;
        double v1 = Math.sin(yaw) * Math.cos(pitch) * (-distance);
        double xCoord = player.posX + v1;
        double yCoord = player.posY + player.getEyeHeight() + Math.sin(-pitch) * distance;
        double zCoord = player.posZ + v;
        float d0 = 0.5f;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                player.posX - 0.5 + v1,
                player.posY + player.getEyeHeight() + Math.sin(-pitch) * distance,
                player.posZ - 0.5 + v,
                player.posX + v1 + 0.5,
                player.posY + player.getEyeHeight() + Math.sin(-pitch) * distance + 1,
                player.posZ + v + 0.5).expand(d0, d0, d0);
        List<EntityLivingBase> list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        for (EntityLivingBase entityLiving : list) {

            if (entityLiving instanceof EntityPlayer) {
                if (entityLiving.equals(player)) {
                    continue;
                }
            }
            entityLiving.motionX = Math.sin(-yaw) * 2;
            entityLiving.motionY = 2;
            entityLiving.motionZ = Math.cos(yaw) * 2;
        }
        for (int i = 0; i < 5; i++) {
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
    public ItemStack onDefensiveRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getDefensiveEnergy());
        }

        double yaw = player.rotationYaw / 180 * Math.PI;
        double pitch = player.rotationPitch / 180 * Math.PI;
        double wantedVelocity = 5;
        double xVel = Math.sin(yaw) * Math.cos(pitch) * (-wantedVelocity);
        double yVel = Math.sin(-pitch) * wantedVelocity;
        double zVel = Math.cos(yaw) * Math.cos(pitch) * wantedVelocity;
        Vec3 vec = player.getLookVec();
        player.motionX = vec.xCoord * wantedVelocity;
        player.motionY = vec.yCoord * wantedVelocity;
        player.motionZ = vec.zCoord * wantedVelocity;
        SpellHelper.setPlayerSpeedFromServer(player, xVel, yVel, zVel);
        world.playSoundEffect(
                (float) player.posX + 0.5F,
                (float) player.posY + 0.5F,
                (float) player.posZ + 0.5F,
                "random.fizz",
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        player.fallDistance = 0;
        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;

        for (int i = 0; i < 8; i++) {
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

        int d0 = 3;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                player.posX,
                player.posY,
                player.posZ,
                (player.posX + 1),
                (player.posY + 2),
                (player.posZ + 1)).expand(d0, d0, d0);
        List list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        Iterator iterator = list.iterator();
        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;
        double wantedVel = 2;

        while (iterator.hasNext()) {
            EntityLivingBase entityLiving = (EntityLivingBase) iterator.next();

            if (entityLiving instanceof EntityPlayer) {
                if (entityLiving.equals(player)) {
                    continue;
                }
            }

            double posXDif = entityLiving.posX - player.posX;
            double posYDif = entityLiving.posY - player.posY + 1;
            double posZDif = entityLiving.posZ - player.posZ;
            double distance2 = Math.pow(posXDif, 2) + Math.pow(posYDif, 2) + Math.pow(posZDif, 2);
            double distance = Math.sqrt(distance2);
            entityLiving.motionX = posXDif * wantedVel / distance;
            entityLiving.motionY = posYDif * wantedVel / distance;
            entityLiving.motionZ = posZDif * wantedVel / distance;
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
}
