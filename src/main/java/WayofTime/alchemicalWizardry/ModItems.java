package WayofTime.alchemicalWizardry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.items.AWBaseItems;
import WayofTime.alchemicalWizardry.common.items.ActivationCrystal;
import WayofTime.alchemicalWizardry.common.items.ApprenticeBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ArchmageBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ArmourInhibitor;
import WayofTime.alchemicalWizardry.common.items.BlankSpell;
import WayofTime.alchemicalWizardry.common.items.BloodShard;
import WayofTime.alchemicalWizardry.common.items.BoundAxe;
import WayofTime.alchemicalWizardry.common.items.BoundPickaxe;
import WayofTime.alchemicalWizardry.common.items.BoundShovel;
import WayofTime.alchemicalWizardry.common.items.CheatyItem;
import WayofTime.alchemicalWizardry.common.items.CreativeDagger;
import WayofTime.alchemicalWizardry.common.items.DaggerOfSacrifice;
import WayofTime.alchemicalWizardry.common.items.DemonPlacer;
import WayofTime.alchemicalWizardry.common.items.DemonicTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyBazooka;
import WayofTime.alchemicalWizardry.common.items.EnergyBlast;
import WayofTime.alchemicalWizardry.common.items.EnergySword;
import WayofTime.alchemicalWizardry.common.items.EnhancedTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.ItemAlchemyBase;
import WayofTime.alchemicalWizardry.common.items.ItemBloodLetterPack;
import WayofTime.alchemicalWizardry.common.items.ItemComplexSpellCrystal;
import WayofTime.alchemicalWizardry.common.items.ItemComponents;
import WayofTime.alchemicalWizardry.common.items.ItemDiabloKey;
import WayofTime.alchemicalWizardry.common.items.ItemIncense;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDismantler;
import WayofTime.alchemicalWizardry.common.items.ItemRitualDiviner;
import WayofTime.alchemicalWizardry.common.items.LavaCrystal;
import WayofTime.alchemicalWizardry.common.items.LifeBucket;
import WayofTime.alchemicalWizardry.common.items.MagicianBloodOrb;
import WayofTime.alchemicalWizardry.common.items.MasterBloodOrb;
import WayofTime.alchemicalWizardry.common.items.ReinforcedTelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.SacrificialDagger;
import WayofTime.alchemicalWizardry.common.items.ScribeTool;
import WayofTime.alchemicalWizardry.common.items.TelepositionFocus;
import WayofTime.alchemicalWizardry.common.items.TranscendentBloodOrb;
import WayofTime.alchemicalWizardry.common.items.armour.BoundArmour;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourEarth;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourFire;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourWater;
import WayofTime.alchemicalWizardry.common.items.armour.OmegaArmourWind;
import WayofTime.alchemicalWizardry.common.items.energy.ItemAttunedCrystal;
import WayofTime.alchemicalWizardry.common.items.energy.ItemDestinationClearer;
import WayofTime.alchemicalWizardry.common.items.energy.ItemTankSegmenter;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyFlask;
import WayofTime.alchemicalWizardry.common.items.potion.AlchemyReagent;
import WayofTime.alchemicalWizardry.common.items.potion.AverageLengtheningCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.AveragePowerCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.CombinationalCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.EnhancedFillingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.GreaterLengtheningCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.GreaterPowerCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.MundaneLengtheningCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.MundanePowerCatalyst;
import WayofTime.alchemicalWizardry.common.items.potion.StandardBindingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.StandardFillingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.WeakBindingAgent;
import WayofTime.alchemicalWizardry.common.items.potion.WeakFillingAgent;
import WayofTime.alchemicalWizardry.common.items.routing.InputRoutingFocus;
import WayofTime.alchemicalWizardry.common.items.routing.OutputRoutingFocus;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilAir;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilBloodLight;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilDivination;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilFluid;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilHarvest;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilLava;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfElementalAffinity;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfEnderSeverance;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfGrowth;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfHaste;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfMagnetism;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfSupression;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheAssassin;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheBridge;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfTheFastMiner;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilOfWind;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilPackRat;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilSeer;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilVoid;
import WayofTime.alchemicalWizardry.common.items.sigil.SigilWater;
import WayofTime.alchemicalWizardry.common.items.sigil.holding.SigilOfHolding;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created with IntelliJ IDEA. User: Pokefenn Date: 17/01/14 Time: 19:48
 */
