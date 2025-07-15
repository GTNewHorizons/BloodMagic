package WayofTime.alchemicalWizardry;

import static net.minecraft.entity.EntityList.stringToClassMapping;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.api.BlockStack;
import WayofTime.alchemicalWizardry.api.rituals.Rituals;
import WayofTime.alchemicalWizardry.client.renderer.ColourThreshold;
import WayofTime.alchemicalWizardry.client.renderer.RenderHelper;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardryEventHooks;
import WayofTime.alchemicalWizardry.common.demonVillage.DemonVillagePath;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorComponent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Created with IntelliJ IDEA. User: Pokefenn Date: 17/01/14 Time: 19:50
 */
public class BloodMagicConfiguration {

    private static final String DEFAULT_COLOR_LIST = "100,f; 80,7; 60,e; 40,6; 25,c; 10,4";
    public static final List<ColourThreshold> colorList = new ArrayList<ColourThreshold>();

    public static Configuration config;

    public static String[] teleposerBlacklist;
    public static String[] blacklist = {};

    public static void init(File configFile) {
        for (String s : DEFAULT_COLOR_LIST.split(";")) {
            String[] ct = s.split(",");
            colorList.add(new ColourThreshold(Integer.valueOf(ct[0].trim()), ct[1].trim()));
        }

        config = new Configuration(configFile);

        try {
            config.load();
            syncConfig();

        } catch (Exception e) {
            AlchemicalWizardry.logger
                    .error("There has been a problem loading the configuration, go ask on the forums :p");

        } finally {
            config.save();
        }
    }

