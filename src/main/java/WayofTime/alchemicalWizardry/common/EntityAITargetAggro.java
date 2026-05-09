package WayofTime.alchemicalWizardry.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;

public class EntityAITargetAggro extends EntityAINearestAttackableTarget {

    private final EntityDemon theCreature;

    public EntityAITargetAggro(EntityDemon entityDemon, Class<? extends Entity> entityClass, int par3, boolean par4) {
        super(entityDemon, entityClass, par3, par4);
        this.theCreature = entityDemon;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return theCreature.isAggro() && super.shouldExecute();
    }
}
