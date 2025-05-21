package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;

public class RitualEffectSummonMeteor extends RitualEffect {

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        String owner = ritualStone.getOwner();

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (ritualStone.getCooldown() > 0) {
            ritualStone.setCooldown(0);
        }

        List<EntityItem> entities = world.getEntitiesWithinAABB(
                EntityItem.class,
                AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));

        if (entities == null) return;

        for (EntityItem entityItem : entities) {
            if (entityItem != null && MeteorRegistry.isValidMeteorFocusItem(entityItem.getEntityItem())) {
                ItemStack stack = entityItem.getEntityItem();
                if (stack == null || stack.stackSize <= 0) continue;

                int meteorID = MeteorRegistry.getMeteorIDForItem(stack);
                int cost = MeteorRegistry.meteorList.get(meteorID).cost;

                if (currentEssence < cost) {
                    EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);
                    if (entityOwner != null) entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                    return;
                }

                EntityMeteor meteor = new EntityMeteor(world, x + 0.5f, 257, z + 0.5f, meteorID);
                meteor.motionY = -1.0f;

                for (Reagent r : ReagentRegistry.reagentList.values()) {
                    if (this.canDrainReagent(ritualStone, r, 1000, true)) {
                        meteor.reagentList.add(r);
                    }
                }

                stack.stackSize--;

                if (stack.stackSize <= 0) {
                    world.removeEntity(entityItem);
                } else {
                    entityItem.setEntityItemStack(stack);
                }

                world.spawnEntityInWorld(meteor);
                ritualStone.setActive(false);
                SoulNetworkHandler.syphonFromNetwork(owner, cost);
                break;
            }
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostFallingTower[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> meteorRitual = new ArrayList();
        meteorRitual.add(new RitualComponent(2, 0, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 0, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 0, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 0, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 0, 1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(3, 0, -1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(1, 0, 3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(1, 0, -3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(4, 0, 2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(4, 0, -2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(2, 0, 4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(2, 0, -4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(5, 0, 3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(5, 0, -3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-5, 0, 3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-5, 0, -3, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(3, 0, 5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-3, 0, 5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(3, 0, -5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-3, 0, -5, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-4, 0, -4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(-4, 0, 4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(4, 0, 4, RitualComponent.DUSK));
        meteorRitual.add(new RitualComponent(4, 0, -4, RitualComponent.DUSK));

        for (int i = 4; i <= 6; i++) {
            meteorRitual.add(new RitualComponent(i, 0, 0, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(-i, 0, 0, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(0, 0, i, RitualComponent.EARTH));
            meteorRitual.add(new RitualComponent(0, 0, -i, RitualComponent.EARTH));
        }

        meteorRitual.add(new RitualComponent(8, 0, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-8, 0, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 0, 8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 0, -8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(8, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-8, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, 8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, -8, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(7, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(-7, 1, 0, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, 7, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(0, 1, -7, RitualComponent.EARTH));
        meteorRitual.add(new RitualComponent(7, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-7, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, 7, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, -7, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(6, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-6, 2, 0, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, 6, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(0, 2, -6, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(6, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-6, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, 6, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, -6, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(5, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-5, 3, 0, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, 5, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(0, 3, -5, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(5, 4, 0, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(-5, 4, 0, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(0, 4, 5, RitualComponent.AIR));
        meteorRitual.add(new RitualComponent(0, 4, -5, RitualComponent.AIR));

        for (int i = -1; i <= 1; i++) {
            meteorRitual.add(new RitualComponent(i, 4, 4, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(i, 4, -4, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(4, 4, i, RitualComponent.AIR));
            meteorRitual.add(new RitualComponent(-4, 4, i, RitualComponent.AIR));
        }

        meteorRitual.add(new RitualComponent(2, 4, 4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(4, 4, 2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(2, 4, -4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-4, 4, 2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-2, 4, 4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(4, 4, -2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-2, 4, -4, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(-4, 4, -2, RitualComponent.WATER));
        meteorRitual.add(new RitualComponent(2, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(3, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(2, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, 2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, 3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-2, 4, -3, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, -2, RitualComponent.FIRE));
        meteorRitual.add(new RitualComponent(-3, 4, -3, RitualComponent.FIRE));
        return meteorRitual;
    }
}