    public static void syncConfig() {
        AlchemicalWizardry.standardBindingAgentDungeonChance = config
                .get("Dungeon Loot Chances", "standardBindingAgent", 30).getInt();
        AlchemicalWizardry.mundanePowerCatalystDungeonChance = config
                .get("Dungeon Loot Chances", "mundanePowerCatalyst", 20).getInt();
        AlchemicalWizardry.averagePowerCatalystDungeonChance = config
                .get("Dungeon Loot Chances", "averagePowerCatalyst", 10).getInt();
        AlchemicalWizardry.greaterPowerCatalystDungeonChance = config
                .get("Dungeon Loot Chances", "greaterPowerCatalyst", 5).getInt();
        AlchemicalWizardry.mundaneLengtheningCatalystDungeonChance = config
                .get("Dungeon Loot Chances", "mundaneLengtheningCatalyst", 20).getInt();
        AlchemicalWizardry.averageLengtheningCatalystDungeonChance = config
                .get("Dungeon Loot Chances", "averageLengtheningCatalyst", 10).getInt();
        AlchemicalWizardry.greaterLengtheningCatalystDungeonChance = config
                .get("Dungeon Loot Chances", "greaterLengtheningCatalyst", 5).getInt();
        AlchemicalWizardry.customPotionDrowningID = config.get("Potion ID", "Drowning", 100).getInt();
        AlchemicalWizardry.customPotionBoostID = config.get("Potion ID", "Boost", 101).getInt();
        AlchemicalWizardry.customPotionProjProtID = config.get("Potion ID", "ProjProt", 102).getInt();
        AlchemicalWizardry.customPotionInhibitID = config.get("Potion ID", "Inhibit", 103).getInt();
        AlchemicalWizardry.customPotionFlightID = config.get("Potion ID", "Flight", 104).getInt();
        AlchemicalWizardry.customPotionReciprocationID = config.get("Potion ID", "Reciprocation", 105).getInt();
        AlchemicalWizardry.customPotionFlameCloakID = config.get("Potion ID", "FlameCloak", 106).getInt();
        AlchemicalWizardry.customPotionIceCloakID = config.get("Potion ID", "IceCloak", 107).getInt();
        AlchemicalWizardry.customPotionHeavyHeartID = config.get("Potion ID", "HeavyHeart", 108).getInt();
        AlchemicalWizardry.customPotionFireFuseID = config.get("Potion ID", "FireFuse", 109).getInt();
        AlchemicalWizardry.customPotionPlanarBindingID = config.get("Potion ID", "PlanarBinding", 110).getInt();
        AlchemicalWizardry.customPotionSoulFrayID = config.get("Potion ID", "SoulFray", 111).getInt();
        AlchemicalWizardry.customPotionSoulHardenID = config.get("Potion ID", "SoulHarden", 112).getInt();
        AlchemicalWizardry.customPotionDeafID = config.get("Potion ID", "Deaf", 113).getInt();
        AlchemicalWizardry.customPotionFeatherFallID = config.get("Potion ID", "FeatherFall", 114).getInt();
        AlchemicalWizardry.customPotionDemonCloakID = config.get("Potion ID", "DemonCloak", 115).getInt();
        AlchemicalWizardry.customPotionAmphibianID = config.get("Potion ID", "Amphibian", 116).getInt();

        AlchemicalWizardry.doMeteorsDestroyBlocks = config.get("meteor", "doMeteorsDestroyBlocks", true)
                .getBoolean(true);
        AlchemicalWizardry.defaultMeteorCost = config.getInt(
                "defaultMeteorCost",
                "meteor",
                1000000,
                0,
                Integer.MAX_VALUE,
                "The default LP cost to use for meteors if the \"cost\" property is not present in its config file.\n");
        AlchemicalWizardry.defaultMeteorBlock = config.getString(
                "defaultMeteorBlock",
                "meteor",
                "",
                "The block to use for filler in meteors if none is provided in the meteor's config file and "
                        + "that\nmeteor's fillerChance is greater than 0. Also used as a fallback for empty ore "
                        + "lists in meteor configs.\nSpecify the block with the "
                        + "format:\n\"modId:itemName:meta\"\nDefaults to minecraft:stone:0 if no block is provided or "
                        + "the provided block cannot be found.");
        MeteorComponent.setDefaultMeteorBlock();
        AlchemicalWizardry.allowedCrushedOresArray = config.get(
                "oreCrushing",
                "allowedOres",
                new String[] { "iron", "gold", "copper", "tin", "lead", "silver", "osmium" }).getStringList();

        AlchemicalWizardry.wimpySettings = config.get("WimpySettings", "IDontLikeFun", false).getBoolean(false);
        AlchemicalWizardry.respawnWithDebuff = config.get("WimpySettings", "RespawnWithDebuff", true).getBoolean();
        AlchemicalWizardry.causeHungerWithRegen = config.get("WimpySettings", "causeHungerWithRegen", true)
                .getBoolean();
        AlchemicalWizardry.causeHungerChatMessage = config.get("WimpySettings", "causeHungerChatMessage", true)
                .getBoolean();
        AlchemicalWizardry.allowPotionRepair = config.get("WimpySettings", "allowPotionRepair", true).getBoolean();
        // AlchemicalWizardry.lockdownAltar = config.get("WimpySettings", "LockdownAltarWithRegen", true).getBoolean();
        AlchemicalWizardry.lockdownAltar = false;
        AlchemicalWizardry.disableBoundToolsRightClick = config
                .get("WimpySettings", "disableBoundToolsRightClick", false).getBoolean(false);

        AlchemicalWizardry.ritualDisabledWater = config.get("Ritual Blacklist", "Ritual of the Full Spring", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledLava = config.get("Ritual Blacklist", "Serenade of the Nether", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledGreenGrove = config.get("Ritual Blacklist", "Ritual of the Green Grove", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledInterdiction = config.get("Ritual Blacklist", "Interdiction Ritual", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledContainment = config.get("Ritual Blacklist", "Ritual of Containment", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledBinding = config.get("Ritual Blacklist", "Ritual of Binding", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledUnbinding = config.get("Ritual Blacklist", "Ritual of Unbinding", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledHighJump = config.get("Ritual Blacklist", "Ritual of the High Jump", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledMagnetism = config.get("Ritual Blacklist", "Ritual of Magnetism", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledCrusher = config.get("Ritual Blacklist", "Ritual of the Crusher", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledSpeed = config.get("Ritual Blacklist", "Ritual of Speed", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledAnimalGrowth = config.get("Ritual Blacklist", "Ritual of the Shepherd", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledSuffering = config.get("Ritual Blacklist", "Well of Suffering", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledRegen = config.get("Ritual Blacklist", "Ritual of Regeneration", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledFeatheredKnife = config
                .get("Ritual Blacklist", "Ritual of the Feathered Knife", false).getBoolean(false);
        AlchemicalWizardry.ritualDisabledFeatheredEarth = config
                .get("Ritual Blacklist", "Ritual of the Feathered Earth", false).getBoolean(false);
        AlchemicalWizardry.ritualDisabledGaia = config.get("Ritual Blacklist", "Ritual of Gaia's Transformation", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledCondor = config.get("Ritual Blacklist", "Reverence of the Condor", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledFallingTower = config
                .get("Ritual Blacklist", "Mark of the Falling Tower", false).getBoolean(false);
        AlchemicalWizardry.ritualDisabledBalladOfAlchemy = config.get("Ritual Blacklist", "Ballad of Alchemy", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledExpulsion = config.get("Ritual Blacklist", "Aura of Expulsion", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledSuppression = config.get("Ritual Blacklist", "Dome of Supression", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledZephyr = config.get("Ritual Blacklist", "Call of the Zephyr", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledHarvest = config.get("Ritual Blacklist", "Reap of the Harvest Moon", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledConduit = config.get("Ritual Blacklist", "Cry of the Eternal Soul", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledEllipsoid = config.get("Ritual Blacklist", "Focus of the Ellipsoid", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledEvaporation = config.get("Ritual Blacklist", "Song of Evaporation", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledSpawnWard = config.get("Ritual Blacklist", "Ward of Sacrosanctity", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledVeilOfEvil = config.get("Ritual Blacklist", "Veil of Evil", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledFullStomach = config
                .get("Ritual Blacklist", "Requiem of the Satiated Stomach", false).getBoolean(false);
        AlchemicalWizardry.ritualDisabledConvocation = config
                .get("Ritual Blacklist", "Convocation of the Damned", false).getBoolean(false);
        AlchemicalWizardry.ritualDisabledSymmetry = config.get("Ritual Blacklist", "Symmetry of the Omega", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledStalling = config.get("Ritual Blacklist", "Duet of the Fused Souls", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledCrafting = config.get("Ritual Blacklist", "Rhythm of the Beating Anvil", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualDisabledPhantomHands = config
                .get("Ritual Blacklist", "Orchestra of the Phantom Hands", false).getBoolean(false);
        AlchemicalWizardry.ritualDisabledSphereIsland = config.get("Ritual Blacklist", "Birth of the Bastion", false)
                .getBoolean(false);

        AlchemicalWizardry.ritualWeakDisabledNight = config.get("Ritual Blacklist.Weak", "Night", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualWeakDisabledResistance = config.get("Ritual Blacklist.Weak", "Resistance", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualWeakDisabledThunderstorm = config.get("Ritual Blacklist.Weak", "Thunderstorm", false)
                .getBoolean(false);
        AlchemicalWizardry.ritualWeakDisabledZombie = config.get("Ritual Blacklist.Weak", "Zombie", false)
                .getBoolean(false);

        AlchemicalWizardry.potionDisableRegen = config.get("Alchemy Potion Blacklist", "Regeneration", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableNightVision = config.get("Alchemy Potion Blacklist", "Night Vision", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableFireResistance = config
                .get("Alchemy Potion Blacklist", "Fire Resistance", false).getBoolean(false);
        AlchemicalWizardry.potionDisableWaterBreathing = config
                .get("Alchemy Potion Blacklist", "Water Breathing", false).getBoolean(false);
        AlchemicalWizardry.potionDisableMoveSpeed = config.get("Alchemy Potion Blacklist", "Move Speed", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableInstantHealth = config.get("Alchemy Potion Blacklist", "Instant Health", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisablePoison = config.get("Alchemy Potion Blacklist", "Poison", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableBlindness = config.get("Alchemy Potion Blacklist", "Blindness", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableWeakness = config.get("Alchemy Potion Blacklist", "Weakness", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableStrength = config.get("Alchemy Potion Blacklist", "Strength", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableJumpBoost = config.get("Alchemy Potion Blacklist", "Jump Boost", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableSlowness = config.get("Alchemy Potion Blacklist", "Slowness", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableMining = config.get("Alchemy Potion Blacklist", "Mining Speed", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableDrowning = config.get("Alchemy Potion Blacklist", "Drowning", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableInvisibility = config.get("Alchemy Potion Blacklist", "Invisibility", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableResistance = config.get("Alchemy Potion Blacklist", "Resistance", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableSaturation = config.get("Alchemy Potion Blacklist", "Saturation", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableHealthBoost = config.get("Alchemy Potion Blacklist", "Health Boost", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableAbsorption = config.get("Alchemy Potion Blacklist", "Absorption", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableBoost = config.get("Alchemy Potion Blacklist", "Boost", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableFlight = config.get("Alchemy Potion Blacklist", "Flight", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableReciprocation = config.get("Alchemy Potion Blacklist", "Reciprocation", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisablePlanarBinding = config.get("Alchemy Potion Blacklist", "Planar Binding", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableSoulFray = config.get("Alchemy Potion Blacklist", "Soul Fray", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableSoulHarden = config.get("Alchemy Potion Blacklist", "Soul Harden", false)
                .getBoolean(false);
        AlchemicalWizardry.potionDisableDeafness = config.get("Alchemy Potion Blacklist", "Deafness", false)
                .getBoolean(false);

        teleposerBlacklist = config.get(
                "Teleposer Blacklist",
                "Blacklist",
                blacklist,
                "Stops specified blocks from being teleposed. Put entries on new lines. Valid syntax is: \nmodid:blockname:meta")
                .getStringList();
        buildTeleposerBlacklist();

        String tempDemonConfigs = "Demon Configs";
        TEDemonPortal.buildingGridDelay = config.get(tempDemonConfigs, "Building Grid Delay", 25).getInt();
        TEDemonPortal.roadGridDelay = config.get(tempDemonConfigs, "Road Grid Delay", 10).getInt();
        TEDemonPortal.demonHoardDelay = config.get(tempDemonConfigs, "Demon Hoard Delay", 40).getInt();
        TEDemonPortal.demonRoadChance = (float) (config.get(tempDemonConfigs, "Demon Road Chance", 0.3f).getDouble());
        TEDemonPortal.demonHouseChance = (float) (config.get(tempDemonConfigs, "Demon House Chance", 0.6f).getDouble());
        TEDemonPortal.demonPortalChance = (float) (config.get(tempDemonConfigs, "Demon Portal Chance", 0.5f)
                .getDouble());
        TEDemonPortal.demonHoardChance = (float) (config.get(tempDemonConfigs, "Demon Hoard Chance", 0.8f).getDouble());
        TEDemonPortal.portalTickRate = (float) (config.get(tempDemonConfigs, "Portal Tick Rate", 0.1f).getDouble());

        DemonVillagePath.canGoDown = config.get(tempDemonConfigs, "canRoadGoDown", true).getBoolean();
        DemonVillagePath.tunnelIfObstructed = config.get(tempDemonConfigs, "tunnelIfObstructed", false).getBoolean();
        DemonVillagePath.createBridgeInAirIfObstructed = config
                .get(tempDemonConfigs, "createBridgeInAirIfObstructed", false).getBoolean();

        TEDemonPortal.limit = config.get(tempDemonConfigs, "demonGridSpaceLimit", 100).getInt();
        TEDemonPortal.demonLimit = config.get(tempDemonConfigs, "demonHoardLimit", 100).getInt();

        AlchemicalWizardry.isDemonRitualCreativeOnly = config.get(tempDemonConfigs, "IsDemonRitualCreativeOnly", false)
                .getBoolean();
        AlchemicalWizardry.demonRitualDimensionBlacklist = config
                .get(tempDemonConfigs, "dimension blacklist for the demon portal activation", new int[] { 0 })
                .getIntList();
        BoundArmour.tryComplexRendering = config.get("WimpySettings", "UseFancyBoundArmour", true).getBoolean(true);

        // ItemIncense.itemDuration = config.get("TestIncenseSettings", "ItemDuration", 100).getInt();
        // ItemIncense.minValue = config.get("TestIncenseSettings", "MinValue", 0).getInt();
        // ItemIncense.maxValue = config.get("TestIncenseSettings", "MaxValue", 100).getInt();
        // PlayerSacrificeHandler.scalingOfSacrifice = (float) config.get("TestIncenseSettings", "ScalingFactor",
        // 0.0025f).getDouble();
        // PlayerSacrificeHandler.soulFrayDuration = config.get("TestIncenseSettings", "SoulFrayDuration",
        // 400).getInt();

        String lpCosts = "Lp Costs";
        AlchemicalWizardry.sigilAirCost = config.get(lpCosts, "Air Sigil", 50).getInt();
        AlchemicalWizardry.sigilBloodLightCost = config.get(lpCosts, "Sigil of the Blood Lamp", 10).getInt();
        AlchemicalWizardry.sigilHarvestCost = config.get(lpCosts, "Harvest Goddess Sigil", 500).getInt();
        AlchemicalWizardry.sigilLavaCost = config.get(lpCosts, "Lava Sigil", 1000).getInt();
        AlchemicalWizardry.sigilElementalAffinityCost = config.get(lpCosts, "Sigil of Elemental Affinity", 200)
                .getInt();
        AlchemicalWizardry.sigilEnderSeveranceCost = config.get(lpCosts, "Sigil of Ender Severance", 200).getInt();
        AlchemicalWizardry.sigilGrowthCost = config.get(lpCosts, "Sigil of the Green Grove", 150).getInt();
        AlchemicalWizardry.sigilHasteCost = config.get(lpCosts, "Sigil of Haste", 250).getInt();
        AlchemicalWizardry.sigilMagnetismCost = config.get(lpCosts, "Sigil of Magnetism", 50).getInt();
        AlchemicalWizardry.sigilSuppressionCost = config.get(lpCosts, "Sigil of Suppression", 400).getInt();
        AlchemicalWizardry.sigilBridgeCost = config.get(lpCosts, "Sigil of the Phantom Bridge", 100).getInt();
        AlchemicalWizardry.sigilFastMinerCost = config.get(lpCosts, "Sigil of the Fast Miner", 100).getInt();
        AlchemicalWizardry.sigilWhirlwindCost = config.get(lpCosts, "Sigil of the Whirlwind", 250).getInt();
        AlchemicalWizardry.sigilCompressCost = config.get(lpCosts, "Sigil of Compression", 200).getInt();
        AlchemicalWizardry.sigilVoidCost = config.get(lpCosts, "Void Sigil", 50).getInt();
        AlchemicalWizardry.sigilWaterCost = config.get(lpCosts, "Water Sigil", 100).getInt();

        config.addCustomCategoryComment(
                lpCosts,
                "For rituals, the first number indicates the activation cost while the second indicates the refresh cost");
        AlchemicalWizardry.ritualCostWater = config.get(lpCosts, "Ritual of the Full Spring", new int[] { 500, 25 })
                .getIntList();
        AlchemicalWizardry.ritualCostLava = config.get(lpCosts, "Serenade of the Nether", new int[] { 10000, 500 })
                .getIntList();
        AlchemicalWizardry.ritualCostGreenGrove = config
                .get(lpCosts, "Ritual of the Green Grove", new int[] { 1000, 20 }).getIntList();
        AlchemicalWizardry.ritualCostInterdiction = config.get(lpCosts, "Interdiction Ritual", new int[] { 1000, 1 })
                .getIntList();
        AlchemicalWizardry.ritualCostContainment = config.get(lpCosts, "Ritual of Containment", new int[] { 2000, 1 })
                .getIntList();
        AlchemicalWizardry.ritualCostBinding = config.get(lpCosts, "Ritual of Binding", new int[] { 5000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostUnbinding = config.get(lpCosts, "Ritual of Unbinding", new int[] { 30000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostHighJump = config.get(lpCosts, "Ritual of the High Jump", new int[] { 1000, 5 })
                .getIntList();
        AlchemicalWizardry.ritualCostMagnetism = config.get(lpCosts, "Ritual of Magnetism", new int[] { 5000, 50 })
                .getIntList();
        AlchemicalWizardry.ritualCostCrusher = config.get(lpCosts, "Ritual of the Crusher", new int[] { 2500, 7 })
                .getIntList();
        AlchemicalWizardry.ritualCostSpeed = config.get(lpCosts, "Ritual of Speed", new int[] { 1000, 5 }).getIntList();
        AlchemicalWizardry.ritualCostAnimalGrowth = config
                .get(lpCosts, "Ritual of the Shepherd", new int[] { 10000, 2 }).getIntList();
        AlchemicalWizardry.ritualCostSuffering = config.get(lpCosts, "Well of Suffering", new int[] { 50000, 2 })
                .getIntList();
        AlchemicalWizardry.ritualCostRegen = config.get(lpCosts, "Ritual of Regeneration", new int[] { 25000, 20 })
                .getIntList();
        AlchemicalWizardry.ritualCostFeatheredKnife = config
                .get(lpCosts, "Ritual of the Feathered Knife", new int[] { 50000, 20 }).getIntList();
        AlchemicalWizardry.ritualCostFeatheredEarth = config
                .get(lpCosts, "Ritual of the Feathered Earth", new int[] { 100000, 0 }).getIntList();
        AlchemicalWizardry.ritualCostGaia = config.get(lpCosts, "Gaia's Transformation", new int[] { 1000000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostCondor = config.get(lpCosts, "Reverence of the Condor", new int[] { 1000000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostFallingTower = config
                .get(lpCosts, "Mark of the Falling Tower", new int[] { 100000, 0 }).getIntList();
        AlchemicalWizardry.ritualCostBalladOfAlchemy = config.get(lpCosts, "Ballad of Alchemy", new int[] { 20000, 10 })
                .getIntList();
        AlchemicalWizardry.ritualCostExpulsion = config.get(lpCosts, "Aura of Expulsion", new int[] { 1000000, 1000 })
                .getIntList();
        AlchemicalWizardry.ritualCostSuppression = config.get(lpCosts, "Dome of Suppression", new int[] { 10000, 2 })
                .getIntList();
        AlchemicalWizardry.ritualCostZephyr = config.get(lpCosts, "Call of the Zephyr", new int[] { 25000, 5 })
                .getIntList();
        AlchemicalWizardry.ritualCostHarvest = config.get(lpCosts, "Reap of the Harvest Moon", new int[] { 20000, 20 })
                .getIntList();
        AlchemicalWizardry.ritualCostConduit = config.get(lpCosts, "Cry of the Eternal Soul", new int[] { 2000000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostEllipsoid = config.get(lpCosts, "Focus of the Ellipsoid", new int[] { 25000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostEvaporation = config.get(lpCosts, "Song of Evaporation", new int[] { 20000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostSpawnWard = config.get(lpCosts, "Ward of Sacrosanctity", new int[] { 150000, 15 })
                .getIntList();
        AlchemicalWizardry.ritualCostVeilOfEvil = config.get(lpCosts, "Veil of Evil", new int[] { 150000, 20 })
                .getIntList();
        AlchemicalWizardry.ritualCostFullStomach = config
                .get(lpCosts, "Requiem of the Satiated Stomach", new int[] { 100000, 100 }).getIntList();
        AlchemicalWizardry.ritualCostConvocation = config
                .get(lpCosts, "Convocation of the Damned", new int[] { 15000000, 0 }).getIntList();
        AlchemicalWizardry.ritualCostSymmetry = config.get(lpCosts, "Symmetry of the Omega", new int[] { 15000000, 0 })
                .getIntList();
        AlchemicalWizardry.ritualCostStalling = config
                .get(lpCosts, "Duet of the Fused Souls", new int[] { 15000000, 5000 }).getIntList();
        AlchemicalWizardry.ritualCostCrafting = config
                .get(lpCosts, "Rhythm of the Beating Anvil", new int[] { 15000, 10 }).getIntList();
        AlchemicalWizardry.ritualCostPhantomHands = config
                .get(lpCosts, "Orchestra of the Phantom Hands", new int[] { 10000, 0 }).getIntList();
        AlchemicalWizardry.ritualCostSphereIsland = config.get(lpCosts, "Blood of the New Moon", new int[] { 10000, 0 })
                .getIntList();

        AlchemicalWizardry.ritualWeakCostNight = config.get(lpCosts, "[Weak Ritual] Night", 5000).getInt();
        AlchemicalWizardry.ritualWeakCostResistance = config.get(lpCosts, "[Weak Ritual] Resistance", 5000).getInt();
        AlchemicalWizardry.ritualWeakCostThunderstorm = config.get(lpCosts, "[Weak Ritual] Thunderstorm", 5000)
                .getInt();
        AlchemicalWizardry.ritualWeakCostZombie = config.get(lpCosts, "[Weak Ritual] Zombie", 5000).getInt();

        AlchemicalWizardry.lpPerSelfSacrifice = config
                .get("sacrifice", "LP per self-sacrifice", AlchemicalWizardry.lpPerSelfSacrifice)
                .getInt(AlchemicalWizardry.lpPerSelfSacrifice);
        AlchemicalWizardry.lpPerSelfSacrificeSoulFray = config
                .get(
                        "sacrifice",
                        "LP per self-sacrifice (when Soul Fray potion is active)",
                        AlchemicalWizardry.lpPerSelfSacrificeSoulFray)
                .getInt(AlchemicalWizardry.lpPerSelfSacrificeSoulFray);
        AlchemicalWizardry.lpPerSelfSacrificeFeatheredKnife = config
                .get(
                        "sacrifice",
                        "LP per self-sacrifice with Ritual of Feathered Knife",
                        AlchemicalWizardry.lpPerSelfSacrificeFeatheredKnife)
                .getInt(AlchemicalWizardry.lpPerSelfSacrificeFeatheredKnife);
        AlchemicalWizardry.lpPerSacrificeBase = config
                .get("sacrifice", "LP per sacrifice", AlchemicalWizardry.lpPerSacrificeBase)
                .getInt(AlchemicalWizardry.lpPerSacrificeBase);
        AlchemicalWizardry.lpPerSacrificeWellOfSuffering = config
                .get(
                        "sacrifice",
                        "LP per sacrifice with Well of Suffering ritual",
                        AlchemicalWizardry.lpPerSacrificeWellOfSuffering)
                .getInt(AlchemicalWizardry.lpPerSacrificeWellOfSuffering);
        AlchemicalWizardry.lpPerSacrificeIncense = config
                .get("sacrifice", "LP per (self-)sacrifice with incense", AlchemicalWizardry.lpPerSacrificeIncense)
                .getDouble(AlchemicalWizardry.lpPerSacrificeIncense);

        AlchemicalWizardry.energyBlastDamage = config
                .get("energy items", "Energy Blast damage", AlchemicalWizardry.energyBlastDamage)
                .getInt(AlchemicalWizardry.energyBlastDamage);
        AlchemicalWizardry.energyBlastLPPerShot = config
                .get("energy items", "Energy Blast LP per shot", AlchemicalWizardry.energyBlastLPPerShot)
                .getInt(AlchemicalWizardry.energyBlastLPPerShot);
        AlchemicalWizardry.energyBlastMaxDelay = config
                .get("energy items", "Energy Blast maximum delay", AlchemicalWizardry.energyBlastMaxDelay)
                .getInt(AlchemicalWizardry.energyBlastMaxDelay);
        AlchemicalWizardry.energyBlastMaxDelayAfterActivation = config
                .get(
                        "energy items",
                        "Energy Blast maximum delay after (re-)activation",
                        AlchemicalWizardry.energyBlastMaxDelayAfterActivation)
                .getInt(AlchemicalWizardry.energyBlastMaxDelayAfterActivation);
        AlchemicalWizardry.energyBlastLPPerActivation = config
                .get("energy items", "Energy Blast LP per activation", AlchemicalWizardry.energyBlastLPPerActivation)
                .getInt(AlchemicalWizardry.energyBlastLPPerActivation);

        AlchemicalWizardry.energyBlastSecondTierDamage = config
                .get("energy items", "Energy Blast Tier II damage", AlchemicalWizardry.energyBlastSecondTierDamage)
                .getInt(AlchemicalWizardry.energyBlastSecondTierDamage);
        AlchemicalWizardry.energyBlastSecondTierLPPerShot = config
                .get(
                        "energy items",
                        "Energy Blast Tier II LP per shot",
                        AlchemicalWizardry.energyBlastSecondTierLPPerShot)
                .getInt(AlchemicalWizardry.energyBlastSecondTierLPPerShot);
        AlchemicalWizardry.energyBlastSecondTierMaxDelay = config
                .get(
                        "energy items",
                        "Energy Blast Tier II maximum delay",
                        AlchemicalWizardry.energyBlastSecondTierMaxDelay)
                .getInt(AlchemicalWizardry.energyBlastSecondTierMaxDelay);
        AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation = config
                .get(
                        "energy items",
                        "Energy Blast Tier II maximum delay after (re-)activation",
                        AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation)
                .getInt(AlchemicalWizardry.energyBlastSecondTierMaxDelayAfterActivation);
        AlchemicalWizardry.energyBlastSecondTierLPPerActivation = config
                .get(
                        "energy items",
                        "Energy Blast Tier II LP per activation",
                        AlchemicalWizardry.energyBlastSecondTierLPPerActivation)
                .getInt(AlchemicalWizardry.energyBlastSecondTierLPPerActivation);

        AlchemicalWizardry.energyBlastThirdTierDamage = config
                .get("energy items", "Energy Blast Tier III damage", AlchemicalWizardry.energyBlastThirdTierDamage)
                .getInt(AlchemicalWizardry.energyBlastThirdTierDamage);
        AlchemicalWizardry.energyBlastThirdTierLPPerShot = config
                .get(
                        "energy items",
                        "Energy Blast Tier III LP per shot",
                        AlchemicalWizardry.energyBlastThirdTierLPPerShot)
                .getInt(AlchemicalWizardry.energyBlastThirdTierLPPerShot);
        AlchemicalWizardry.energyBlastThirdTierMaxDelay = config
                .get(
                        "energy items",
                        "Energy Blast Tier III maximum delay",
                        AlchemicalWizardry.energyBlastThirdTierMaxDelay)
                .getInt(AlchemicalWizardry.energyBlastThirdTierMaxDelay);
        AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation = config
                .get(
                        "energy items",
                        "Energy Blast Tier III maximum delay after (re-)activation",
                        AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation)
                .getInt(AlchemicalWizardry.energyBlastThirdTierMaxDelayAfterActivation);
        AlchemicalWizardry.energyBlastThirdTierLPPerActivation = config
                .get(
                        "energy items",
                        "Energy Blast Tier III LP per activation",
                        AlchemicalWizardry.energyBlastThirdTierLPPerActivation)
                .getInt(AlchemicalWizardry.energyBlastThirdTierLPPerActivation);

        AlchemicalWizardry.energyBazookaDamage = config
                .get("energy items", "Energy Bazooka damage", AlchemicalWizardry.energyBazookaDamage)
                .getInt(AlchemicalWizardry.energyBazookaDamage);
        AlchemicalWizardry.energyBazookaSecondaryDamage = config
                .get("energy items", "Energy Bazooka secondary damage", AlchemicalWizardry.energyBazookaSecondaryDamage)
                .getInt(AlchemicalWizardry.energyBazookaSecondaryDamage);
        AlchemicalWizardry.energyBazookaLPPerShot = config
                .get("energy items", "Energy Bazooka LP per shot", AlchemicalWizardry.energyBazookaLPPerShot)
                .getInt(AlchemicalWizardry.energyBazookaLPPerShot);
        AlchemicalWizardry.energyBazookaMaxDelay = config
                .get("energy items", "Energy Bazooka maximum delay", AlchemicalWizardry.energyBazookaMaxDelay)
                .getInt(AlchemicalWizardry.energyBazookaMaxDelay);
        AlchemicalWizardry.energyBazookaMaxDelayAfterActivation = config
                .get(
                        "energy items",
                        "Energy Bazooka maximum delay after (re-)activation",
                        AlchemicalWizardry.energyBazookaMaxDelayAfterActivation)
                .getInt(AlchemicalWizardry.energyBazookaMaxDelayAfterActivation);
        AlchemicalWizardry.energyBazookaLPPerActivation = config
                .get(
                        "energy items",
                        "Energy Bazooka LP per activation",
                        AlchemicalWizardry.energyBazookaLPPerActivation)
                .getInt(AlchemicalWizardry.energyBazookaLPPerActivation);

        AlchemicalWizardry.energyBazookaSecondTierDamage = config
                .get("energy items", "Energy Bazooka Tier II damage", AlchemicalWizardry.energyBazookaSecondTierDamage)
                .getInt(AlchemicalWizardry.energyBazookaSecondTierDamage);
        AlchemicalWizardry.energyBazookaSecondTierSecondaryDamage = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier II secondary damage",
                        AlchemicalWizardry.energyBazookaSecondTierSecondaryDamage)
                .getInt(AlchemicalWizardry.energyBazookaSecondTierSecondaryDamage);
        AlchemicalWizardry.energyBazookaSecondTierLPPerShot = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier II LP per shot",
                        AlchemicalWizardry.energyBazookaSecondTierLPPerShot)
                .getInt(AlchemicalWizardry.energyBazookaSecondTierLPPerShot);
        AlchemicalWizardry.energyBazookaSecondTierMaxDelay = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier II maximum delay",
                        AlchemicalWizardry.energyBazookaSecondTierMaxDelay)
                .getInt(AlchemicalWizardry.energyBazookaSecondTierMaxDelay);
        AlchemicalWizardry.energyBazookaSecondTierMaxDelayAfterActivation = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier II maximum delay after (re-)activation",
                        AlchemicalWizardry.energyBazookaSecondTierMaxDelayAfterActivation)
                .getInt(AlchemicalWizardry.energyBazookaSecondTierMaxDelayAfterActivation);
        AlchemicalWizardry.energyBazookaSecondTierLPPerActivation = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier II LP per activation",
                        AlchemicalWizardry.energyBazookaSecondTierLPPerActivation)
                .getInt(AlchemicalWizardry.energyBazookaSecondTierLPPerActivation);

        AlchemicalWizardry.energyBazookaThirdTierDamage = config
                .get("energy items", "Energy Bazooka Tier III damage", AlchemicalWizardry.energyBazookaThirdTierDamage)
                .getInt(AlchemicalWizardry.energyBazookaThirdTierDamage);
        AlchemicalWizardry.energyBazookaThirdTierSecondaryDamage = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier III secondary damage",
                        AlchemicalWizardry.energyBazookaThirdTierSecondaryDamage)
                .getInt(AlchemicalWizardry.energyBazookaThirdTierSecondaryDamage);
        AlchemicalWizardry.energyBazookaThirdTierLPPerShot = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier III LP per shot",
                        AlchemicalWizardry.energyBazookaThirdTierLPPerShot)
                .getInt(AlchemicalWizardry.energyBazookaThirdTierLPPerShot);
        AlchemicalWizardry.energyBazookaThirdTierMaxDelay = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier III maximum delay",
                        AlchemicalWizardry.energyBazookaThirdTierMaxDelay)
                .getInt(AlchemicalWizardry.energyBazookaThirdTierMaxDelay);
        AlchemicalWizardry.energyBazookaThirdTierMaxDelayAfterActivation = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier III maximum delay after (re-)activation",
                        AlchemicalWizardry.energyBazookaThirdTierMaxDelayAfterActivation)
                .getInt(AlchemicalWizardry.energyBazookaThirdTierMaxDelayAfterActivation);
        AlchemicalWizardry.energyBazookaThirdTierLPPerActivation = config
                .get(
                        "energy items",
                        "Energy Bazooka Tier III LP per activation",
                        AlchemicalWizardry.energyBazookaThirdTierLPPerActivation)
                .getInt(AlchemicalWizardry.energyBazookaThirdTierLPPerActivation);

        Side side = FMLCommonHandler.instance().getSide();
        if (side == Side.CLIENT) {
            RenderHelper.xOffset = config.get("ClientSettings", "AlchemyHUDxOffset", 50).getInt();
            RenderHelper.yOffset = config.get("ClientSettings", "AlchemyHUDyOffset", 2).getInt();
            RenderHelper.lpBarX = config.get("ClientSettings", "LPHUDxOffset", 12).getInt();
            RenderHelper.lpBarY = config.get("ClientSettings", "LPHUDyOffset", 75).getInt();
            AlchemicalWizardry.displayRitualAnimation = config.get("ClientSettings", "Display Ritual Animation", true)
                    .getBoolean(true);
        }

        config.save();
    }

    public static void finishLoading() {
        String[] defaultRunes = { "AWWayofTime:AlchemicalWizardrybloodRune", "AWWayofTime:speedRune",
                "AWWayofTime:efficiencyRune", "AWWayofTime:runeOfSacrifice", "AWWayofTime:runeOfSelfSacrifice",
                "AWWayofTime:AlchemicalWizardrybloodRune:1", "AWWayofTime:AlchemicalWizardrybloodRune:2",
                "AWWayofTime:AlchemicalWizardrybloodRune:3", "AWWayofTime:AlchemicalWizardrybloodRune:4",
                "AWWayofTime:AlchemicalWizardrybloodRune:5", "AWWayofTime:AlchemicalWizardrybloodRune:6" };

        AlchemicalWizardry.secondTierRunes = readRuneOverrides("secondTier", defaultRunes);
        AlchemicalWizardry.thirdTierRunes = readRuneOverrides("thirdTier", defaultRunes);
        AlchemicalWizardry.fourthTierRunes = readRuneOverrides("fourthTier", defaultRunes);
        AlchemicalWizardry.fifthTierRunes = readRuneOverrides("fifthTier", defaultRunes);
        AlchemicalWizardry.sixthTierRunes = readRuneOverrides("sixthTier", defaultRunes);

        AlchemicalWizardry.fifthTierBeacons = readBlockConfig(
                "altar blocks",
                "fifthTierBeacons",
                new String[] { "minecraft:beacon", "etfuturum:beacon", "chisel:beacon:*" },
                "Valid blocks for the Blood Altar's tier 5 beacons:\n"
                        + "Use the following format for all of these entries: mod:block(:meta). Meta * or 32767 allows for any meta.\n"
                        + "An empty entry allows for any non-air block to be used for that part of the structure.\n"
                        + "Invalid or missing blocks are skipped. If all entries are invalid, any block can be used as a fallback.");
        AlchemicalWizardry.thirdTierCaps = readBlockConfig(
                "altar blocks",
                "thirdTierCaps",
                new String[] { "minecraft:glowstone", "BloodArsenal:blood_infused_glowstone", "Botania:seaLamp",
                        "chisel:glowstone:*", "etfuturum:sea_lantern", "etfuturum:shroomlight",
                        "ExtraUtilities:color_lightgem:*" },
                "Valid blocks for the Blood Altar's tier 3 caps:");
        AlchemicalWizardry.thirdTierPillars = readBlockConfig(
                "altar blocks",
                "thirdTierPillars",
                new String[] {},
                "Valid blocks for the Blood Altar's tier 3 pillars:");
        AlchemicalWizardry.fourthTierCaps = readBlockConfig(
                "altar blocks",
                "fourthTierCaps",
                new String[] { "AWWayofTime:largeBloodStoneBrick" },
                "Valid blocks for the Blood Altar's tier 4 caps:");
        AlchemicalWizardry.fourthTierPillars = readBlockConfig(
                "altar blocks",
                "fourthTierPillars",
                new String[] {},
                "Valid blocks for the Blood Altar's tier 4 pillars.");
        AlchemicalWizardry.sixthTierCaps = readBlockConfig(
                "altar blocks",
                "sixthTierCaps",
                new String[] { "AWWayofTime:blockCrystal" },
                "Valid blocks for the Blood Altar's tier 6 caps:");
        AlchemicalWizardry.sixthTierPillars = readBlockConfig(
                "altar blocks",
                "sixthTierPillars",
                new String[] {},
                "Valid blocks for the Blood Altar's tier 6 pillars:");
        config.save();
    }

    public static void loadCustomLPValues() {
        AlchemicalWizardry.lpPerSactificeCustom = new HashMap<>();
        for (Object object : EntityList.stringToClassMapping.entrySet()) {
            Entry entry = (Entry) object;
            String entityName = (String) entry.getKey();
            Class entityClass = (Class) entry.getValue();
            if (EntityLivingBase.class.isAssignableFrom(entityClass)
                    && !Modifier.isAbstract(entityClass.getModifiers())) {
                int lpAmount = 500;
                if (EntityVillager.class.isAssignableFrom(entityClass)) {
                    lpAmount = 2000;
                }
                if (EntitySlime.class.isAssignableFrom(entityClass)) {
                    lpAmount = 150;
                }
                if (EntityEnderman.class.isAssignableFrom(entityClass)) {
                    lpAmount = 200;
                }
                if (EntityAnimal.class.isAssignableFrom(entityClass)) {
                    lpAmount = 250;
                }
                lpAmount = config.get("sacrifice.custom values", entityName, lpAmount).getInt(lpAmount);
                AlchemicalWizardry.lpPerSactificeCustom.put(entityClass, lpAmount);
            }
        }
        config.save();
    }

    public static void set(String categoryName, String propertyName, String newValue) {
        config.load();
        if (config.getCategoryNames().contains(categoryName)) {
            if (config.getCategory(categoryName).containsKey(propertyName)) {
                config.getCategory(categoryName).get(propertyName).set(newValue);
            }
        }
        config.save();
    }

    public static void loadBlacklist() {
        AlchemicalWizardry.wellBlacklist = new ArrayList<Class>();
        for (Object o : stringToClassMapping.entrySet()) {
            Entry entry = (Entry) o;
            Class curClass = (Class) entry.getValue();
            boolean valid = EntityLivingBase.class.isAssignableFrom(curClass)
                    && !Modifier.isAbstract(curClass.getModifiers());
            if (valid) {
                boolean blacklisted = config.get("wellOfSufferingBlackList", entry.getKey().toString(), false)
                        .getBoolean();
                if (blacklisted) AlchemicalWizardry.wellBlacklist.add(curClass);
            }
        }
        config.save();
    }

    public static void blacklistRituals() {
        if (AlchemicalWizardry.ritualDisabledWater) r("AW001Water");
        if (AlchemicalWizardry.ritualDisabledLava) r("AW002Lava");
        if (AlchemicalWizardry.ritualDisabledGreenGrove) r("AW003GreenGrove");
        if (AlchemicalWizardry.ritualDisabledInterdiction) r("AW004Interdiction");
        if (AlchemicalWizardry.ritualDisabledContainment) r("AW005Containment");
        if (AlchemicalWizardry.ritualDisabledBinding) r("AW006Binding");
        if (AlchemicalWizardry.ritualDisabledUnbinding) r("AW007Unbinding"); // "A medium dry martini, lemon peel.
                                                                             // Shaken, not stirred."
        if (AlchemicalWizardry.ritualDisabledHighJump) r("AW008HighJump");
        if (AlchemicalWizardry.ritualDisabledMagnetism) r("AW009Magnetism");
        if (AlchemicalWizardry.ritualDisabledCrusher) r("AW010Crusher");
        if (AlchemicalWizardry.ritualDisabledSpeed) r("AW011Speed");
        if (AlchemicalWizardry.ritualDisabledAnimalGrowth) r("AW012AnimalGrowth");
        if (AlchemicalWizardry.ritualDisabledSuffering) r("AW013Suffering");
        if (AlchemicalWizardry.ritualDisabledRegen) r("AW014Regen");
        if (AlchemicalWizardry.ritualDisabledFeatheredKnife) r("AW015FeatheredKnife");
        if (AlchemicalWizardry.ritualDisabledFeatheredEarth) r("AW016FeatheredEarth");
        if (AlchemicalWizardry.ritualDisabledGaia) r("AW017Gaia");
        if (AlchemicalWizardry.ritualDisabledCondor) r("AW018Condor");
        if (AlchemicalWizardry.ritualDisabledFallingTower) r("AW019FallingTower");
        if (AlchemicalWizardry.ritualDisabledBalladOfAlchemy) r("AW020BalladOfAlchemy");
        if (AlchemicalWizardry.ritualDisabledExpulsion) r("AW021Expulsion");
        if (AlchemicalWizardry.ritualDisabledSuppression) r("AW022Suppression");
        if (AlchemicalWizardry.ritualDisabledZephyr) r("AW023Zephyr");
        if (AlchemicalWizardry.ritualDisabledHarvest) r("AW024Harvest");
        if (AlchemicalWizardry.ritualDisabledConduit) r("AW025Conduit");
        if (AlchemicalWizardry.ritualDisabledEllipsoid) r("AW026Ellipsoid");
        if (AlchemicalWizardry.ritualDisabledEvaporation) r("AW027Evaporation");
        if (AlchemicalWizardry.ritualDisabledSpawnWard) r("AW028SpawnWard");
        if (AlchemicalWizardry.ritualDisabledVeilOfEvil) r("AW029VeilOfEvil");
        if (AlchemicalWizardry.ritualDisabledFullStomach) r("AW030FullStomach");
        if (AlchemicalWizardry.ritualDisabledConvocation) r("AW031Convocation");
        if (AlchemicalWizardry.ritualDisabledSymmetry) r("AW032Symmetry");
        if (AlchemicalWizardry.ritualDisabledStalling) r("AW033Stalling");
        if (AlchemicalWizardry.ritualDisabledCrafting) r("AW034Crafting");
        if (AlchemicalWizardry.ritualDisabledPhantomHands) r("AW035PhantomHands");
        if (AlchemicalWizardry.ritualDisabledSphereIsland) r("AW036SphereIsland");
    }

    private static void r(String ritualID) {
        Rituals.ritualMap.remove(ritualID);
        Rituals.keyList.remove(ritualID);
    }

    private static void buildTeleposerBlacklist() {

        // Make sure it's empty before setting the blacklist.
        // Otherwise, reloading the config while in-game will duplicate the list.
        AlchemicalWizardryEventHooks.teleposerBlacklist.clear();

        for (String blockSet : BloodMagicConfiguration.teleposerBlacklist) {
            String[] blockData = blockSet.split(":");

            Block block = GameRegistry.findBlock(blockData[0], blockData[1]);
            int meta = 0;

            // If the block follows full syntax: modid:blockname:meta
            if (blockData.length == 3) {
                // Check if it's an int, if so, parse it. If not, set meta to 0 to avoid crashing.
                if (isInteger(blockData[2])) meta = Integer.parseInt(blockData[2]);
                else if (blockData[2].equals("*")) meta = OreDictionary.WILDCARD_VALUE;
            }

            AlchemicalWizardryEventHooks.teleposerBlacklist.add(new BlockStack(block, meta));
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private static List<BlockStack> readRuneOverrides(String key, String[] defaultValues) {
        return readBlockConfig("rune overrides", key, defaultValues, "");
    }

    private static List<BlockStack> readBlockConfig(String category, String key, String[] defaultValues,
            String comment) {
        Property property = config.get(category, key, defaultValues, comment);
        List<BlockStack> validStacks = new ArrayList<>();

        for (String name : property.getStringList()) {
            String[] blockData = name.split(":");
            if (blockData.length < 2 || blockData.length > 3) {
                continue;
            }

            Block block = GameRegistry.findBlock(blockData[0], blockData[1]);
            if (block == null) {
                AlchemicalWizardry.logger.info("Block not found for {} {}: {}", category, key, name);
                continue;
            }

            int meta = 0;
            if (blockData.length == 3) {
                if (isInteger(blockData[2])) meta = Integer.parseInt(blockData[2]);
                else if (blockData[2].equals("*")) meta = OreDictionary.WILDCARD_VALUE;
            }

            validStacks.add(new BlockStack(block, meta));
        }

        return validStacks;
    }

}
