package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

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
        } else {
            boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
            boolean hasPotentia = this
                    .canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

            int teleportDistance = hasVirtus ? 300 : 100;
            int range = hasPotentia ? 50 : 25;
            List<EntityPlayer> playerList = SpellHelper
                    .getPlayersInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
            boolean flag = false;

            TileEntity tile = world.getTileEntity(x, y + 1, z);
            IInventory inventoryTile = null;
            if (tile instanceof IInventory) {
                inventoryTile = (IInventory) tile;
            }

            for (EntityPlayer entityplayer : playerList) {
                if (entityplayer.capabilities.isCreativeMode) {
                    continue;
                }
                String playerString = SpellHelper.getUsername(entityplayer);
                if (!playerString.equals(owner)) {
                    if (inventoryTile != null) {
                        boolean test = false;
                        for (int i = 0; i < inventoryTile.getSizeInventory(); i++) {
                            ItemStack stack = inventoryTile.getStackInSlot(i);
                            if (stack != null && stack.getItem() instanceof IBindable
                                    && IBindable.getOwnerName(stack).equals(playerString)) {
                                test = true;
                            }
                        }

                        if (test) {
                            continue;
                        }
                    }
                    flag = teleportRandomly(entityplayer, teleportDistance) || flag;
                }
            }

            if (flag) {
                if (hasVirtus) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, true);
                }

                if (hasPotentia) {
                    this.canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, true);
                }

                SoulNetworkHandler.syphonFromNetwork(owner, getCostPerRefresh());
            }
        }

        boolean hasTennebrae = this
                .canDrainReagent(ritualStone, ReagentRegistry.tenebraeReagent, tennebraeDrain, false);
        if (hasTennebrae && SoulNetworkHandler.canSyphonFromOnlyNetwork(owner, 1000)) {
            boolean hasVirtus = this.canDrainReagent(ritualStone, ReagentRegistry.virtusReagent, virtusDrain, false);
            boolean hasPotentia = this
                    .canDrainReagent(ritualStone, ReagentRegistry.potentiaReagent, potentiaDrain, false);

            int teleportDistance = hasVirtus ? 300 : 100;
            int range = hasPotentia ? 50 : 25;
            List<EntityLivingBase> livingList = SpellHelper
                    .getLivingEntitiesInRange(world, x + 0.5, y + 0.5, z + 0.5, range, range);
            boolean flag = false;

            for (EntityLivingBase livingEntity : livingList) {
                if (livingEntity instanceof EntityPlayer) {
                    continue;
                }

                flag = teleportRandomly(livingEntity, teleportDistance) || flag;
            }

            if (flag) {
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

    public boolean teleportRandomly(EntityLivingBase entityLiving, double distance) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (player.capabilities.isCreativeMode) return false;
        }

        double x = entityLiving.posX;
        double y = entityLiving.posY;
        double z = entityLiving.posZ;
        Random rand = new Random();
        double d0 = x + (rand.nextDouble() - 0.5D) * distance;
        double d1 = y + (double) (rand.nextInt((int) distance) - (distance) / 2);
        double d2 = z + (rand.nextDouble() - 0.5D) * distance;
        int i = 0;

        while (!teleportTo(entityLiving, d0, d1, d2, x, y, z) && i < 100) {
            d0 = x + (rand.nextDouble() - 0.5D) * distance;
            d1 = y + (double) (rand.nextInt((int) distance) - (distance) / 2);
            d2 = z + (rand.nextDouble() - 0.5D) * distance;
            i++;
        }

        if (i >= 100) {
            return false;
        }

        return true;
    }

    public boolean teleportTo(EntityLivingBase entityLiving, double par1, double par3, double par5, double lastX,
            double lastY, double lastZ) {
        EnderTeleportEvent event = new EnderTeleportEvent(entityLiving, par1, par3, par5, 0);

        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        double d3 = lastX;
        double d4 = lastY;
        double d5 = lastZ;
        SpellTeleport.moveEntityViaTeleport(entityLiving, event.targetX, event.targetY, event.targetZ);
        boolean flag = false;
        int i = MathHelper.floor_double(entityLiving.posX);
        int j = MathHelper.floor_double(entityLiving.posY);
        int k = MathHelper.floor_double(entityLiving.posZ);
        int l;

        if (entityLiving.worldObj.blockExists(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                Block block = entityLiving.worldObj.getBlock(i, j - 1, k);

                if (block != null && block.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --entityLiving.posY;
                    --j;
                }
            }

            if (flag1) {
                SpellTeleport
                        .moveEntityViaTeleport(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ);

                if (entityLiving.worldObj.getCollidingBoundingBoxes(entityLiving, entityLiving.boundingBox).isEmpty()
                        && !entityLiving.worldObj.isAnyLiquid(entityLiving.boundingBox)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            SpellTeleport.moveEntityViaTeleport(entityLiving, d3, d4, d5);
            return false;
        } else {
            short short1 = 128;

            for (l = 0; l < short1; ++l) {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (entityLiving.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (entityLiving.posX - d3) * d6
                        + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                double d8 = d4 + (entityLiving.posY - d4) * d6
                        + entityLiving.worldObj.rand.nextDouble() * (double) entityLiving.height;
                double d9 = d5 + (entityLiving.posZ - d5) * d6
                        + (entityLiving.worldObj.rand.nextDouble() - 0.5D) * (double) entityLiving.width * 2.0D;
                entityLiving.worldObj.spawnParticle("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }
            return true;
        }
    }

    public void moveEntityViaTeleport(EntityLivingBase entityLiving, double x, double y, double z) {
        if (entityLiving instanceof EntityPlayer) {
            if (entityLiving != null && entityLiving instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP) entityLiving;

                if (entityplayermp.worldObj == entityLiving.worldObj) {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, x, y, z, 5.0F);

                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        if (entityLiving.isRiding()) {
                            entityLiving.mountEntity((Entity) null);
                        }
                        entityLiving.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                    }
                }
            }
        } else if (entityLiving != null) {
            entityLiving.setPosition(x, y, z);
        }
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> expulsionRitual = new ArrayList();
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
