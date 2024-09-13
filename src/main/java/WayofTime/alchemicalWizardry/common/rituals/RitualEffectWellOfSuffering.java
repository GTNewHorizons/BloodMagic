package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;

public class RitualEffectWellOfSuffering extends RitualEffect {

    public static final int timeDelay = 25;
    public static final int amount = AlchemicalWizardry.lpPerSacrificeWellOfSuffering;
    public static final int tennebraeDrain = 5;
    public static final int potentiaDrain = 10;
    public static final int offensaDrain = 3;

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (world.getWorldTime() % timeDelay != 0) {
            return;
        }

        IBloodAltar tileAltar = null;
        boolean testFlag = false;

        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                for (int k = -10; k <= 10; k++) {
                    if (world.getTileEntity(x + i, y + k, z + j) instanceof IBloodAltar) {
                        tileAltar = (IBloodAltar) world.getTileEntity(x + i, y + k, z + j);
                        testFlag = true;
                    }
                }
            }
        }

        if (!testFlag) {
            return;
        }

        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

        int d0 = 10;
        int vertRange = hasPotentia ? 20 : 10;
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1))
                .expand(d0, vertRange, d0);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

        int entityCount = 0;
        boolean hasTennebrae = this
                .canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tennebraeDrain, false);
        boolean hasOffensa = this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, false);

        if (currentEssence < this.getCostPerRefresh() * list.size()) {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else {
            for (EntityLivingBase livingEntity : list) {
                if (!livingEntity.isEntityAlive() || livingEntity instanceof EntityPlayer
                        || AlchemicalWizardry.wellBlacklist.contains(livingEntity.getClass())) {
                    continue;
                }

                hasOffensa = hasOffensa
                        && this.canDrainReagent(ritualStone, ReagentRegistry.offensaReagent, offensaDrain, true);

                if (livingEntity.attackEntityFrom(DamageSource.outOfWorld, hasOffensa ? 2 : 1)) {
                    hasTennebrae = hasTennebrae
                            && this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tennebraeDrain, true);

                    entityCount++;
                    tileAltar.sacrificialDaggerCall(amount * (hasTennebrae ? 2 : 1) * (hasOffensa ? 2 : 1), true);
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * entityCount);

            if (hasPotentia) {
                this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
            }
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostSuffering[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> wellOfSufferingRitual = new ArrayList<>();
        wellOfSufferingRitual.add(new RitualComponent(1, 0, 1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, 1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, -1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, -1, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, -2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, -2, RitualComponent.FIRE));
        wellOfSufferingRitual.add(new RitualComponent(0, -1, 2, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 0, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(0, -1, -2, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 0, RitualComponent.EARTH));
        wellOfSufferingRitual.add(new RitualComponent(-3, -1, -3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(3, -1, -3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(-3, -1, 3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(3, -1, 3, RitualComponent.DUSK));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, -1, 2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, -1, -2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(2, -1, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 0, 1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(1, 0, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, 0, 1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, 4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 0, -1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-1, 0, -4, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(-4, 0, -1, RitualComponent.WATER));
        wellOfSufferingRitual.add(new RitualComponent(4, 1, 0, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(0, 1, 4, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.AIR));
        wellOfSufferingRitual.add(new RitualComponent(0, 1, -4, RitualComponent.AIR));
        return wellOfSufferingRitual;
    }
}
