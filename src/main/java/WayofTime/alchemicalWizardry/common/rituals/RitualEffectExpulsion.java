package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.simple.SpellTeleport;

public class RitualEffectExpulsion extends RitualEffect {

    public static final int virtusDrain = 10;
    public static final int potentiaDrain = 10;
    public static final int tennebraeDrain = 5;

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
        boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
        boolean hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

        int teleportDistance = hasVirtus ? 300 : 100;
        int range = hasPotentia ? 50 : 25;
        List<EntityPlayer> playerList = SpellHelper.getPlayersInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
        boolean teleported = false;

        TileEntity tile = world.getTileEntity(x, y + 1, z);
        IInventory inventoryTile = null;
        if (tile instanceof IInventory inv) {
            inventoryTile = inv;
        }

        players: for (EntityPlayer entityplayer : playerList) {
            if (entityplayer.capabilities.isCreativeMode) {
                continue;
            }
            String playerString = SpellHelper.getUsername(entityplayer);
            if (!playerString.equals(owner)) {
                if (inventoryTile != null) {
                    for (int i = 0; i < inventoryTile.getSizeInventory(); i++) {
                        ItemStack stack = inventoryTile.getStackInSlot(i);
                        if (stack != null && stack.getItem() instanceof IBindable
                                && IBindable.getOwnerName(stack).equals(playerString)) {
                            continue players;
                        }
                    }
                }
                teleported = SpellTeleport.teleportRandomly(entityplayer, teleportDistance) || teleported;
            }
        }

        if (teleported) {
            if (hasVirtus) {
                this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
            }

            if (hasPotentia) {
                this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
            }

            SoulNetworkHandler.syphonFromNetwork(owner, getCostPerRefresh());
        }

        if (this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tennebraeDrain, false)
                && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, 1000)) {
            hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
            hasPotentia = this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

            teleportDistance = hasVirtus ? 300 : 100;
            range = hasPotentia ? 50 : 25;
            List<EntityLivingBase> livingList = SpellHelper
                    .getLivingEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
            teleported = false;

            for (EntityLivingBase livingEntity : livingList) {
                if (livingEntity instanceof EntityPlayer) {
                    continue;
                }

                teleported = SpellTeleport.teleportRandomly(livingEntity, teleportDistance) || teleported;
            }

            if (teleported) {
                if (hasVirtus) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
                }

                if (hasPotentia) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                }

                this.canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tennebraeDrain, true);

                SoulNetworkHandler.syphonFromNetwork(owner, 1000);
            }
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostExpulsion[1];
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> expulsionRitual = new ArrayList<>();
        expulsionRitual.add(new RitualComponent(2, 0, 2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(2, 0, 1, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(1, 0, 2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(2, 0, -2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(2, 0, -1, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-2, 0, 2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(1, 0, -2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-2, 0, -2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(4, 0, 2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(5, 0, 2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(4, 0, -2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(5, 0, -2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-4, 0, 2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-5, 0, 2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-4, 0, -2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-5, 0, -2, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(2, 0, 4, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(2, 0, 5, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-2, 0, 4, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-2, 0, 5, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(2, 0, -4, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(2, 0, -5, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-2, 0, -4, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(-2, 0, -5, RitualComponent.AIR));
        expulsionRitual.add(new RitualComponent(0, 0, 6, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(0, 0, -6, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(6, 0, 0, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-6, 0, 0, RitualComponent.EARTH));
        expulsionRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(-6, 0, 1, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(-6, 0, -1, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(5, 0, 0, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(6, 0, 1, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(6, 0, -1, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(0, 0, 5, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(1, 0, 6, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(-1, 0, 6, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(0, 0, -5, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(1, 0, -6, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(-1, 0, -6, RitualComponent.DUSK));
        expulsionRitual.add(new RitualComponent(4, 0, 4, RitualComponent.FIRE));
        expulsionRitual.add(new RitualComponent(4, 0, -4, RitualComponent.FIRE));
        expulsionRitual.add(new RitualComponent(-4, 0, 4, RitualComponent.FIRE));
        expulsionRitual.add(new RitualComponent(-4, 0, -4, RitualComponent.FIRE));
        return expulsionRitual;
    }
}
