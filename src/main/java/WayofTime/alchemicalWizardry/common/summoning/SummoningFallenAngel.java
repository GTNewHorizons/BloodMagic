package WayofTime.alchemicalWizardry.common.summoning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningHelper;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;

public class SummoningFallenAngel extends SummoningHelper {

    public SummoningFallenAngel(String id) {
        super(id);
    }

    @Override
    public EntityLivingBase getEntity(World worldObj) {
        return new EntityFallenAngel(worldObj);
    }
}
