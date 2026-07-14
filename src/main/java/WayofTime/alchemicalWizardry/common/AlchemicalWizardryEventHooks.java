package WayofTime.alchemicalWizardry.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.BloodMagicConfiguration;
import WayofTime.alchemicalWizardry.ModItems;
import WayofTime.alchemicalWizardry.api.BlockStack;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.event.TeleposeEvent;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.api.spell.APISpellHelper;
import WayofTime.alchemicalWizardry.common.achievements.ModAchievements;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.IHoardDemon;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.items.EnergySword;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmour;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;
import WayofTime.alchemicalWizardry.common.omega.ReagentRegenConfiguration;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import vazkii.botania.api.internal.IManaBurst;

public class AlchemicalWizardryEventHooks {

    public static Map<String, Boolean> playerFlightBuff = new HashMap<>();
    public static List<String> playersWith1Step = new ArrayList<>();

    public static Map<Integer, List<CoordAndRange>> respawnMap = new HashMap<>();
    public static Map<Integer, List<CoordAndRange>> forceSpawnMap = new HashMap<>();
    public static ArrayList<BlockStack> teleposerBlacklist = new ArrayList<>();

    public static Random rand = new Random();

    @SubscribeEvent
    public void onEntityInteractEvent(EntityInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        OmegaParadigm parad = OmegaRegistry.getOmegaParadigmOfWeilder(player);
        if (parad == null) {
            return;
        }
        ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) {
            parad.onEmptyHandEntityInteract(player, event.target);
        } else if (heldItem.getItem() instanceof EnergySword) {
            parad.onBoundSwordInteractWithEntity(player, event.target);
        }
    }

    @SubscribeEvent
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        if (event.isCancelable() && event.left != null
                && event.right != null
                && (event.left.getItem() instanceof BoundArmour || event.right.getItem() instanceof BoundArmour)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingHurtEvent(LivingHurtEvent event) {
        if (!event.isCanceled() && event.entityLiving instanceof EntityPlayerMP player
                && !event.entityLiving.worldObj.isRemote) {

            if (APISpellHelper.getCurrentAdditionalMaxHP(player) > 0) {
                APISpellHelper.setPlayerReagentRegenCooldownTag(player, 20 * 20);
            }

            float prevHp = APISpellHelper.getCurrentAdditionalHP(player);
            if (prevHp > 0) {
                float recalculatedAmount = ArmorProperties
                        .ApplyArmor(player, player.inventory.armorInventory, event.source, event.ammount);
                if (recalculatedAmount <= 0) return;
                recalculatedAmount = SpellHelper
                        .applyPotionDamageCalculations(player, event.source, recalculatedAmount); // Recalculated damage

                float ratio = recalculatedAmount / event.ammount;

                float f1 = recalculatedAmount;
                recalculatedAmount = Math.max(recalculatedAmount - player.getAbsorptionAmount(), 0.0F);
                player.setAbsorptionAmount(player.getAbsorptionAmount() - (f1 - recalculatedAmount));

                if (prevHp > recalculatedAmount) {
                    float hp = (prevHp - recalculatedAmount); // New HP - this is obviously > 0...

                    // event.setCanceled(true);
                    event.ammount = 0;
                    Reagent reagent = APISpellHelper.getPlayerReagentType(player);
                    OmegaParadigm paradigm = OmegaRegistry.getParadigmForReagent(reagent);
                    if (paradigm != null) {
                        ItemStack chestStack = player.inventory.armorInventory[2];

                        if (chestStack != null && chestStack.getItem() instanceof OmegaArmour) {
                            if (((OmegaArmour) chestStack.getItem()).paradigm == paradigm) {
                                paradigm.onHPBarDepleted(player, chestStack);
                            }
                        }
                    }

                    APISpellHelper.setCurrentAdditionalHP(player, hp);
                    NewPacketHandler.INSTANCE.sendTo(
                            NewPacketHandler.getAddedHPPacket(hp, APISpellHelper.getCurrentAdditionalMaxHP(player)),
                            player);
                } else {
                    APISpellHelper.setCurrentAdditionalHP(player, 0);
                    NewPacketHandler.INSTANCE.sendTo(
                            NewPacketHandler.getAddedHPPacket(0, APISpellHelper.getCurrentAdditionalMaxHP(player)),
                            player);

                    event.ammount -= prevHp / ratio;
                    if (event.ammount < 0) {
                        event.ammount = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (!(event.source.getEntity() instanceof EntityPlayer player)) {
            return;
        }
        ItemStack item = player.getCurrentEquippedItem();
        if (item != null && item.getItem() instanceof EnergySword sword
                && IBindable.checkAndSetItemOwner(item, player)
                && EnergyItems.syphonBatteries(item, player, sword.drainCost())) {
            event.entityLiving.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (!event.source.getDamageType().equals("player") || event.entityLiving instanceof EntityAnimal) {
            return;
        }
        PotionEffect effect = event.entityLiving.getActivePotionEffect(Potion.weakness);

        if (effect != null && effect.getAmplifier() >= 2 && Math.random() < 0.50d) {
            event.entityLiving.dropItem(ModItems.weakBloodShard, 1);
        }
    }

    @SubscribeEvent
    public void omegaUpdateReagentAndHpEvent(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer player && !event.entityLiving.worldObj.isRemote) {
            Reagent reagent = APISpellHelper.getPlayerReagentType(player);
            float reagentAmount = APISpellHelper.getPlayerCurrentReagentAmount(player);

            boolean hasReagentChanged = false;

            if (reagentAmount > 0 && OmegaRegistry.hasParadigm(reagent)) {
                int cooldown = APISpellHelper.getPlayerReagentRegenCooldownTag(player);
                boolean canHealHPBar = true;
                if (cooldown > 0) {
                    float extra = 0;
                    if (player.isPotionActive(AlchemicalWizardry.customPotionSoulHarden)) {
                        extra += 0.25f * (1 + player.getActivePotionEffect(AlchemicalWizardry.customPotionSoulHarden)
                                .getAmplifier());
                    }

                    if (player.isPotionActive(AlchemicalWizardry.customPotionSoulFray)) {
                        extra -= 0.25f * (1
                                + player.getActivePotionEffect(AlchemicalWizardry.customPotionSoulFray).getAmplifier());
                    }

                    int deduction = -1 - (extra >= 0 ? (rand.nextFloat() < extra ? 1 : 0)
                            : (rand.nextFloat() < -extra / 2 ? -1 : 0));

                    APISpellHelper.setPlayerReagentRegenCooldownTag(player, Math.max(cooldown + deduction, 0));
                    canHealHPBar = false;
                }

                OmegaParadigm parad = OmegaRegistry.getParadigmForReagent(reagent);
                ReagentRegenConfiguration config = parad.getRegenConfig(player);

                if (parad.isPlayerWearingFullSet(player)) {
                    if (canHealHPBar) {
                        int tickRate = config.tickRate;

                        if (player.isPotionActive(Potion.regeneration)) {
                            int i = player.getActivePotionEffect(Potion.regeneration).getAmplifier();
                            double factor = Math.pow(1.5, i + 1);
                            tickRate = Math.max((int) (tickRate / factor), 1);
                        }

                        if (event.entityLiving.worldObj.getWorldTime() % tickRate == 0) {
                            boolean hasHealthChanged = false;
                            int maxHealth = parad.getMaxAdditionalHealth();

                            float health = APISpellHelper.getCurrentAdditionalHP(player);

                            if (health > maxHealth) {
                                health = maxHealth;
                                hasHealthChanged = true;
                            } else if (health < maxHealth) {
                                float addedAmount = Math.min(
                                        Math.min((reagentAmount / config.costPerPoint), config.healPerTick),
                                        maxHealth - health);
                                float drain = addedAmount * config.costPerPoint;

                                reagentAmount -= drain;
                                health += addedAmount;
                                hasHealthChanged = true;
                            }

                            if (player instanceof EntityPlayerMP) {
                                if (hasHealthChanged) {
                                    APISpellHelper.setCurrentAdditionalHP(player, health);
                                    NewPacketHandler.INSTANCE.sendTo(
                                            NewPacketHandler.getAddedHPPacket(health, maxHealth),
                                            (EntityPlayerMP) player);
                                }
                            }
                        }
                    }
                } else {
                    reagentAmount = 0;
                    APISpellHelper.setPlayerMaxReagentAmount(player, 0);
                    NewPacketHandler.INSTANCE
                            .sendTo(NewPacketHandler.getReagentBarPacket(null, 0, 0), (EntityPlayerMP) player);
                    APISpellHelper.setCurrentAdditionalHP(player, 0);
                    NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getAddedHPPacket(0, 0), (EntityPlayerMP) player);
                }
                // Consumes the amount
                float costPerTick = parad.getCostPerTickOfUse(player);
                if (parad.doDrainReagent(player)) {
                    if (reagentAmount > costPerTick) {
                        reagentAmount = Math.max(0, reagentAmount - costPerTick);
                    } else {
                        reagentAmount = 0;
                    }
                }

                hasReagentChanged = true;
            }

            if (reagentAmount <= 0) {
                boolean hasRevertedArmour = false;
                ItemStack[] armourInventory = player.inventory.armorInventory;
                for (ItemStack stack : armourInventory) {
                    if (stack != null && stack.getItem() instanceof OmegaArmour) {
                        ((OmegaArmour) stack.getItem()).revertArmour(player, stack);
                        hasRevertedArmour = true;
                    }
                }

                if (hasRevertedArmour) {
                    APISpellHelper.setCurrentAdditionalHP(player, 0);
                    NewPacketHandler.INSTANCE.sendTo(NewPacketHandler.getAddedHPPacket(0, 0), (EntityPlayerMP) player);
                }
            }

            if (player instanceof EntityPlayerMP p) {
                if (hasReagentChanged) {
                    APISpellHelper.setPlayerCurrentReagentAmount(player, reagentAmount);
                    NewPacketHandler.INSTANCE.sendTo(
                            NewPacketHandler.getReagentBarPacket(
                                    reagent,
                                    reagentAmount,
                                    APISpellHelper.getPlayerMaxReagentAmount(player)),
                            p);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDamageEvent(LivingAttackEvent event) {
        if (event.source.isProjectile() && event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionProjProt)
                && event.isCancelable()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingSpawnEvent(CheckSpawn event) {
        if (!(event.entityLiving instanceof EntityMob)) {
            return;
        }

        String respawnRitual = "AW028SpawnWard";

        int dimension = event.world.provider.dimensionId;
        if (respawnMap.containsKey(dimension)) {
            List<CoordAndRange> list = respawnMap.get(dimension);

            if (list != null) {
                for (CoordAndRange coords : list) {
                    TileEntity tile = event.world.getTileEntity(coords.xCoord, coords.yCoord, coords.zCoord);

                    if (!(tile instanceof TEMasterStone masterStone) || !masterStone.isRunning
                            || !masterStone.getCurrentRitual().equals(respawnRitual)) {
                        list.remove(coords);
                        continue;
                    }
                    if (event.x <= coords.xCoord - coords.horizRadius || !(event.x < coords.xCoord + coords.horizRadius)
                            || !(event.z > coords.zCoord - coords.horizRadius)
                            || !(event.z < coords.zCoord + coords.horizRadius)
                            || !(event.y > coords.yCoord - coords.vertRadius)
                            || !(event.y < coords.yCoord + coords.vertRadius)) {
                        continue;
                    }
                    switch (event.getResult()) {
                        case ALLOW -> event.setResult(Result.DEFAULT);
                        case DEFAULT -> event.setResult(Result.DENY);
                    }
                    break;
                }
            }
        }

        if (event.entityLiving instanceof EntityCreeper) {
            return;
        }

        String forceSpawnRitual = "AW029VeilOfEvil";

        if (forceSpawnMap.containsKey(dimension)) {
            List<CoordAndRange> list = forceSpawnMap.get(dimension);

            if (list != null) {
                for (CoordAndRange coords : list) {
                    TileEntity tile = event.world.getTileEntity(coords.xCoord, coords.yCoord, coords.zCoord);

                    if (!(tile instanceof TEMasterStone stone) || !stone.isRunning
                            || !stone.getCurrentRitual().equals(forceSpawnRitual)) {
                        list.remove(coords);
                        continue;
                    }
                    if (event.x <= coords.xCoord - coords.horizRadius || !(event.x < coords.xCoord + coords.horizRadius)
                            || !(event.z > coords.zCoord - coords.horizRadius)
                            || !(event.z < coords.zCoord + coords.horizRadius)
                            || !(event.y > coords.yCoord - coords.vertRadius)
                            || !(event.y < coords.yCoord + coords.vertRadius)) {
                        continue;
                    }
                    switch (event.getResult()) {
                        case DEFAULT -> event.setResult(Result.ALLOW);
                        case DENY -> event.setResult(Result.DEFAULT);
                    }
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (AlchemicalWizardry.respawnWithDebuff && event.player != null) {
            event.player.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionSoulFray.id, 20 * 60 * 5, 0));
        }
    }

    @SubscribeEvent
    public void onLivingJumpEvent(LivingJumpEvent event) {
        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionBoost)) {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
            event.entityLiving.motionY += (0.1f) * (2 + i);
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionHeavyHeart)) {
            event.entityLiving.motionY = 0;
        }
    }

    @SubscribeEvent
    public void onEndermanTeleportEvent(EnderTeleportEvent event) {
        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionPlanarBinding) && event.isCancelable()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingAttackEvent event) {
        EntityLivingBase entityAttacked = event.entityLiving;

        if (entityAttacked.isPotionActive(AlchemicalWizardry.customPotionReciprocation)) {
            Entity entityAttacking = event.source.getSourceOfDamage();

            if (entityAttacking instanceof EntityLivingBase) {
                int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionReciprocation)
                        .getAmplifier();
                float damageRecieve = event.ammount / 2 * (i + 1);
                entityAttacking.attackEntityFrom(DamageSource.generic, damageRecieve);
            }
        }

        if (entityAttacked.isPotionActive(AlchemicalWizardry.customPotionFlameCloak)) {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFlameCloak).getAmplifier();

            Entity entityAttacking = event.source.getSourceOfDamage();

            if (entityAttacking instanceof EntityLivingBase entity && !entityAttacking.isImmuneToFire()
                    && !entity.isPotionActive(Potion.fireResistance)) {
                entityAttacking.attackEntityFrom(DamageSource.inFire, 2 * i + 2);
                entityAttacking.setFire(3);
            }
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingUpdateEvent event) {
        EntityLivingBase entityLiving = event.entityLiving;
        double x = entityLiving.posX;
        double y = entityLiving.posY;
        double z = entityLiving.posZ;

        Vec3 blockVector = SpellHelper.getEntityBlockVector(entityLiving);
        int xPos = (int) (blockVector.xCoord);
        int yPos = (int) (blockVector.yCoord);
        int zPos = (int) (blockVector.zCoord);

        if (entityLiving instanceof EntityPlayerMP player) {
            if (!entityLiving.worldObj.isRemote && entityLiving.worldObj.getTotalWorldTime() % 20 == 0) {
                String ownerName = SoulNetworkHandler.getUsername(player);
                NewPacketHandler.INSTANCE.sendTo(
                        NewPacketHandler.getLPPacket(
                                SoulNetworkHandler.getCurrentEssence(ownerName),
                                SoulNetworkHandler.getMaxEssence(ownerName)),
                        player);
            }
        }

        if (entityLiving instanceof EntityPlayer entityPlayer && entityLiving.worldObj.isRemote) {
            boolean highStepListed = playersWith1Step.contains(entityPlayer.getDisplayName());
            boolean hasHighStep = entityPlayer.isPotionActive(AlchemicalWizardry.customPotionBoost);

            if (hasHighStep && !highStepListed) {
                playersWith1Step.add(SpellHelper.getUsername(entityPlayer));
            }

            if (!hasHighStep && highStepListed) {
                playersWith1Step.remove(SpellHelper.getUsername(entityPlayer));
                entityPlayer.stepHeight = 0.5F;
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionFeatherFall)) {
            event.entityLiving.fallDistance = 0;
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionDrowning)
                && !event.entityLiving.isPotionActive(Potion.waterBreathing)) {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionDrowning).getAmplifier();

            if (event.entityLiving.worldObj.getWorldTime() % (20 / (i + 1)) == 0) {
                event.entityLiving.attackEntityFrom(DamageSource.drown, 2);
                event.entityLiving.hurtResistantTime = Math.min(event.entityLiving.hurtResistantTime, 20 / (i + 1));
            }
        }

        // event.entityLiving.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeAllModifiers();

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionBoost)) {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
            if (!event.entityLiving.isSneaking() && event.entityLiving instanceof EntityPlayer entityPlayer) {
                float percentIncrease = (i + 1) * 0.05f;
                entityPlayer.stepHeight = 1.0f;

                if ((entityPlayer.onGround || entityPlayer.capabilities.isFlying) && entityPlayer.moveForward > 0F)
                    entityPlayer.moveFlying(
                            0F,
                            1F,
                            entityPlayer.capabilities.isFlying ? (percentIncrease / 2.0f) : percentIncrease);
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionProjProt)) {
            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionProjProt).getAmplifier();
            EntityLivingBase entity = event.entityLiving;
            int posX = (int) Math.round(entity.posX - 0.5f);
            int posY = (int) Math.round(entity.posY);
            int posZ = (int) Math.round(entity.posZ - 0.5f);
            int d0 = (int) ((i + 1) * 2.5);
            AxisAlignedBB axisalignedbb = AxisAlignedBB
                    .getBoundingBox(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5)
                    .expand(d0, d0, d0);
            List<Entity> list = event.entityLiving.worldObj.getEntitiesWithinAABB(Entity.class, axisalignedbb);

            for (Entity projectile : list) {
                if (!(projectile instanceof IProjectile)
                        || (AlchemicalWizardry.isBotaniaLoaded && isManaBurst(projectile))) {
                    continue;
                }

                Entity throwingEntity = switch (projectile) {
                    case EntityArrow entityArrow -> entityArrow.shootingEntity;
                    case EnergyBlastProjectile energyBlastProjectile -> energyBlastProjectile.shootingEntity;
                    case EntityThrowable entityThrowable -> entityThrowable.getThrower();
                    default -> null;
                };

                if (throwingEntity != null && throwingEntity.equals(entity)) {
                    continue;
                }

                double delX = projectile.posX - entity.posX;
                double delY = projectile.posY - entity.posY;
                double delZ = projectile.posZ - entity.posZ;

                double angle = (delX * projectile.motionX + delY * projectile.motionY + delZ * projectile.motionZ)
                        / (Math.sqrt(delX * delX + delY * delY + delZ * delZ) * Math.sqrt(
                                projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY
                                        + projectile.motionZ * projectile.motionZ));
                angle = Math.acos(angle);
                if (angle < 3 * (Math.PI / 4)) {
                    // angle is < 135 degrees
                    continue;
                }

                if (throwingEntity != null) {
                    delX = -projectile.posX + throwingEntity.posX;
                    delY = -projectile.posY + (throwingEntity.posY + throwingEntity.getEyeHeight());
                    delZ = -projectile.posZ + throwingEntity.posZ;
                }

                double curVel = Math.sqrt(delX * delX + delY * delY + delZ * delZ);

                delX /= curVel;
                delY /= curVel;
                delZ /= curVel;
                double newVel = Math.sqrt(
                        projectile.motionX * projectile.motionX + projectile.motionY * projectile.motionY
                                + projectile.motionZ * projectile.motionZ);
                projectile.motionX = newVel * delX;
                projectile.motionY = newVel * delY;
                projectile.motionZ = newVel * delZ;
                // TODO make this not affect player's projectiles
            }
        }

        if (event.entityLiving.isPotionActive(AlchemicalWizardry.customPotionFlight)) {
            if (event.entityLiving instanceof EntityPlayer entityPlayer) {
                String ownerName = SpellHelper.getUsername(entityPlayer);
                playerFlightBuff.put(ownerName, true);
                entityPlayer.capabilities.allowFlying = true;
            }
        } else {
            if (event.entityLiving instanceof EntityPlayer entityPlayer) {
                String ownerName = SpellHelper.getUsername(entityPlayer);

                if (!playerFlightBuff.containsKey(ownerName)) {
                    playerFlightBuff.put(ownerName, false);
                }

                if (playerFlightBuff.get(ownerName)) {
                    playerFlightBuff.put(ownerName, false);

                    if (!entityPlayer.capabilities.isCreativeMode) {
                        entityPlayer.capabilities.allowFlying = false;
                        entityPlayer.capabilities.isFlying = false;
                        entityPlayer.sendPlayerAbilities();
                    }
                }
            }
        }

        if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionFlameCloak)) {
            entityLiving.worldObj.spawnParticle(
                    "flame",
                    x + SpellHelper.gaussian(1),
                    y - 1.3 + SpellHelper.gaussian(0.3),
                    z + SpellHelper.gaussian(1),
                    0,
                    0.06d,
                    0);

            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFlameCloak).getAmplifier();
            double range = i * 0.5;

            List<Entity> entities = SpellHelper.getEntitiesInRange(entityLiving.worldObj, x, y, z, range, range);
            if (entities != null) {
                for (Entity entity : entities) {
                    if (!entity.equals(entityLiving) && !entity.isImmuneToFire()
                            && !(entity instanceof EntityLivingBase
                                    && ((EntityLivingBase) entity).isPotionActive(Potion.fireResistance))) {
                        entity.setFire(3);
                    }
                }
            }
        }

        if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionIceCloak)) {
            if (entityLiving.worldObj.getWorldTime() % 2 == 0) entityLiving.worldObj.spawnParticle(
                    "reddust",
                    x + SpellHelper.gaussian(1),
                    y - 1.3 + SpellHelper.gaussian(0.3),
                    z + SpellHelper.gaussian(1),
                    0x74,
                    0xbb,
                    0xfb);

            int r = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionIceCloak).getAmplifier();
            int horizRange = r + 1;

            if (!entityLiving.worldObj.isRemote) {
                for (int i = -horizRange; i <= horizRange; i++) {
                    for (int k = -horizRange; k <= horizRange; k++) {
                        for (int j = -2; j <= 0; j++) {
                            SpellHelper.freezeWaterBlock(entityLiving.worldObj, xPos + i, yPos + j, zPos + k);
                        }
                    }
                }
            }
        }

        if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionHeavyHeart)) {
            entityLiving.worldObj.spawnParticle(
                    "flame",
                    x + SpellHelper.gaussian(1),
                    y - 1.3 + SpellHelper.gaussian(0.3),
                    z + SpellHelper.gaussian(1),
                    0,
                    0.06d,
                    0);

            int i = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionHeavyHeart).getAmplifier();
            double decrease = 0.025 * (i + 1);

            if (entityLiving.motionY > -0.9) {
                entityLiving.motionY -= decrease;
            }

            if (entityLiving instanceof EntityPlayer) {
                SpellHelper.setPlayerSpeedFromServer(
                        (EntityPlayer) entityLiving,
                        entityLiving.motionX,
                        entityLiving.motionY - decrease,
                        entityLiving.motionZ);
            }
        }

        if (entityLiving.isPotionActive(AlchemicalWizardry.customPotionFireFuse)) {
            entityLiving.worldObj.spawnParticle(
                    "flame",
                    x + SpellHelper.gaussian(1),
                    y - 1.3 + SpellHelper.gaussian(0.3),
                    z + SpellHelper.gaussian(1),
                    0,
                    0.06d,
                    0);

            int r = event.entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFireFuse).getAmplifier();
            int radius = r + 1;

            if (entityLiving.getActivePotionEffect(AlchemicalWizardry.customPotionFireFuse).getDuration() <= 2) {
                entityLiving.worldObj.createExplosion(null, x, y, z, radius, false);
            }
        }
    }

    @SubscribeEvent
    public void onTelepose(TeleposeEvent event) {
        BlockStack initialBlock = new BlockStack(event.initialBlock, event.initialMetadata);
        BlockStack finalBlock = new BlockStack(event.finalBlock, event.finalMetadata);

        if (teleposerBlacklist.contains(initialBlock) || teleposerBlacklist.contains(finalBlock))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        EntityLivingBase entityLiving = event.entityLiving;

        if (entityLiving instanceof IDemon && event.source.getEntity() instanceof EntityPlayer player) {

            player.addStat(ModAchievements.demonSpawn, 1);
        }
        if (entityLiving instanceof IHoardDemon && event.source.getEntity() instanceof EntityPlayer player) {

            player.addStat(ModAchievements.demons, 1);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals("AWWayofTime")) {
            BloodMagicConfiguration.syncConfig();
            AlchemicalWizardry.logger.info("Refreshing configuration file.");
        }
    }

    @Optional.Method(modid = "Botania")
    private boolean isManaBurst(Entity entity) {
        if (entity instanceof IManaBurst burst) {
            ItemStack lens = burst.getSourceLens();
            return lens.getItemDamage() == 8 || lens.getItemDamage() == 11;
        }
        return false;
    }
}
