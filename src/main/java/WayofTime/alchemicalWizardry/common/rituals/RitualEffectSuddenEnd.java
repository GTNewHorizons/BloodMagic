package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RitualEffectSuddenEnd extends RitualEffect {
	public static final int timeDelay = 20;// slightly faster to get rid of those mobs right away befroe they cause...problems
	public static final int amount = AlchemicalWizardry.lpPerSacrificeSuddenEnd;

	@Override
	public void performEffect(IMasterRitualStone ritualStone) {
		String owner = ritualStone.getOwner();
		int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);

		World world = ritualStone.getWorld();
		int x = ritualStone.getXCoord();
		int y = ritualStone.getYCoord();
		int z = ritualStone.getZCoord();

		if (world.getWorldTime() % this.timeDelay != 0) {
			return;
		}

		IBloodAltar tileAltar = null;
		boolean testFlag = false;

		for (int i = -5; i <= 5; i++) {
			for (int j = -5; j <= 5; j++) {
				for (int k = -13; k <= 13; k++)// height checking is +6 because it's height is higher than normal
				{
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

		int d0 = 32;// should increase range from 10, 10, 10 to 32, 16, 32 including vertrange change
		int vertRange = 16;
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1)).expand(d0, vertRange, d0);
		List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

		int entityCount = 0;

		if (currentEssence < this.getCostPerRefresh() * list.size()) {
			SoulNetworkHandler.causeNauseaToPlayer(owner);
		} else {
			for (EntityLivingBase livingEntity : list) {
				if (!livingEntity.isEntityAlive() || livingEntity instanceof EntityPlayer || AlchemicalWizardry.wellBlacklist.contains(livingEntity.getClass())) {
					continue;
				}

				if (livingEntity.attackEntityFrom(DamageSource.outOfWorld, livingEntity.getMaxHealth() * 2)) {
					entityCount++;
					tileAltar.sacrificialDaggerCall(this.amount, true);
				}
			}
			SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh() * entityCount);
		}
	}

	@Override
	public int getCostPerRefresh() {
		return AlchemicalWizardry.ritualCostSuddenEnd[1];
	}

	@Override
	public List<RitualComponent> getRitualComponentList() {
		ArrayList<RitualComponent> suddenEndRitual = new ArrayList();
		// dawn
		suddenEndRitual.add(new RitualComponent(3, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(4, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(6, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(7, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(8, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(9, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(10, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(11, 0, 0, RitualComponent.DAWN));

		suddenEndRitual.add(new RitualComponent(-3, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-4, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-6, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-7, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-8, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-9, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-10, 0, 0, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(-11, 0, 0, RitualComponent.DAWN));

		suddenEndRitual.add(new RitualComponent(0, 0, 3, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 4, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 6, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 7, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 8, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 9, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 10, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, 11, RitualComponent.DAWN));

		suddenEndRitual.add(new RitualComponent(0, 0, -3, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -4, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -6, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -7, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -8, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -9, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -10, RitualComponent.DAWN));
		suddenEndRitual.add(new RitualComponent(0, 0, -11, RitualComponent.DAWN));

		// fire
		suddenEndRitual.add(new RitualComponent(4, 0, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(4, 0, -1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(4, 1, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(4, -1, 0, RitualComponent.FIRE));

		suddenEndRitual.add(new RitualComponent(7, 0, 2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, 0, -2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, 2, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, -2, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, 1, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, 1, -1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, -1, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(7, -1, -1, RitualComponent.FIRE));

		suddenEndRitual.add(new RitualComponent(11, 1, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 2, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 3, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 4, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 2, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 2, -1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 4, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 4, -1, RitualComponent.FIRE));

		suddenEndRitual.add(new RitualComponent(11, -1, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -2, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -3, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -4, 0, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -2, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -2, -1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -4, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -4, -1, RitualComponent.FIRE));

		suddenEndRitual.add(new RitualComponent(11, 0, 1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 0, 2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 0, 3, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 0, 4, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 1, 2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -1, 2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 1, 4, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -1, 4, RitualComponent.FIRE));

		suddenEndRitual.add(new RitualComponent(11, 0, -1, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 0, -2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 0, -3, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 0, -4, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 1, -2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -1, -2, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, 1, -4, RitualComponent.FIRE));
		suddenEndRitual.add(new RitualComponent(11, -1, -4, RitualComponent.FIRE));

		// water
		suddenEndRitual.add(new RitualComponent(-4, 0, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-4, 0, -1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-4, 1, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-4, -1, 0, RitualComponent.WATER));

		suddenEndRitual.add(new RitualComponent(-7, 0, 2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, 0, -2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, 2, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, -2, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, 1, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, 1, -1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, -1, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-7, -1, -1, RitualComponent.WATER));

		suddenEndRitual.add(new RitualComponent(-11, 1, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 2, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 3, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 4, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 2, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 2, -1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 4, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 4, -1, RitualComponent.WATER));

		suddenEndRitual.add(new RitualComponent(-11, -1, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -2, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -3, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -4, 0, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -2, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -2, -1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -4, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -4, -1, RitualComponent.WATER));

		suddenEndRitual.add(new RitualComponent(-11, 0, 1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 0, 2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 0, 3, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 0, 4, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 1, 2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -1, 2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 1, 4, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -1, 4, RitualComponent.WATER));

		suddenEndRitual.add(new RitualComponent(-11, 0, -1, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 0, -2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 0, -3, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 0, -4, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 1, -2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -1, -2, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, 1, -4, RitualComponent.WATER));
		suddenEndRitual.add(new RitualComponent(-11, -1, -4, RitualComponent.WATER));

		// earth
		suddenEndRitual.add(new RitualComponent(1, 0, 4, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, 0, 4, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, 1, 4, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, -1, 4, RitualComponent.EARTH));

		suddenEndRitual.add(new RitualComponent(2, 0, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-2, 0, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, 2, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, -2, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(1, 1, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, 1, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(1, -1, 7, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, -1, 7, RitualComponent.EARTH));

		suddenEndRitual.add(new RitualComponent(0, 1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, 2, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, 3, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, 4, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(1, 2, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, 2, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(1, 4, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, 4, 11, RitualComponent.EARTH));

		suddenEndRitual.add(new RitualComponent(0, -1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, -2, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, -3, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(0, -4, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(1, -2, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, -2, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(1, -4, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-1, -4, 11, RitualComponent.EARTH));

		suddenEndRitual.add(new RitualComponent(1, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(2, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(3, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(4, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(2, 1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(2, -1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(4, 1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(4, -1, 11, RitualComponent.EARTH));

		suddenEndRitual.add(new RitualComponent(-1, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-2, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-3, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-4, 0, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-2, 1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-2, -1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-4, 1, 11, RitualComponent.EARTH));
		suddenEndRitual.add(new RitualComponent(-4, -1, 11, RitualComponent.EARTH));

		// air
		suddenEndRitual.add(new RitualComponent(1, 0, -4, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, 0, -4, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, 1, -4, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, -1, -4, RitualComponent.AIR));

		suddenEndRitual.add(new RitualComponent(2, 0, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-2, 0, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, 2, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, -2, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(1, 1, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, 1, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(1, -1, -7, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, -1, -7, RitualComponent.AIR));

		suddenEndRitual.add(new RitualComponent(0, 1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, 2, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, 3, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, 4, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(1, 2, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, 2, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(1, 4, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, 4, -11, RitualComponent.AIR));

		suddenEndRitual.add(new RitualComponent(0, -1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, -2, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, -3, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(0, -4, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(1, -2, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, -2, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(1, -4, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-1, -4, -11, RitualComponent.AIR));

		suddenEndRitual.add(new RitualComponent(1, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(2, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(3, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(4, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(2, 1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(2, -1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(4, 1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(4, -1, -11, RitualComponent.AIR));

		suddenEndRitual.add(new RitualComponent(-1, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-2, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-3, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-4, 0, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-2, 1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-2, -1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-4, 1, -11, RitualComponent.AIR));
		suddenEndRitual.add(new RitualComponent(-4, -1, -11, RitualComponent.AIR));

		// dusk
		suddenEndRitual.add(new RitualComponent(4, 1, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, 2, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, -1, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, -2, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, 1, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, 2, -1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, -1, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(4, -2, -1, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(7, 3, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, 2, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, 1, 3, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, -3, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, -2, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, -1, 3, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, 3, -1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, 2, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, 1, -3, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, -3, -1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, -2, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(7, -1, -3, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(-4, 1, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, 2, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, -1, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, -2, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, 1, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, 2, -1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, -1, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-4, -2, -1, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(-7, 3, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, 2, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, 1, 3, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, -3, 1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, -2, 2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, -1, 3, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, 3, -1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, 2, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, 1, -3, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, -3, -1, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, -2, -2, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-7, -1, -3, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(2, 1, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(1, 2, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(2, -1, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(1, -2, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, 1, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, 2, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, -1, 4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, -2, 4, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(1, 3, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(2, 2, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(3, 1, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(1, -3, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(2, -2, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(3, -1, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, 3, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, 2, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-3, 1, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, -3, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, -2, 7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-3, -1, 7, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(2, 1, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(1, 2, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(2, -1, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(1, -2, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, 1, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, 2, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, -1, -4, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, -2, -4, RitualComponent.DUSK));

		suddenEndRitual.add(new RitualComponent(1, 3, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(2, 2, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(3, 1, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(1, -3, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(2, -2, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(3, -1, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, 3, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, 2, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-3, 1, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-1, -3, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-2, -2, -7, RitualComponent.DUSK));
		suddenEndRitual.add(new RitualComponent(-3, -1, -7, RitualComponent.DUSK));

		// blood
		suddenEndRitual.add(new RitualComponent(11, 4, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, 3, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, 4, 3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(11, -4, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, -3, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, -4, 3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(11, 4, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, 3, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, 4, -3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(11, -4, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, -3, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(11, -4, -3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-11, 4, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, 3, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, 4, 3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-11, -4, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, -3, 4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, -4, 3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-11, 4, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, 3, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, 4, -3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-11, -4, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, -3, -4, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-11, -4, -3, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(4, 4, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(4, 3, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(3, 4, 11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(4, -4, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(4, -3, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(3, -4, 11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-4, 4, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-4, 3, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-3, 4, 11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-4, -4, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-4, -3, 11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-3, -4, 11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(4, 4, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(4, 3, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(3, 4, -11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(4, -4, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(4, -3, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(3, -4, -11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-4, 4, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-4, 3, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-3, 4, -11, RitualComponent.BLOOD));

		suddenEndRitual.add(new RitualComponent(-4, -4, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-4, -3, -11, RitualComponent.BLOOD));
		suddenEndRitual.add(new RitualComponent(-3, -4, -11, RitualComponent.BLOOD));
		return suddenEndRitual;
	}

}
