package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.entity.projectile.FireProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SpellFireBurst extends HomSpell {

    public Random itemRand = new Random();

    public SpellFireBurst() {
        super();
        this.setEnergies(100, 300, 400, 100);
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
            FireProjectile proj = new FireProjectile(world, player, 7);
            world.spawnEntityInWorld(proj);
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

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    world.spawnEntityInWorld(
                            new FireProjectile(
                                    world,
                                    player,
                                    8,
                                    2,
                                    player.posX,
                                    player.posY + player.getEyeHeight(),
                                    player.posZ,
                                    player.rotationYaw + i * 10F,
                                    player.rotationPitch + j * 10F));
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

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        int d0 = 2;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                player.posX,
                player.posY,
                player.posZ,
                player.posX + 1,
                player.posY + 2,
                player.posZ + 1).expand(d0, d0, d0);
        List<EntityLivingBase> list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        for (EntityLivingBase entityLiving : list) {
            if (entityLiving instanceof EntityPlayer && entityLiving.equals(player)) {
                continue;
            }

            entityLiving.setFire(100);
            entityLiving.attackEntityFrom(DamageSource.inFire, 2);
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

        world.playSoundAtEntity(player, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (world.isAirBlock((int) player.posX + i, (int) player.posY + j, (int) player.posZ + k)) {
                        if (world.rand.nextFloat() < 0.8F) {
                            world.setBlock(
                                    (int) player.posX + i,
                                    (int) player.posY + j,
                                    (int) player.posZ + k,
                                    Blocks.fire);
                        }
                    }
                }
            }
        }

        return item;
    }
}
