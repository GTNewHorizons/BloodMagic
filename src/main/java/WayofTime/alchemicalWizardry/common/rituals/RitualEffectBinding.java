package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.BindingRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectBinding extends RitualEffect {

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (currentEssence < this.getCostPerRefresh()) {
            SoulNetworkHandler.causeNauseaToPlayer(owner);
            return;
        }
        if (ritualStone.getVar1() == 0) {
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1);
            List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);

            for (EntityItem item : list) {
                ItemStack itemStack = item.getEntityItem();

                if (itemStack == null) {
                    continue;
                }

                if (BindingRegistry.isRequiredItemValid(itemStack)) {
                    ritualStone.setVar1(BindingRegistry.getIndexForItem(itemStack) + 1);
                    itemStack.stackSize--;
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z));
                    ritualStone.setCooldown(ritualStone.getCooldown() - 1);
                    if (itemStack.stackSize <= 0) {
                        item.setDead();
                    }
                    break;
                }

                if (world.rand.nextInt(10) == 0) {
                    SpellHelper
                            .sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 1, x, y, z);
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, getCostPerRefresh());
        } else {
            ritualStone.setCooldown(ritualStone.getCooldown() - 1);

            if (world.rand.nextInt(20) == 0) {
                int lightningPoint = world.rand.nextInt(8);

                switch (lightningPoint) {
                    case 0 -> world.addWeatherEffect(new EntityLightningBolt(world, x + 4, y + 3, z));
                    case 1 -> world.addWeatherEffect(new EntityLightningBolt(world, x - 4, y + 3, z));
                    case 2 -> world.addWeatherEffect(new EntityLightningBolt(world, x, y + 3, z + 4));
                    case 3 -> world.addWeatherEffect(new EntityLightningBolt(world, x, y + 3, z - 4));
                    case 4 -> world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z + 3));
                    case 5 -> world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z + 3));
                    case 6 -> world.addWeatherEffect(new EntityLightningBolt(world, x + 3, y + 3, z - 3));
                    case 7 -> world.addWeatherEffect(new EntityLightningBolt(world, x - 3, y + 3, z - 3));
                }
            }

            if (ritualStone.getCooldown() <= 0) {
                ItemStack spawnedItem = BindingRegistry.getOutputForIndex(ritualStone.getVar1() - 1);

                if (spawnedItem != null) {
                    EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, spawnedItem.copy());
                    world.spawnEntityInWorld(newItem);
                }

                ritualStone.setActive(false);
            }
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostBinding[1];
    }

    @Override
    public int getInitialCooldown() {
        return 200;
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> boundSoulRitual = new ArrayList<>();
        boundSoulRitual.add(new RitualComponent(3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(-3, 0, 0, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, 3, 2));
        boundSoulRitual.add(new RitualComponent(0, 0, -3, 2));
        boundSoulRitual.add(new RitualComponent(2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, 2, 4));
        boundSoulRitual.add(new RitualComponent(2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(-2, 0, -2, 4));
        boundSoulRitual.add(new RitualComponent(4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(-4, 2, 0, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, 4, 1));
        boundSoulRitual.add(new RitualComponent(0, 2, -4, 1));
        boundSoulRitual.add(new RitualComponent(3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, 3, 3));
        boundSoulRitual.add(new RitualComponent(-3, 2, -3, 3));
        boundSoulRitual.add(new RitualComponent(4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(-4, 1, 0, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, 4, 0));
        boundSoulRitual.add(new RitualComponent(0, 1, -4, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(3, 1, -3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, 3, 0));
        boundSoulRitual.add(new RitualComponent(-3, 1, -3, 0));
        return boundSoulRitual;
    }
}
