package WayofTime.alchemicalWizardry;

import net.minecraft.block.Block;

import WayofTime.alchemicalWizardry.common.block.ArmourForge;
import WayofTime.alchemicalWizardry.common.block.BlockAlchemicCalcinator;
import WayofTime.alchemicalWizardry.common.block.BlockAltar;
import WayofTime.alchemicalWizardry.common.block.BlockBelljar;
import WayofTime.alchemicalWizardry.common.block.BlockBloodLightSource;
import WayofTime.alchemicalWizardry.common.block.BlockConduit;
import WayofTime.alchemicalWizardry.common.block.BlockCrucible;
import WayofTime.alchemicalWizardry.common.block.BlockCrystal;
import WayofTime.alchemicalWizardry.common.block.BlockDemonPortal;
import WayofTime.alchemicalWizardry.common.block.BlockEnchantmentGlyph;
import WayofTime.alchemicalWizardry.common.block.BlockHomHeart;
import WayofTime.alchemicalWizardry.common.block.BlockMasterStone;
import WayofTime.alchemicalWizardry.common.block.BlockPedestal;
import WayofTime.alchemicalWizardry.common.block.BlockPlinth;
import WayofTime.alchemicalWizardry.common.block.BlockReagentConduit;
import WayofTime.alchemicalWizardry.common.block.BlockSchematicSaver;
import WayofTime.alchemicalWizardry.common.block.BlockSocket;
import WayofTime.alchemicalWizardry.common.block.BlockSpectralContainer;
import WayofTime.alchemicalWizardry.common.block.BlockSpellEffect;
import WayofTime.alchemicalWizardry.common.block.BlockSpellEnhancement;
import WayofTime.alchemicalWizardry.common.block.BlockSpellModifier;
import WayofTime.alchemicalWizardry.common.block.BlockSpellParadigm;
import WayofTime.alchemicalWizardry.common.block.BlockStabilityGlyph;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.block.BlockWritingTable;
import WayofTime.alchemicalWizardry.common.block.BloodRune;
import WayofTime.alchemicalWizardry.common.block.BloodStoneBrick;
import WayofTime.alchemicalWizardry.common.block.EfficiencyRune;
import WayofTime.alchemicalWizardry.common.block.EmptySocket;
import WayofTime.alchemicalWizardry.common.block.ImperfectRitualStone;
import WayofTime.alchemicalWizardry.common.block.LargeBloodStoneBrick;
import WayofTime.alchemicalWizardry.common.block.LifeEssenceBlock;
import WayofTime.alchemicalWizardry.common.block.MimicBlock;
import WayofTime.alchemicalWizardry.common.block.RitualStone;
import WayofTime.alchemicalWizardry.common.block.RuneOfSacrifice;
import WayofTime.alchemicalWizardry.common.block.RuneOfSelfSacrifice;
import WayofTime.alchemicalWizardry.common.block.SpectralBlock;
import WayofTime.alchemicalWizardry.common.block.SpeedRune;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.BlockDemonChest;
import WayofTime.alchemicalWizardry.common.items.ItemBlockCrystalBelljar;
import WayofTime.alchemicalWizardry.common.items.ItemBloodRuneBlock;
import WayofTime.alchemicalWizardry.common.items.ItemCrystalBlock;
import WayofTime.alchemicalWizardry.common.items.ItemEnchantmentGlyphBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEffectBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellEnhancementBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellModifierBlock;
import WayofTime.alchemicalWizardry.common.items.ItemSpellParadigmBlock;
import WayofTime.alchemicalWizardry.common.items.ItemStabilityGlyphBlock;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created with IntelliJ IDEA. User: Pokefenn Date: 17/01/14 Time: 19:48
 */
public class ModBlocks {

    // Blood Altar
    public static BlockAltar blockAltar;
    public static BloodRune bloodRune;
    public static SpeedRune speedRune;
    public static EfficiencyRune efficiencyRune;
    public static RuneOfSacrifice runeOfSacrifice;
    public static RuneOfSelfSacrifice runeOfSelfSacrifice;
    public static Block blockCrucible;
    public static Block largeBloodStoneBrick;
    public static Block bloodStoneBrick;
    public static Block blockCrystal;

