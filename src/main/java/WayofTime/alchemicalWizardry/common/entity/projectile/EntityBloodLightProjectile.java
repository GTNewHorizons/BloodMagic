package WayofTime.alchemicalWizardry.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.ModBlocks;

public class EntityBloodLightProjectile extends EnergyBlastProjectile {

    public EntityBloodLightProjectile(World world) {
        super(world);
    }

    public EntityBloodLightProjectile(World world, EntityLivingBase player, int damage) {
        super(world, player, damage);
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            if (mop.entityHit == shootingEntity) {
                return;
            }

            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int sideHit = mop.sideHit;
            int blockX = mop.blockX;
            int blockY = mop.blockY;
            int blockZ = mop.blockZ;

            if (sideHit == 0 && this.worldObj.isAirBlock(blockX, blockY - 1, blockZ)) {
                this.worldObj.setBlock(blockX, blockY - 1, blockZ, ModBlocks.blockBloodLight);
            }

            if (sideHit == 1 && this.worldObj.isAirBlock(blockX, blockY + 1, blockZ)) {
                this.worldObj.setBlock(blockX, blockY + 1, blockZ, ModBlocks.blockBloodLight);
            }

            if (sideHit == 2 && this.worldObj.isAirBlock(blockX, blockY, blockZ - 1)) {
                this.worldObj.setBlock(blockX, blockY, blockZ - 1, ModBlocks.blockBloodLight);
            }

            if (sideHit == 3 && this.worldObj.isAirBlock(blockX, blockY, blockZ + 1)) {
                this.worldObj.setBlock(blockX, blockY, blockZ + 1, ModBlocks.blockBloodLight);
            }

            if (sideHit == 4 && this.worldObj.isAirBlock(blockX - 1, blockY, blockZ)) {
                this.worldObj.setBlock(blockX - 1, blockY, blockZ, ModBlocks.blockBloodLight);
            }

            if (sideHit == 5 && this.worldObj.isAirBlock(blockX + 1, blockY, blockZ)) {
                this.worldObj.setBlock(blockX + 1, blockY, blockZ, ModBlocks.blockBloodLight);
            }
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity target) {
        if (target == shootingEntity && ticksInAir > 3) {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            this.setDead();
        } else {
            if (target instanceof EntityLivingBase) {
                ((EntityLivingBase) target).setRevengeTarget(shootingEntity);
                doDamage(1, target);
            }
        }

        if (worldObj.isAirBlock((int) this.posX, (int) this.posY, (int) this.posZ)) {
            worldObj.setBlock((int) this.posX, (int) this.posY, (int) this.posZ, Blocks.fire);
        }

        spawnHitParticles("magicCrit", 8);
        this.setDead();
    }
}
