package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectFeatheredKnife extends RitualEffect {

    public final int amount = AlchemicalWizardry.lpPerSelfSacrificeFeatheredKnife;

    public static final int sanctusDrain = 5;
    public static final int reductusDrain = 3;
    public static final int magicalesDrain = 2;
    public static final int potentiaDrain = 5;

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

        int timeDelay = hasPotentia ? 10 : 20;

        if (world.getTotalWorldTime() % timeDelay != 0) {
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

        boolean hasReductus = this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, false);

        double range = hasReductus ? 8 : 15;
        double vertRange = hasReductus ? 8 : 20;
        List<EntityPlayer> list = SpellHelper.getPlayersInRange(world, x + 0.5, y + 0.5, z + 0.5, range, vertRange);

        int entityCount = 0;
        boolean flag = false;

        if (currentEssence < this.getCostPerRefresh() * list.size()) {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
        } else {
            boolean hasMagicales = this
                    .canDrainReagent(ritualStone, ReagentRegistry.magicalesReagent, magicalesDrain, false);
            boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);

            EntityPlayer ownerPlayer = SpellHelper.getPlayerForUsername(owner);
            for (EntityPlayer player : list) {
                hasSanctus = hasSanctus
                        && this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
                double threshold = hasSanctus ? 0.7d : 0.3d;

                if ((hasMagicales && player == ownerPlayer) || !hasMagicales) {
                    if (!SpellHelper.isFakePlayer(world, player)) {
                        if (player.getHealth() / player.getMaxHealth() > threshold) {
                            player.setHealth(player.getHealth() - 1);
                            entityCount++;
                            tileAltar.sacrificialDaggerCall(this.amount, false);
                            if (hasSanctus) {
                                this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);
                            }
                            if (hasMagicales) {
                                this.canDrainReagent(
                                        ritualStone,
                                        ReagentRegistry.magicalesReagent,
                                        magicalesDrain,
                                        true);
                                break;
                            }
                        }
                    }
                }
            }

            if (entityCount > 0) {
                if (hasReductus) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.reductusReagent, reductusDrain, true);
                }
                if (hasPotentia) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                }

                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * entityCount);
            }
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostFeatheredKnife[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> featheredKnifeRitual = new ArrayList();
        featheredKnifeRitual.add(new RitualComponent(1, 0, 0, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(-1, 0, 0, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(0, 0, 1, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(0, 0, -1, RitualComponent.DUSK));
        featheredKnifeRitual.add(new RitualComponent(2, -1, 0, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, 0, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(0, -1, 2, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(0, -1, -2, RitualComponent.WATER));
        featheredKnifeRitual.add(new RitualComponent(1, -1, 1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(1, -1, -1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-1, -1, 1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-1, -1, -1, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(4, -1, 2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(2, -1, 4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(2, -1, -4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(4, -1, -2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.FIRE));
        featheredKnifeRitual.add(new RitualComponent(4, 0, 2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(2, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(2, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, -2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, 3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(4, 0, -3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.EARTH));
        featheredKnifeRitual.add(new RitualComponent(3, 0, 3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(3, 0, -3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.AIR));
        featheredKnifeRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.AIR));
        return featheredKnifeRitual;
    }
}