    // Rituals
    public static Block imperfectRitualStone;
    public static Block ritualStone;
    public static Block blockMasterStone;
    public static Block blockPedestal;
    public static Block blockPlinth;
    public static Block blockStabilityGlyph;
    public static Block blockEnchantmentGlyph;

    // Reagents
    public static Block blockWritingTable;
    public static Block blockAlchemicCalcinator;
    public static Block blockReagentConduit;
    public static Block blockCrystalBelljar;

    // Bound Armor
    public static Block emptySocket;
    public static Block bloodSocket;
    public static Block armourForge;

    // Spells
    public static Block blockHomHeart;
    public static Block blockConduit;
    public static Block blockSpellEffect;
    public static Block blockSpellParadigm;
    public static Block blockSpellModifier;
    public static Block blockSpellEnhancement;

    // Misc
    public static Block blockTeleposer;
    public static Block spectralBlock;
    public static Block blockBloodLight;
    public static Block blockSpectralContainer;
    public static Block blockLifeEssence;
    public static Block blockDemonPortal;
    public static Block blockDemonChest;
    public static Block blockBuildingSchematicSaver;
    public static Block blockMimic;

    public static void init() {
        // Blood Altar
        blockAltar = new BlockAltar();
        bloodRune = new BloodRune();
        speedRune = new SpeedRune();
        efficiencyRune = new EfficiencyRune();
        runeOfSacrifice = new RuneOfSacrifice();
        runeOfSelfSacrifice = new RuneOfSelfSacrifice();
        blockCrucible = new BlockCrucible();
        bloodStoneBrick = new BloodStoneBrick();
        largeBloodStoneBrick = new LargeBloodStoneBrick();
        blockCrystal = new BlockCrystal();

        // Rituals
        imperfectRitualStone = new ImperfectRitualStone();
        ritualStone = new RitualStone();
        blockMasterStone = new BlockMasterStone();
        blockPedestal = new BlockPedestal();
        blockPlinth = new BlockPlinth();
        blockStabilityGlyph = new BlockStabilityGlyph();
        blockEnchantmentGlyph = new BlockEnchantmentGlyph();

        // Reagents
        blockWritingTable = new BlockWritingTable();
        blockAlchemicCalcinator = new BlockAlchemicCalcinator();
        blockReagentConduit = new BlockReagentConduit();
        blockCrystalBelljar = new BlockBelljar();

        // Bound Armor
        emptySocket = new EmptySocket();
        bloodSocket = new BlockSocket();
        armourForge = new ArmourForge();

        // Spells
        blockHomHeart = new BlockHomHeart();
        blockConduit = new BlockConduit();
        blockSpellEffect = new BlockSpellEffect();
        blockSpellParadigm = new BlockSpellParadigm();
        blockSpellModifier = new BlockSpellModifier();
        blockSpellEnhancement = new BlockSpellEnhancement();

        // Misc
        blockTeleposer = new BlockTeleposer();
        spectralBlock = new SpectralBlock();
        blockBloodLight = new BlockBloodLightSource();
        blockSpectralContainer = new BlockSpectralContainer();
        blockLifeEssence = new LifeEssenceBlock();
        blockDemonPortal = new BlockDemonPortal();
        blockDemonChest = new BlockDemonChest();
        blockBuildingSchematicSaver = new BlockSchematicSaver();
        blockMimic = new MimicBlock();
    }

