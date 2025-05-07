package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.bindingRegistry.UnbindingRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectUnbinding extends RitualEffect {

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
        } else {
            int d0 = 0;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(
                    (double) x,
                    (double) y + 1,
                    (double) z,
                    (double) (x + 1),
                    (double) (y + 2),
                    (double) (z + 1)).expand(d0, d0, d0);
            List list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
            Iterator iterator = list.iterator();
            EntityItem item;

            boolean drain = false;

            while (iterator.hasNext()) {
                final int sanctusDrain = 1000;
                item = (EntityItem) iterator.next();
                ItemStack itemStack = item.getEntityItem();

                if (itemStack == null) {
                    continue;
                }

                boolean hasSanctus = this
                        .canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
                if (hasSanctus) {
                    if (itemStack.getItem() instanceof IBindable && !IBindable.getOwnerName(itemStack).equals("")) {
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z - 5));
                        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z + 5));
                        world.addWeatherEffect(new EntityLightningBolt(world, x - 5, y + 1, z));
                        world.addWeatherEffect(new EntityLightningBolt(world, x + 5, y + 1, z));

                        IBindable.setItemOwner(itemStack, "");
                        this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, true);
                        drain = true;
                        ritualStone.setActive(false);
                        break;
                    }
                }

                if (itemStack.getItem() == ModItems.boundHelmet) {
                    ritualStone.setVar1(5);
                } else if (itemStack.getItem() == ModItems.boundPlate) {
                    ritualStone.setVar1(8);
                } else if (itemStack.getItem() == ModItems.boundLeggings) {
                    ritualStone.setVar1(7);
                } else if (itemStack.getItem() == ModItems.boundBoots) {
                    ritualStone.setVar1(4);
                } else if (UnbindingRegistry.isRequiredItemValid(itemStack)) {
                    ritualStone.setVar1(UnbindingRegistry.getIndexForItem(itemStack) + 9);
                }

                if (ritualStone.getVar1() > 0 && ritualStone.getVar1() <= 8) {
                    item.setDead();
                    doLightning(world, x, y, z);
                    ItemStack[] inv = ((BoundArmour) itemStack.getItem()).getInternalInventory(itemStack);
                    int bloodSockets = 0;
                    if (itemStack.getItem() == ModItems.boundHelmet) {
                        bloodSockets = 5;
                    } else if (itemStack.getItem() == ModItems.boundPlate) {
                        bloodSockets = 8;
                    } else if (itemStack.getItem() == ModItems.boundLeggings) {
                        bloodSockets = 7;
                    } else if (itemStack.getItem() == ModItems.boundBoots) {
                        bloodSockets = 4;
                    }
                    if (inv != null) {
                        for (ItemStack internalItem : inv) {
                            if (internalItem != null) {
                                doLightning(world, x, y, z);
                                EntityItem newItem = new EntityItem(
                                        world,
                                        x + 0.5,
                                        y + 1,
                                        z + 0.5,
                                        internalItem.copy());
                                world.spawnEntityInWorld(newItem);
                            }
                        }
                    }

                    EntityItem newItem = new EntityItem(
                            world,
                            x + 0.5,
                            y + 1,
                            z + 0.5,
                            new ItemStack(ModBlocks.bloodSocket, bloodSockets));
                    world.spawnEntityInWorld(newItem);
                    ritualStone.setActive(false);
                    drain = true;
                    break;
                } else if (ritualStone.getVar1() >= 9) {
                    item.setDead();
                    doLightning(world, x, y, z);
                    List<ItemStack> spawnedItem = UnbindingRegistry.getOutputForIndex(ritualStone.getVar1() - 9);

                    if (spawnedItem != null) {
                        for (ItemStack itemStack1 : spawnedItem) {
                            EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, itemStack1.copy());
                            world.spawnEntityInWorld(newItem);
                        }
                    }

                    ritualStone.setActive(false);
                    drain = true;
                    break;
                }
            }

            if (drain) {
                SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
            }
        }

        if (world.rand.nextInt(10) == 0) {
            SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 1, x, y, z);
        }
    }

    private void doLightning(World world, int x, int y, int z) {
        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z - 5));
        world.addWeatherEffect(new EntityLightningBolt(world, x, y + 1, z + 5));
        world.addWeatherEffect(new EntityLightningBolt(world, x - 5, y + 1, z));
        world.addWeatherEffect(new EntityLightningBolt(world, x + 5, y + 1, z));
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostUnbinding[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> unbindingRitual = new ArrayList<RitualComponent>();
        unbindingRitual.add(new RitualComponent(-2, 0, 0, 4));
        unbindingRitual.add(new RitualComponent(2, 0, 0, 4));
        unbindingRitual.add(new RitualComponent(0, 0, 2, 4));
        unbindingRitual.add(new RitualComponent(0, 0, -2, 4));
        unbindingRitual.add(new RitualComponent(-2, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, -3, 3));
        unbindingRitual.add(new RitualComponent(-3, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, -3, 3));
        unbindingRitual.add(new RitualComponent(3, 0, -2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(-2, 0, 3, 3));
        unbindingRitual.add(new RitualComponent(-3, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(2, 0, 3, 3));
        unbindingRitual.add(new RitualComponent(3, 0, 2, 3));
        unbindingRitual.add(new RitualComponent(3, 1, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 1, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 1, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 1, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 2, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 2, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 2, -3, 0));
        unbindingRitual.add(new RitualComponent(-3, 2, 3, 0));
        unbindingRitual.add(new RitualComponent(3, 3, 3, 2));
        unbindingRitual.add(new RitualComponent(3, 3, -3, 2));
        unbindingRitual.add(new RitualComponent(-3, 3, -3, 2));
        unbindingRitual.add(new RitualComponent(-3, 3, 3, 2));
        unbindingRitual.add(new RitualComponent(-5, 0, 0, 2));
        unbindingRitual.add(new RitualComponent(5, 0, 0, 2));
        unbindingRitual.add(new RitualComponent(0, 0, 5, 2));
        unbindingRitual.add(new RitualComponent(0, 0, -5, 2));
        return unbindingRitual;
    }
}
