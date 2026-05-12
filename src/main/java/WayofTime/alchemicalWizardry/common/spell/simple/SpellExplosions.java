package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SpellExplosions extends HomSpell {

    Random itemRand = new Random();

    public SpellExplosions() {
        super();
        this.setEnergies(400, 500, 1900, 1500);
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
            world.spawnEntityInWorld(new ExplosionProjectile(world, player, 6, true));
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

        int distance = 4;
        double yaw = player.rotationYaw / 180 * Math.PI;
        double pitch = player.rotationPitch / 180 * Math.PI;
        world.createExplosion(
                player,
                player.posX + Math.sin(yaw) * Math.cos(pitch) * (-distance),
                player.posY + player.getEyeHeight() + Math.sin(-pitch) * distance,
                player.posZ + Math.cos(yaw) * Math.cos(pitch) * distance,
                (float) (3),
                true);
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

        int distance = 4;
        world.createExplosion(
                player,
                player.posX,
                player.posY + player.getEyeHeight(),
                player.posZ,
                (float) (distance),
                false);
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

        int radius = 3;

        for (int i = 0; i < 360; i += 36) {
            world.createExplosion(
                    player,
                    player.posX + Math.cos(i) * radius,
                    player.posY,
                    player.posZ + Math.sin(i) * radius,
                    (float) (2),
                    true);
        }

        return null;
    }
}