public class ModItems {

    // Orbs
    public static Item weakBloodOrb;
    public static Item apprenticeBloodOrb;
    public static Item magicianBloodOrb;
    public static Item masterBloodOrb;
    public static Item archmageBloodOrb;
    public static Item transcendentBloodOrb;
    public static Item creativeFiller;

    // Bound Tools/Weapons
    public static Item energySword;
    public static Item boundPickaxe;
    public static Item boundAxe;
    public static Item boundShovel;
    public static Item customTool;
    public static Item energyBlaster;
    public static Item energyBlasterSecondTier;
    public static Item energyBlasterThirdTier;
    public static Item energyBazooka;
    public static Item energyBazookaSecondTier;
    public static Item energyBazookaThirdTier;

    // Armor
    public static Item itemBloodPack;

    public static Item sanguineHelmet;
    public static Item sanguineRobe;
    public static Item sanguinePants;
    public static Item sanguineBoots;

    public static Item boundHelmet;
    public static Item boundPlate;
    public static Item boundLeggings;
    public static Item boundBoots;

    public static Item boundHelmetWater;
    public static Item boundPlateWater;
    public static Item boundLeggingsWater;
    public static Item boundBootsWater;

    public static Item boundHelmetEarth;
    public static Item boundPlateEarth;
    public static Item boundLeggingsEarth;
    public static Item boundBootsEarth;

    public static Item boundHelmetWind;
    public static Item boundPlateWind;
    public static Item boundLeggingsWind;
    public static Item boundBootsWind;

    public static Item boundHelmetFire;
    public static Item boundPlateFire;
    public static Item boundLeggingsFire;
    public static Item boundBootsFire;

    // Sigils
    // T1
    public static Item divinationSigil;
    public static Item itemSeerSigil;
    public static Item waterSigil;
    public static Item lavaSigil;
    // T2
    public static Item airSigil;
    public static Item voidSigil;
    public static Item growthSigil;
    public static Item sigilOfTheFastMiner;
    // T3
    public static Item sigilOfElementalAffinity;
    public static Item itemBloodLightSigil;
    public static Item sigilOfTheBridge;
    public static Item sigilOfMagnetism;
    public static Item sigilOfHolding;
    // T4
    public static Item sigilOfWind;
    public static Item itemSigilOfSupression;
    public static Item sigilOfHaste;
    public static Item itemSigilOfEnderSeverance;
    public static Item itemCompressionSigil;
    // T5
    public static Item itemHarvestSigil;
    // Unimplemented
    public static Item itemFluidSigil;
    public static Item itemAssassinSigil;

    // Crafting Materials
    public static Item blankSlate;
    public static Item reinforcedSlate;
    public static Item imbuedSlate;
    public static Item demonicSlate;
    public static Item baseItems;
    public static Item weakBloodShard;
    public static Item demonBloodShard;
    public static Item bucketLife;

    // Alchemy
    // Potions
    public static Item alchemyFlask;
    public static Item simpleCatalyst;
    public static Item weakBindingAgent;
    public static Item standardBindingAgent;
    public static Item mundanePowerCatalyst;
    public static Item averagePowerCatalyst;
    public static Item greaterPowerCatalyst;
    public static Item mundaneLengtheningCatalyst;
    public static Item averageLengtheningCatalyst;
    public static Item greaterLengtheningCatalyst;
    public static Item weakFillingAgent;
    public static Item standardFillingAgent;
    public static Item enhancedFillingAgent;
    // Reagents
    public static Item itemAttunedCrystal;
    public static Item itemTankSegmenter;
    public static Item itemDestinationClearer;
    public static Item incendium;
    public static Item magicales;
    public static Item sanctus;
    public static Item aether;
    public static Item crepitous;
    public static Item crystallos;
    public static Item terrae;
    public static Item aquasalus;
    public static Item tennebrae;
    // Other Alchemy Items
    public static Item baseAlchemyItems;
    public static Item itemIncense;
    // Unused
    public static Item itemCombinationalCatalyst;

