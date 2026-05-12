package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class EntityAirElemental extends EntityElemental implements IMob {

    public EntityAirElemental(World world) {
        super(world, AlchemicalWizardry.entityAirElementalID);
    }

    @Override
    public void inflictEffectOnEntity(Entity target) {
        if (target instanceof EntityPlayer p) {
            SpellHelper.setPlayerSpeedFromServer(p, target.motionX, target.motionY + 3, target.motionZ);
            p.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        } else if (target instanceof EntityLivingBase t) {
            target.motionY += 3.0D;
            t.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}
