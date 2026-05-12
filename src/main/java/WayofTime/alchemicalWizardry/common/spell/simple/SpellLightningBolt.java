package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.LightningBoltProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SpellLightningBolt extends HomSpell {

    Random itemRand = new Random();

    public SpellLightningBolt() {
        super();
        this.setEnergies(75, 200, 700, 700);
    }

    @Override
    public ItemStack onOffensiveRangedRightClick(ItemStack item, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveRangedEnergy());
        }

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new LightningBoltProjectile(world, player, 8, false));
        }

        return item;
    }

    @Override
    public ItemStack onOffensiveMeleeRightClick(ItemStack item, World world, EntityPlayer player) {
        // TODO Make it work better...?
        if (IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getOffensiveMeleeEnergy());
        }

        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;
        world.getWorldInfo().setRaining(true);
        if (world.isRemote) {
            world.setRainStrength(1.0f);
            world.setThunderStrength(1.0f);
        }

        world.getWorldInfo().setThunderTime(0);
        world.getWorldInfo().setThundering(true);

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

        double xCoord = player.posX;
        double yCoord = player.posY;
        double zCoord = player.posZ;

        for (int i = 0; i < 5; i++) {
            world.addWeatherEffect(
                    new EntityLightningBolt(
                            world,
                            xCoord + itemRand.nextInt(64) - 32,
                            yCoord + itemRand.nextInt(8) - 8,
                            zCoord + itemRand.nextInt(64) - 32));
        }

        for (int i = 0; i < 8; i++) {
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
    public ItemStack onEnvironmentalRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        if (!player.capabilities.isCreativeMode) {
            EnergyItems.syphonAndDamageWhileInContainer(item, player, this.getEnvironmentalEnergy());
        }

        if (!world.isRemote) {
            world.spawnEntityInWorld(new LightningBoltProjectile(world, player, 8, true));
        }

        return item;
    }
}