    // Utility
    public static Item sacrificialDagger;
    public static Item daggerOfSacrifice;
    public static Item creativeDagger;
    public static Item lavaCrystal;
    public static Item itemKeyOfDiablo;
    public static Item armourInhibitor;
    public static Item telepositionFocus;
    public static Item enhancedTelepositionFocus;
    public static Item reinforcedTelepositionFocus;
    public static Item demonicTelepositionFocus;
    public static Item inputRoutingFocus;
    public static Item outputRoutingFocus;
    public static Item blankSpell;
    public static Item itemComplexSpellCrystal;
    public static Item demonPlacer;

    // Rituals
    public static Item waterScribeTool;
    public static Item fireScribeTool;
    public static Item earthScribeTool;
    public static Item airScribeTool;
    public static Item duskScribeTool;
    public static Item dawnScribeTool;
    public static Item itemRitualDiviner;
    public static Item ritualDismantler;
    public static Item activationCrystal;

    // Guide-API
    public static Item itemMailCatalogue;

    // Unimplemented
    public static Item focusBloodBlast;
    public static Item focusGravityWell;
    public static Item itemBloodFrame;

    public static void init() {
        // Orbs
        weakBloodOrb = new EnergyBattery(5000).setUnlocalizedName("weakBloodOrb");
        apprenticeBloodOrb = new ApprenticeBloodOrb(25000).setUnlocalizedName("apprenticeBloodOrb");
        magicianBloodOrb = new MagicianBloodOrb(150000).setUnlocalizedName("magicianBloodOrb");
        masterBloodOrb = new MasterBloodOrb(1000000).setUnlocalizedName("masterBloodOrb");
        archmageBloodOrb = new ArchmageBloodOrb(10000000).setUnlocalizedName("archmageBloodOrb");
        transcendentBloodOrb = new TranscendentBloodOrb(30000000).setUnlocalizedName("transcendentBloodOrb");
        creativeFiller = new CheatyItem().setUnlocalizedName("cheatyItem");

        // Bound Tools/Weapons
        energySword = new EnergySword().setUnlocalizedName("energySword");
        boundPickaxe = new BoundPickaxe().setUnlocalizedName("boundPickaxe");
        boundAxe = new BoundAxe().setUnlocalizedName("boundAxe");
        boundShovel = new BoundShovel().setUnlocalizedName("boundShovel");
        customTool = new ItemSpellMultiTool().setUnlocalizedName("multiTool");
        SpellParadigmTool.customTool = customTool;
        energyBlaster = new EnergyBlast(1).setUnlocalizedName("energyBlast");
        energyBlasterSecondTier = new EnergyBlast(2).setUnlocalizedName("energyBlastSecondTier");
        energyBlasterThirdTier = new EnergyBlast(3).setUnlocalizedName("energyBlastThirdTier");
        energyBazooka = new EnergyBazooka(1).setUnlocalizedName("energyBazooka");
        energyBazookaSecondTier = new EnergyBazooka(2).setUnlocalizedName("energyBazookaSecondTier");
        energyBazookaThirdTier = new EnergyBazooka(3).setUnlocalizedName("energyBazookaThirdTier");

        // Armor
        itemBloodPack = new ItemBloodLetterPack().setUnlocalizedName("itemBloodPack");

        boundHelmet = new BoundArmour(0).setUnlocalizedName("boundHelmet");
        boundPlate = new BoundArmour(1).setUnlocalizedName("boundPlate");
        boundLeggings = new BoundArmour(2).setUnlocalizedName("boundLeggings");
        boundBoots = new BoundArmour(3).setUnlocalizedName("boundBoots");

        boundHelmetWater = new OmegaArmourWater(0).setUnlocalizedName("boundHelmetWater");
        boundPlateWater = new OmegaArmourWater(1).setUnlocalizedName("boundPlateWater");
        boundLeggingsWater = new OmegaArmourWater(2).setUnlocalizedName("boundLeggingsWater");
        boundBootsWater = new OmegaArmourWater(3).setUnlocalizedName("boundBootsWater");

        boundHelmetEarth = new OmegaArmourEarth(0).setUnlocalizedName("boundHelmetEarth");
        boundPlateEarth = new OmegaArmourEarth(1).setUnlocalizedName("boundPlateEarth");
        boundLeggingsEarth = new OmegaArmourEarth(2).setUnlocalizedName("boundLeggingsEarth");
        boundBootsEarth = new OmegaArmourEarth(3).setUnlocalizedName("boundBootsEarth");

        boundHelmetWind = new OmegaArmourWind(0).setUnlocalizedName("boundHelmetWind");
        boundPlateWind = new OmegaArmourWind(1).setUnlocalizedName("boundPlateWind");
        boundLeggingsWind = new OmegaArmourWind(2).setUnlocalizedName("boundLeggingsWind");
        boundBootsWind = new OmegaArmourWind(3).setUnlocalizedName("boundBootsWind");

        boundHelmetFire = new OmegaArmourFire(0).setUnlocalizedName("boundHelmetFire");
        boundPlateFire = new OmegaArmourFire(1).setUnlocalizedName("boundPlateFire");
        boundLeggingsFire = new OmegaArmourFire(2).setUnlocalizedName("boundLeggingsFire");
        boundBootsFire = new OmegaArmourFire(3).setUnlocalizedName("boundBootsFire");

        // Sigils
        // T1
        divinationSigil = new SigilDivination().setUnlocalizedName("divinationSigil");
        itemSeerSigil = new SigilSeer().setUnlocalizedName("itemSeerSigil");
        waterSigil = new SigilWater().setUnlocalizedName("waterSigil");
        lavaSigil = new SigilLava().setUnlocalizedName("lavaSigil");
        // T2
        airSigil = new SigilAir().setUnlocalizedName("airSigil");
        voidSigil = new SigilVoid().setUnlocalizedName("voidSigil");
        growthSigil = new SigilOfGrowth().setUnlocalizedName("growthSigil");
        sigilOfTheFastMiner = new SigilOfTheFastMiner().setUnlocalizedName("sigilOfTheFastMiner");
        // T3
        sigilOfElementalAffinity = new SigilOfElementalAffinity().setUnlocalizedName("sigilOfElementalAffinity");
        itemBloodLightSigil = new SigilBloodLight().setUnlocalizedName("bloodLightSigil");
        sigilOfTheBridge = new SigilOfTheBridge().setUnlocalizedName("sigilOfTheBridge");
        sigilOfMagnetism = new SigilOfMagnetism().setUnlocalizedName("sigilOfMagnetism");
        sigilOfHolding = new SigilOfHolding().setUnlocalizedName("sigilOfHolding");
        // T4
        sigilOfWind = new SigilOfWind().setUnlocalizedName("sigilOfWind");
        itemSigilOfSupression = new SigilOfSupression().setUnlocalizedName("itemSigilOfSupression");
        sigilOfHaste = new SigilOfHaste().setUnlocalizedName("sigilOfHaste");
        itemSigilOfEnderSeverance = (new SigilOfEnderSeverance()).setUnlocalizedName("itemSigilOfEnderSeverance");
        itemCompressionSigil = new SigilPackRat().setUnlocalizedName("itemCompressionSigil");
        // T5
        itemHarvestSigil = new SigilHarvest().setUnlocalizedName("itemHarvestSigil");
        // Unimplemented
        itemFluidSigil = new SigilFluid().setUnlocalizedName("itemFluidSigil");
        itemAssassinSigil = new SigilOfTheAssassin().setUnlocalizedName("itemAssassinSigil");

        // Crafting Materials
        blankSlate = new AWBaseItems().setUnlocalizedName("blankSlate");
        reinforcedSlate = new AWBaseItems().setUnlocalizedName("reinforcedSlate");
        imbuedSlate = new AWBaseItems().setUnlocalizedName("imbuedSlate");
        demonicSlate = new AWBaseItems().setUnlocalizedName("demonicSlate");
        baseItems = new ItemComponents().setUnlocalizedName("baseItems");
        weakBloodShard = new BloodShard().setUnlocalizedName("weakBloodShard");
        demonBloodShard = new BloodShard().setUnlocalizedName("demonBloodShard");
        bucketLife = new LifeBucket(ModBlocks.blockLifeEssence).setUnlocalizedName("bucketLife")
                .setContainerItem(Items.bucket).setCreativeTab(CreativeTabs.tabMisc);

        // Alchemy
        // Potions
        alchemyFlask = new AlchemyFlask().setUnlocalizedName("alchemyFlask");
        simpleCatalyst = new AlchemyReagent().setUnlocalizedName("simpleCatalyst");
        weakBindingAgent = new WeakBindingAgent().setUnlocalizedName("weakBindingAgent");
        standardBindingAgent = new StandardBindingAgent().setUnlocalizedName("standardBindingAgent");
        mundanePowerCatalyst = new MundanePowerCatalyst().setUnlocalizedName("mundanePowerCatalyst");
        averagePowerCatalyst = new AveragePowerCatalyst().setUnlocalizedName("averagePowerCatalyst");
        greaterPowerCatalyst = new GreaterPowerCatalyst().setUnlocalizedName("greaterPowerCatalyst");
        mundaneLengtheningCatalyst = new MundaneLengtheningCatalyst().setUnlocalizedName("mundaneLengtheningCatalyst");
        averageLengtheningCatalyst = new AverageLengtheningCatalyst().setUnlocalizedName("averageLengtheningCatalyst");
        greaterLengtheningCatalyst = new GreaterLengtheningCatalyst().setUnlocalizedName("greaterLengtheningCatalyst");
        weakFillingAgent = new WeakFillingAgent().setUnlocalizedName("weakFillingAgent");
        standardFillingAgent = new StandardFillingAgent().setUnlocalizedName("standardFillingAgent");
        enhancedFillingAgent = new EnhancedFillingAgent().setUnlocalizedName("enhancedFillingAgent");
        // Reagents
        itemAttunedCrystal = new ItemAttunedCrystal().setUnlocalizedName("itemAttunedCrystal");
        itemTankSegmenter = new ItemTankSegmenter().setUnlocalizedName("itemTankSegmenter");
        itemDestinationClearer = new ItemDestinationClearer().setUnlocalizedName("destinationClearer");
        incendium = new AlchemyReagent().setUnlocalizedName("incendium");
        magicales = new AlchemyReagent().setUnlocalizedName("magicales");
        sanctus = new AlchemyReagent().setUnlocalizedName("sanctus");
        aether = new AlchemyReagent().setUnlocalizedName("aether");
        crepitous = new AlchemyReagent().setUnlocalizedName("crepitous");
        crystallos = new AlchemyReagent().setUnlocalizedName("crystallos");
        terrae = new AlchemyReagent().setUnlocalizedName("terrae");
        aquasalus = new AlchemyReagent().setUnlocalizedName("aquasalus");
        tennebrae = new AlchemyReagent().setUnlocalizedName("tennebrae");
        // Other Alchemy Items
        baseAlchemyItems = new ItemAlchemyBase().setUnlocalizedName("baseAlchemyItems");
        itemIncense = new ItemIncense().setUnlocalizedName("bloodMagicIncenseItem");
        // Unused
        itemCombinationalCatalyst = new CombinationalCatalyst().setUnlocalizedName("itemCombinationalCatalyst");

        // Utility
        sacrificialDagger = new SacrificialDagger().setUnlocalizedName("sacrificialDagger");
        daggerOfSacrifice = new DaggerOfSacrifice().setUnlocalizedName("daggerOfSacrifice");
        creativeDagger = new CreativeDagger().setUnlocalizedName("creativeDagger");
        lavaCrystal = new LavaCrystal().setUnlocalizedName("lavaCrystal");
        itemKeyOfDiablo = new ItemDiabloKey().setUnlocalizedName("itemDiabloKey");
        armourInhibitor = new ArmourInhibitor().setUnlocalizedName("armourInhibitor");
        telepositionFocus = new TelepositionFocus(1).setUnlocalizedName("telepositionFocus");
        enhancedTelepositionFocus = new EnhancedTelepositionFocus().setUnlocalizedName("enhancedTelepositionFocus");
        reinforcedTelepositionFocus = new ReinforcedTelepositionFocus()
                .setUnlocalizedName("reinforcedTelepositionFocus");
        demonicTelepositionFocus = new DemonicTelepositionFocus().setUnlocalizedName("demonicTelepositionFocus");
        inputRoutingFocus = new InputRoutingFocus().setUnlocalizedName("inputRoutingFocus");
        outputRoutingFocus = new OutputRoutingFocus().setUnlocalizedName("outputRoutingFocus");
        blankSpell = new BlankSpell().setUnlocalizedName("blankSpell");
        itemComplexSpellCrystal = new ItemComplexSpellCrystal().setUnlocalizedName("itemComplexSpellCrystal");
        demonPlacer = new DemonPlacer().setUnlocalizedName("demonPlacer");

        // Rituals
        waterScribeTool = new ScribeTool(1, "WaterScribeTool").setUnlocalizedName("waterScribeTool");
        fireScribeTool = new ScribeTool(2, "FireScribeTool").setUnlocalizedName("fireScribeTool");
        earthScribeTool = new ScribeTool(3, "EarthScribeTool").setUnlocalizedName("earthScribeTool");
        airScribeTool = new ScribeTool(4, "AirScribeTool").setUnlocalizedName("airScribeTool");
        duskScribeTool = new ScribeTool(5, "DuskScribeTool").setUnlocalizedName("duskScribeTool");
        dawnScribeTool = new ScribeTool(6, "DawnScribeTool").setUnlocalizedName("dawnScribeTool");
        itemRitualDiviner = new ItemRitualDiviner().setUnlocalizedName("ritualDiviner");
        ritualDismantler = new ItemRitualDismantler().setUnlocalizedName("ritualDismantler");
        activationCrystal = new ActivationCrystal();
    }

