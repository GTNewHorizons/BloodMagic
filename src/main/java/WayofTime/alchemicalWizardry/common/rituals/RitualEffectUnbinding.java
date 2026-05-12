package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
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
            return;
        }
        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1);
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
        boolean drain = false;

        for (EntityItem item : list) {
            final int sanctusDrain = 1000;
            ItemStack itemStack = item.getEntityItem();

            if (itemStack == null) {
                continue;
            }

            boolean hasSanctus = this.canDrainReagent(ritualStone, ReagentRegistry.sanctusReagent, sanctusDrain, false);
            if (hasSanctus) {
                if (itemStack.getItem() instanceof IBindable && !IBindable.getOwnerName(itemStack).isEmpty()) {
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

            int sockets = getBloodSockets(itemStack);
            if (sockets > 0) {
                ritualStone.setVar1(sockets);
            } else if (UnbindingRegistry.isRequiredItemValid(itemStack)) {
                ritualStone.setVar1(UnbindingRegistry.getIndexForItem(itemStack) + 9);
            }

            if (ritualStone.getVar1() > 0 && ritualStone.getVar1() <= 8
                    && itemStack.getItem() instanceof BoundArmour armor) {
                item.setDead();
                doLightning(world, x, y, z);
                ItemStack[] inv = armor.getInternalInventory(itemStack);
                int bloodSockets = getBloodSockets(itemStack);
                if (inv != null) {
                    for (ItemStack internalItem : inv) {
                        if (internalItem != null) {
                            doLightning(world, x, y, z);
                            EntityItem newItem = new EntityItem(world, x + 0.5, y + 1, z + 0.5, internalItem.copy());
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

        if (world.rand.nextInt(10) == 0) {
            SpellHelper.sendIndexedParticleToAllAround(world, x, y, z, 20, world.provider.dimensionId, 1, x, y, z);
        }
    }

    private static int getBloodSockets(ItemStack itemStack) {
        if (itemStack.getItem() == ModItems.boundHelmet) {
            return 5;
        } else if (itemStack.getItem() == ModItems.boundPlate) {
            return 8;
        } else if (itemStack.getItem() == ModItems.boundLeggings) {
            return 7;
        } else if (itemStack.getItem() == ModItems.boundBoots) {
            return 4;
        }
        return 0;
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
