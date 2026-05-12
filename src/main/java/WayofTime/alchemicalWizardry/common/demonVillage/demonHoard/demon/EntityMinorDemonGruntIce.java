package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.entity.projectile.IceProjectile;

public class EntityMinorDemonGruntIce extends EntityMinorDemonGrunt {

    public EntityMinorDemonGruntIce(World world) {
        super(world);
        this.setDemonID(AlchemicalWizardry.entityMinorDemonGruntIceID);
    }

    @Override
    public void causeEffect(Entity entity) {
        if (entity instanceof EntityLivingBase e) {
            e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 4));
        }
    }

    @Override
    protected EnergyBlastProjectile attackProjectile(EntityLivingBase target) {
        return new IceProjectile(worldObj, this, target, 1.8f, 0f, 15, 600);
    }
}
