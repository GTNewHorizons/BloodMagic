package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectLeap extends RitualEffect {

    // Throws mobs with twice the horizontal speed
    public static final int aetherDrain = 10;
    // Throws mobs with half the vertical speed
    public static final int terraeDrain = 10;
    // Grants 3 seconds of fall immunity
    public static final int reductusDrain = 10;
    // Only throws adults (and non-sneaking players)
    public static final int tenebraeDrain = 10;
    // Only throws babies (and non-sneaking players)
    public static final int sanctusDrain = 10;

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        double range = 2.0;

        List<EntityLivingBase> livingList = SpellHelper
                .getLivingEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);

        if (livingList == null || livingList.isEmpty()) {
            return;
        }

        if (currentEssence < this.getCostPerRefresh() * livingList.size()) {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
            return;
        }

        boolean hasAether = true;
        boolean hasTerrae = true;
        boolean hasReductus = true;
        boolean hasTenebrae = true;
        boolean hasSanctus = true;

        int direction = ritualStone.getDirection();

        int count = 0;

        for (EntityLivingBase livingEntity : livingList) {
            if (livingEntity.isSneaking()) {
                continue;
            }

            hasReductus = hasReductus
                    && this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);
            hasTenebrae = hasTenebrae
                    && this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tenebraeDrain, false);
            hasSanctus = hasSanctus
                    && this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);

            // Sanctus only makes it work on children and tenebrae only with adults.
            // Both being present prevents the ritual from working for any NPCs.
            if (((hasSanctus && !livingEntity.isChild()) || (hasTenebrae && livingEntity.isChild()))
                    && !(livingEntity instanceof EntityPlayer)) {
                continue;
            }

            hasAether = hasAether
                    && this.canDrainReagent(ritualStone, ReagentRegistry.aetherReagent, aetherDrain, true);
            hasTerrae = hasTerrae
                    && this.canDrainReagent(ritualStone, ReagentRegistry.terraeReagent, terraeDrain, true);

            double motionY = hasTerrae ? 0.6 : 1.2;
            double speed = hasAether ? 6.0 : 3.0;
            livingEntity.motionY = motionY;
            livingEntity.fallDistance = 0;
            count++;

            if (livingEntity instanceof EntityPlayer) {
                switch (direction) {
                    case 1:
                        SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, 0, motionY, -speed);
                        break;
                    case 2:
                        SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, speed, motionY, 0);
                        break;
                    case 3:
                        SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, 0, motionY, speed);
                        break;
                    case 4:
                        SpellHelper.setPlayerSpeedFromServer((EntityPlayer) livingEntity, -speed, motionY, 0);
                        break;
                }
            } else {
                switch (direction) {
                    case 1:
                        livingEntity.motionX = 0.0;
                        livingEntity.motionZ = -speed;
                        break;
                    case 2:
                        livingEntity.motionX = speed;
                        livingEntity.motionZ = 0.0;
                        break;
                    case 3:
                        livingEntity.motionX = 0.0;
                        livingEntity.motionZ = speed;
                        break;
                    case 4:
                        livingEntity.motionX = -speed;
                        livingEntity.motionZ = 0.0;
                        break;
                }

                if (hasTenebrae) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tenebraeDrain, true);
                } else if (hasSanctus) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);
                }
            }

            if (hasReductus && !livingEntity.isPotionActive(AlchemicalWizardry.customPotionFeatherFall)) {
                livingEntity
                        .addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionFeatherFall.id, 3 * 20, 0));
                this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
            }
        }

        if (count > 0) {
            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * count);
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostSpeed[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> leapingRitual = new ArrayList<>();
        leapingRitual.add(new RitualComponent(0, 0, -2, RitualComponent.DUSK));
        leapingRitual.add(new RitualComponent(1, 0, -1, RitualComponent.AIR));
        leapingRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.AIR));

        for (int i = 0; i <= 2; i++) {
            leapingRitual.add(new RitualComponent(2, 0, i, RitualComponent.AIR));
            leapingRitual.add(new RitualComponent(-2, 0, i, RitualComponent.AIR));
        }
        return leapingRitual;
    }
}