    public static void registerItems() {
        // Orbs
        GameRegistry.registerItem(ModItems.weakBloodOrb, "weakBloodOrb");
        GameRegistry.registerItem(ModItems.apprenticeBloodOrb, "apprenticeBloodOrb");
        GameRegistry.registerItem(ModItems.magicianBloodOrb, "magicianBloodOrb");
        GameRegistry.registerItem(ModItems.masterBloodOrb, "masterBloodOrb");
        GameRegistry.registerItem(ModItems.archmageBloodOrb, "archmageBloodOrb");
        GameRegistry.registerItem(ModItems.transcendentBloodOrb, "transcendentBloodOrb");
        GameRegistry.registerItem(ModItems.creativeFiller, "creativeFiller");

        // Bound Tools/Weapons
        GameRegistry.registerItem(ModItems.energySword, "energySword");
        GameRegistry.registerItem(ModItems.boundPickaxe, "boundPickaxe");
        GameRegistry.registerItem(ModItems.boundAxe, "boundAxe");
        GameRegistry.registerItem(ModItems.boundShovel, "boundShovel");
        GameRegistry.registerItem(ModItems.customTool, "customTool");
        GameRegistry.registerItem(ModItems.energyBlaster, "energyBlaster");
        GameRegistry.registerItem(ModItems.energyBlasterSecondTier, "energyBlasterSecondTier");
        GameRegistry.registerItem(ModItems.energyBlasterThirdTier, "energyBlasterThirdTier");
        GameRegistry.registerItem(ModItems.energyBazooka, "energyBazooka");
        GameRegistry.registerItem(ModItems.energyBazookaSecondTier, "energyBazookaSecondTier");
        GameRegistry.registerItem(ModItems.energyBazookaThirdTier, "energyBazookaThirdTier");

        // Armor
        GameRegistry.registerItem(ModItems.itemBloodPack, "itemBloodPack");

        GameRegistry.registerItem(ModItems.boundHelmet, "boundHelmet");
        GameRegistry.registerItem(ModItems.boundPlate, "boundPlate");
        GameRegistry.registerItem(ModItems.boundLeggings, "boundLeggings");
        GameRegistry.registerItem(ModItems.boundBoots, "boundBoots");

        GameRegistry.registerItem(ModItems.boundHelmetWater, "boundHelmetWater");
        GameRegistry.registerItem(ModItems.boundPlateWater, "boundPlateWater");
        GameRegistry.registerItem(ModItems.boundLeggingsWater, "boundLeggingsWater");
        GameRegistry.registerItem(ModItems.boundBootsWater, "boundBootsWater");

        GameRegistry.registerItem(ModItems.boundHelmetEarth, "boundHelmetEarth");
        GameRegistry.registerItem(ModItems.boundPlateEarth, "boundPlateEarth");
        GameRegistry.registerItem(ModItems.boundLeggingsEarth, "boundLeggingsEarth");
        GameRegistry.registerItem(ModItems.boundBootsEarth, "boundBootsEarth");

        GameRegistry.registerItem(ModItems.boundHelmetWind, "boundHelmetWind");
        GameRegistry.registerItem(ModItems.boundPlateWind, "boundPlateWind");
        GameRegistry.registerItem(ModItems.boundLeggingsWind, "boundLeggingsWind");
        GameRegistry.registerItem(ModItems.boundBootsWind, "boundBootsWind");

        GameRegistry.registerItem(ModItems.boundHelmetFire, "boundHelmetFire");
        GameRegistry.registerItem(ModItems.boundPlateFire, "boundPlateFire");
        GameRegistry.registerItem(ModItems.boundLeggingsFire, "boundLeggingsFire");
        GameRegistry.registerItem(ModItems.boundBootsFire, "boundBootsFire");

        // Sigils
        // T1
        GameRegistry.registerItem(ModItems.divinationSigil, "divinationSigil");
        GameRegistry.registerItem(ModItems.itemSeerSigil, "seerSigil");
        GameRegistry.registerItem(ModItems.waterSigil, "waterSigil");
        GameRegistry.registerItem(ModItems.lavaSigil, "lavaSigil");
        // T2
        GameRegistry.registerItem(ModItems.airSigil, "airSigil");
        GameRegistry.registerItem(ModItems.voidSigil, "voidSigil");
        GameRegistry.registerItem(ModItems.growthSigil, "growthSigil");
        GameRegistry.registerItem(ModItems.sigilOfTheFastMiner, "sigilOfTheFastMiner");
        // T3
        GameRegistry.registerItem(ModItems.sigilOfElementalAffinity, "sigilOfElementalAffinity");
        GameRegistry.registerItem(ModItems.sigilOfTheBridge, "sigilOfTheBridge");
        GameRegistry.registerItem(ModItems.itemBloodLightSigil, "itemBloodLightSigil");
        GameRegistry.registerItem(ModItems.sigilOfMagnetism, "sigilOfMagnetism");
        GameRegistry.registerItem(ModItems.sigilOfHolding, "sigilOfHolding");
        // T4
        GameRegistry.registerItem(ModItems.sigilOfWind, "sigilOfWind");
        GameRegistry.registerItem(ModItems.itemSigilOfSupression, "sigilOfSupression");
        GameRegistry.registerItem(ModItems.sigilOfHaste, "sigilOfHaste");
        GameRegistry.registerItem(ModItems.itemSigilOfEnderSeverance, "sigilOfEnderSeverance");
        GameRegistry.registerItem(ModItems.itemCompressionSigil, "itemCompressionSigil");
        // T5
        GameRegistry.registerItem(ModItems.itemHarvestSigil, "itemHarvestSigil");
        // Unimplemented
        GameRegistry.registerItem(ModItems.itemFluidSigil, "fluidSigil");
        GameRegistry.registerItem(ModItems.itemAssassinSigil, "itemAssassinSigil");

        // Crafting Materials
        GameRegistry.registerItem(ModItems.blankSlate, "blankSlate");
        GameRegistry.registerItem(ModItems.reinforcedSlate, "reinforcedSlate");
        GameRegistry.registerItem(ModItems.imbuedSlate, "imbuedSlate");
        GameRegistry.registerItem(ModItems.demonicSlate, "demonicSlate");
        GameRegistry.registerItem(ModItems.baseItems, "bloodMagicBaseItems");
        GameRegistry.registerItem(ModItems.weakBloodShard, "weakBloodShard");
        GameRegistry.registerItem(ModItems.demonBloodShard, "demonBloodShard");
        GameRegistry.registerItem(ModItems.bucketLife, "bucketLife");

        // Alchemy
        // Potions
        GameRegistry.registerItem(ModItems.alchemyFlask, "alchemyFlask");
        GameRegistry.registerItem(ModItems.simpleCatalyst, "simpleCatalyst");
        GameRegistry.registerItem(ModItems.weakBindingAgent, "weakBindingAgent");
        GameRegistry.registerItem(ModItems.standardBindingAgent, "standardBindingAgent");
        GameRegistry.registerItem(ModItems.mundanePowerCatalyst, "mundanePowerCatalyst");
        GameRegistry.registerItem(ModItems.averagePowerCatalyst, "averagePowerCatalyst");
        GameRegistry.registerItem(ModItems.greaterPowerCatalyst, "greaterPowerCatalyst");
        GameRegistry.registerItem(ModItems.mundaneLengtheningCatalyst, "mundaneLengtheningCatalyst");
        GameRegistry.registerItem(ModItems.averageLengtheningCatalyst, "averageLengtheningCatalyst");
        GameRegistry.registerItem(ModItems.greaterLengtheningCatalyst, "greaterLengtheningCatalyst");
        GameRegistry.registerItem(ModItems.weakFillingAgent, "weakFillingAgent");
        GameRegistry.registerItem(ModItems.standardFillingAgent, "standardFillingAgent");
        GameRegistry.registerItem(ModItems.enhancedFillingAgent, "enhancedFillingAgent");
        // Reagents
        GameRegistry.registerItem(ModItems.itemAttunedCrystal, "itemAttunedCrystal");
        GameRegistry.registerItem(ModItems.itemTankSegmenter, "itemTankSegmenter");
        GameRegistry.registerItem(ModItems.itemDestinationClearer, "itemDestinationClearer");
        GameRegistry.registerItem(ModItems.incendium, "incendium");
        GameRegistry.registerItem(ModItems.magicales, "magicales");
        GameRegistry.registerItem(ModItems.sanctus, "sanctus");
        GameRegistry.registerItem(ModItems.aether, "aether");
        GameRegistry.registerItem(ModItems.crepitous, "crepitous");
        GameRegistry.registerItem(ModItems.crystallos, "crystallos");
        GameRegistry.registerItem(ModItems.terrae, "terrae");
        GameRegistry.registerItem(ModItems.aquasalus, "aquasalus");
        GameRegistry.registerItem(ModItems.tennebrae, "tennebrae");
        // Other Alchemy Items
        GameRegistry.registerItem(ModItems.baseAlchemyItems, "bloodMagicBaseAlchemyItems");
        GameRegistry.registerItem(ModItems.itemIncense, "bloodMagicIncenseItem");
        // Unused
        GameRegistry.registerItem(ModItems.itemCombinationalCatalyst, "itemCombinationalCatalyst");

        // Utility
        GameRegistry.registerItem(ModItems.sacrificialDagger, "sacrificialKnife");
        GameRegistry.registerItem(ModItems.daggerOfSacrifice, "daggerOfSacrifice");
        GameRegistry.registerItem(ModItems.creativeDagger, "creativeDagger");
        GameRegistry.registerItem(ModItems.lavaCrystal, "lavaCrystal");
        GameRegistry.registerItem(ModItems.itemKeyOfDiablo, "itemKeyOfDiablo");
        GameRegistry.registerItem(ModItems.armourInhibitor, "armourInhibitor");
        GameRegistry.registerItem(ModItems.telepositionFocus, "telepositionFocus");
        GameRegistry.registerItem(ModItems.enhancedTelepositionFocus, "enhancedTelepositionFocus");
        GameRegistry.registerItem(ModItems.reinforcedTelepositionFocus, "reinforcedTelepositionFocus");
        GameRegistry.registerItem(ModItems.demonicTelepositionFocus, "demonicTelepositionFocus");
        GameRegistry.registerItem(ModItems.inputRoutingFocus, "inputRoutingFocus");
        GameRegistry.registerItem(ModItems.outputRoutingFocus, "outputRoutingFocus");
        GameRegistry.registerItem(ModItems.blankSpell, "blankSpell");
        GameRegistry.registerItem(ModItems.itemComplexSpellCrystal, "itemComplexSpellCrystal");
        GameRegistry.registerItem(ModItems.demonPlacer, "demonPlacer");

        // Rituals
        GameRegistry.registerItem(ModItems.waterScribeTool, "waterScribeTool");
        GameRegistry.registerItem(ModItems.fireScribeTool, "fireScribeTool");
        GameRegistry.registerItem(ModItems.earthScribeTool, "earthScribeTool");
        GameRegistry.registerItem(ModItems.airScribeTool, "airScribeTool");
        GameRegistry.registerItem(ModItems.duskScribeTool, "duskScribeTool");
        GameRegistry.registerItem(ModItems.dawnScribeTool, "dawnScribeTool");
        GameRegistry.registerItem(ModItems.itemRitualDiviner, "itemRitualDiviner");
        GameRegistry.registerItem(ModItems.ritualDismantler, "ritualDismantler");
        GameRegistry.registerItem(ModItems.activationCrystal, "activationCrystal");
    }
}
