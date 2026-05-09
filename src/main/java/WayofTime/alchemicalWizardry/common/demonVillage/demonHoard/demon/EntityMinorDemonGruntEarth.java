package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;

public class EntityMinorDemonGruntEarth extends EntityMinorDemonGrunt {

    public EntityMinorDemonGruntEarth(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntEarthID);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (super.attackEntityAsMob(entity)) {
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 2));
            return true;
        }
        return false;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase entity, float par2) {
        if (friendlyDemon(entity)) {
            return;
        }
        this.worldObj.spawnEntityInWorld(new ExplosionProjectile(worldObj, this, entity, 1.8f, 0f, 15, 600, false));
    }
}
