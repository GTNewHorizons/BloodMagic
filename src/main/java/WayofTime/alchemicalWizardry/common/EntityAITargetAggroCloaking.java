package WayofTime.alchemicalWizardry.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;

public class EntityAITargetAggroCloaking extends EntityAITargetAggro {

    int cloakLevel; // Level of cloaking that the owner demon is fooled by

    public EntityAITargetAggroCloaking(EntityDemon entityDemon, Class<? extends Entity> entityClass, int par3,
            boolean par4, int cloak) {
        super(entityDemon, entityClass, par3, par4);
        this.cloakLevel = cloak;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.taskOwner.getAITarget();
        boolean cloakActive = false;

        if (target != null && target.isPotionActive(AlchemicalWizardry.customPotionBoost.id)) {
            cloakActive = target.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier()
                    >= cloakLevel;
        }

        return !cloakActive && super.shouldExecute();
    }
}