    public static void registerBlocksInPre() {
        // Blood Altar
        GameRegistry.registerBlock(ModBlocks.blockAltar, "Altar");
        GameRegistry.registerBlock(
                ModBlocks.bloodRune,
                ItemBloodRuneBlock.class,
                "AlchemicalWizardry" + (ModBlocks.bloodRune.getUnlocalizedName().substring(5)));
        GameRegistry.registerBlock(ModBlocks.speedRune, "speedRune");
        GameRegistry.registerBlock(ModBlocks.efficiencyRune, "efficiencyRune");
        GameRegistry.registerBlock(ModBlocks.runeOfSacrifice, "runeOfSacrifice");
        GameRegistry.registerBlock(ModBlocks.runeOfSelfSacrifice, "runeOfSelfSacrifice");
        GameRegistry.registerBlock(ModBlocks.blockCrucible, "blockCrucible");
        GameRegistry.registerBlock(ModBlocks.largeBloodStoneBrick, "largeBloodStoneBrick");
        GameRegistry.registerBlock(ModBlocks.bloodStoneBrick, "bloodStoneBrick");
        GameRegistry.registerBlock(ModBlocks.blockCrystal, ItemCrystalBlock.class, "blockCrystal");

        // Rituals
        GameRegistry.registerBlock(ModBlocks.imperfectRitualStone, "imperfectRitualStone");
        GameRegistry.registerBlock(ModBlocks.ritualStone, "ritualStone");
        GameRegistry.registerBlock(ModBlocks.blockMasterStone, "masterStone");
        GameRegistry.registerBlock(ModBlocks.blockPedestal, "blockPedestal");
        GameRegistry.registerBlock(ModBlocks.blockPlinth, "blockPlinth");
        GameRegistry.registerBlock(ModBlocks.blockStabilityGlyph, ItemStabilityGlyphBlock.class, "blockStabilityGlyph");
        GameRegistry.registerBlock(
                ModBlocks.blockEnchantmentGlyph,
                ItemEnchantmentGlyphBlock.class,
                "blockEnchantmentGlyph");

        // Reagents
        GameRegistry.registerBlock(ModBlocks.blockWritingTable, "blockWritingTable");
        GameRegistry.registerBlock(ModBlocks.blockAlchemicCalcinator, "blockAlchemicCalcinator");
        GameRegistry.registerBlock(ModBlocks.blockReagentConduit, "blockReagentConduit");
        GameRegistry.registerBlock(ModBlocks.blockCrystalBelljar, ItemBlockCrystalBelljar.class, "blockCrystalBelljar");

        // Bound Armor
        GameRegistry.registerBlock(ModBlocks.emptySocket, "emptySocket");
        GameRegistry.registerBlock(ModBlocks.bloodSocket, "bloodSocket");
        GameRegistry.registerBlock(ModBlocks.armourForge, "armourForge");

        // Spells
        GameRegistry.registerBlock(ModBlocks.blockHomHeart, "blockHomHeart");
        GameRegistry.registerBlock(ModBlocks.blockConduit, "blockConduit");
        GameRegistry.registerBlock(
                ModBlocks.blockSpellParadigm,
                ItemSpellParadigmBlock.class,
                "AlchemicalWizardry" + (ModBlocks.blockSpellParadigm.getUnlocalizedName()));
        GameRegistry.registerBlock(
                ModBlocks.blockSpellEnhancement,
                ItemSpellEnhancementBlock.class,
                "AlchemicalWizardry" + (ModBlocks.blockSpellEnhancement.getUnlocalizedName()));
        GameRegistry.registerBlock(
                ModBlocks.blockSpellModifier,
                ItemSpellModifierBlock.class,
                "AlchemicalWizardry" + (ModBlocks.blockSpellModifier.getUnlocalizedName()));
        GameRegistry.registerBlock(
                ModBlocks.blockSpellEffect,
                ItemSpellEffectBlock.class,
                "AlchemicalWizardry" + (ModBlocks.blockSpellEffect.getUnlocalizedName()));

        // Misc
        GameRegistry.registerBlock(ModBlocks.blockTeleposer, "blockTeleposer");
        GameRegistry.registerBlock(ModBlocks.spectralBlock, "spectralBlock");
        GameRegistry.registerBlock(ModBlocks.blockBloodLight, "bloodLight");
        GameRegistry.registerBlock(ModBlocks.blockSpectralContainer, "spectralContainer");
        GameRegistry.registerBlock(ModBlocks.blockLifeEssence, "lifeEssence");
        GameRegistry.registerBlock(ModBlocks.blockDemonPortal, "demonPortalMain");
        GameRegistry.registerBlock(ModBlocks.blockDemonChest, "blockDemonChest");
        GameRegistry.registerBlock(ModBlocks.blockBuildingSchematicSaver, "blockSchemSaver");
        GameRegistry.registerBlock(ModBlocks.blockMimic, "blockMimic");
    }

    public static void registerBlocksInInit() {}
}
