/*
 * Original class by https://github.com/Superfrogman98, copied with permission Original PR:
 * https://github.com/GTNewHorizons/StructureCompat/pull/6 Link to message giving permission (GT: New Horizons Discord
 * server): https://discord.com/channels/181078474394566657/603348502637969419/1367345532376453130
 */

package WayofTime.alchemicalWizardry.compat.structurelib;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bsideup.jabel.Desugar;
import com.google.common.base.Joiner;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.BlockStack;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

public class AltarStructure {

    private static final String STRUCTURE_ALTAR = "tier1";
    private static final String STRUCTURE_TIER_2 = "tier2";
    private static final String STRUCTURE_TIER_3 = "tier3";
    private static final String STRUCTURE_TIER_4 = "tier4";
    private static final String STRUCTURE_TIER_5 = "tier5";
    private static final String STRUCTURE_TIER_6 = "tier6";
    @SuppressWarnings("rawtypes")
    private static final AnyBlock ANY_BLOCK = new AnyBlock();

    public static final int[][] TIER_OFFSET = { { 0, 0, 0 }, { 1, -1, 1 }, { 3, 1, 3 }, { 5, 2, 5 }, { 8, -3, 8 },
            { 11, 3, 11 } };

    @SuppressWarnings("unchecked")
    public static void registerAltarStructureInfo() {
        StructureDefinition.Builder<TEAltar> altarBuilder = IStructureDefinition.builder();
        String T6_S = "                       ";
        String T6_SR = "r                     r";
        // region Structure
        // spotless:off
        altarBuilder
                .addShape(STRUCTURE_ALTAR, new String[][] {{"a"}})
                .addShape(STRUCTURE_TIER_2, transpose(new String[][] {{"rrr","r r","rrr"}}))
                .addShape(STRUCTURE_TIER_3, transpose(new String[][] {{"g     g","       ","       ","       ","       ","       ","g     g"},
                        {"t     t","       ","       ","       ","       ","       ","t     t"},
                        {"t     t","       ","       ","       ","       ","       ","t     t"},
                        {" rrrrr ","r     r","r     r","r     r","r     r","r     r"," rrrrr "}}))
                .addShape(STRUCTURE_TIER_4, transpose(new String[][]
                        {{"b         b","           ","           ","           ","           ","           ","           ","           ","           ","           ","b         b"},
                                {"v         v","           ","           ","           ","           ","           ","           ","           ","           ","           ","v         v"},
                                {"v         v","           ","           ","           ","           ","           ","           ","           ","           ","           ","v         v"},
                                {"v         v","           ","           ","           ","           ","           ","           ","           ","           ","           ","v         v"},
                                {"v         v","           ","           ","           ","           ","           ","           ","           ","           ","           ","v         v"},
                                {"  rrrrrrr  ","           ","r         r","r         r","r         r","r         r","r         r","r         r","r         r","           ","  rrrrrrr  "}}))
                .addShape(STRUCTURE_TIER_5, transpose(new String[][] {{"e               e","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","                 ","e               e"},
                        {"  rrrrrrrrrrrrr  ","                 ","r               r","r               r","r               r","r               r","r               r","r               r","r               r","r               r","r               r","r               r","r               r","r               r","r               r","                 ","  rrrrrrrrrrrrr  "}}))
                .addShape(STRUCTURE_TIER_6, transpose(new String[][]
                        {
                                {"c                     c",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"c                     c"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"i                     i",T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,T6_S,"i                     i"},
                                {"  rrrrrrrrrrrrrrrrrrr  ",T6_S,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_SR,T6_S, "  rrrrrrrrrrrrrrrrrrr  "},
                        }))
                .addElement('a',ofBlock(ModBlocks.blockAltar,0))
                .addElement('r',ofChain(ofBlockAnyMeta(ModBlocks.bloodRune),ofBlockAnyMeta(ModBlocks.speedRune),ofBlockAnyMeta(ModBlocks.runeOfSelfSacrifice),ofBlockAnyMeta(ModBlocks.runeOfSacrifice),ofBlockAnyMeta(ModBlocks.efficiencyRune)));

        char[] keys = {'t', 'g', 'v', 'b', 'e', 'i', 'c'};
        List<BlockStack>[] values = new List[] {
                AlchemicalWizardry.thirdTierPillars,
                AlchemicalWizardry.thirdTierCaps,
                AlchemicalWizardry.fourthTierPillars,
                AlchemicalWizardry.fourthTierCaps,
                AlchemicalWizardry.fifthTierBeacons,
                AlchemicalWizardry.sixthTierPillars,
                AlchemicalWizardry.sixthTierCaps
        };

        for (int i = 0; i < keys.length; i++) {
            if (values[i].isEmpty()) {
                altarBuilder.addElement(keys[i], ANY_BLOCK);
                continue;
            }

            altarBuilder.addElement(keys[i],
                    ofChain(
                            values[i].stream()
                                    .map(s -> s.getMeta() == OreDictionary.WILDCARD_VALUE
                                            ? ofBlockAnyMeta(s.getBlock())
                                            : ofBlock(s.getBlock(), s.getMeta()))
                                    .toArray(IStructureElement[]::new)
                    )
            );
        }


        IMultiblockInfoContainer.registerTileClass(TEAltar.class,
                new AltarStructure.AltarMultiblockInfoContainer(altarBuilder.build()));
        // spotless:on
    }

    @Desugar
    private record AltarMultiblockInfoContainer(IStructureDefinition<TEAltar> structureAltar)
            implements IMultiblockInfoContainer<TEAltar> {

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEAltar ctx, ExtendedFacing aSide) {
            int tier = triggerStack.stackSize;
            if (tier > 6) {
                tier = 6;
            }
            for (int i = 1; i <= tier; i++) {
                this.structureAltar.buildOrHints(
                        ctx,
                        triggerStack,
                        "tier" + i,
                        ctx.getWorldObj(),
                        ExtendedFacing.DEFAULT,
                        ctx.xCoord,
                        ctx.yCoord,
                        ctx.zCoord,
                        TIER_OFFSET[i - 1][0],
                        TIER_OFFSET[i - 1][1],
                        TIER_OFFSET[i - 1][2],
                        hintsOnly);
            }
        }

        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudge, ISurvivalBuildEnvironment env,
                TEAltar altar, ExtendedFacing aSide) {
            int built = 0;
            int tier = triggerStack.stackSize;
            if (tier > 6) {
                tier = 6;
            }
            if (altar.getTier() >= tier) return -1;

            for (int i = 1; i <= tier; i++) {
                built += this.structureAltar.survivalBuild(
                        altar,
                        triggerStack,
                        "tier" + i,
                        altar.getWorldObj(),
                        ExtendedFacing.DEFAULT,
                        altar.xCoord,
                        altar.yCoord,
                        altar.zCoord,
                        TIER_OFFSET[i - 1][0],
                        TIER_OFFSET[i - 1][1],
                        TIER_OFFSET[i - 1][2],
                        elementBudge,
                        env,
                        false);
            }
            return built;
        }

        @SuppressWarnings("unchecked")
        @Override
        public String[] getDescription(ItemStack stackSize) {
            ArrayList<String> out = new ArrayList<>();
            out.add(StatCollector.translateToLocal("structurelib.bloodmagic.altar.1"));
            out.add(StatCollector.translateToLocal("structurelib.bloodmagic.altar.2"));

            int[] pillarTiers = { 3, 4, 6 };
            List<BlockStack>[] pillarLists = new List[] { AlchemicalWizardry.thirdTierPillars,
                    AlchemicalWizardry.fourthTierPillars, AlchemicalWizardry.sixthTierPillars };
            ArrayList<Integer> pillars = new ArrayList<>();

            for (int i = 0; i < pillarTiers.length; i++) {
                if (pillarLists[i].isEmpty()) {
                    pillars.add(pillarTiers[i]);
                }
            }

            if (!pillars.isEmpty()) {
                out.add(
                        StatCollector.translateToLocalFormatted(
                                "structurelib.bloodmagic.altar.3",
                                Joiner.on(", ").join(pillars)));
            }

            int[] capTiers = { 3, 4, 6 };
            List<BlockStack>[] capLists = new List[] { AlchemicalWizardry.thirdTierCaps,
                    AlchemicalWizardry.fourthTierCaps, AlchemicalWizardry.sixthTierCaps };
            ArrayList<Integer> caps = new ArrayList<>();

            for (int i = 0; i < capTiers.length; i++) {
                if (capLists[i].isEmpty()) {
                    caps.add(capTiers[i]);
                }
            }

            if (!caps.isEmpty()) {
                out.add(
                        StatCollector.translateToLocalFormatted(
                                "structurelib.bloodmagic.altar.4",
                                Joiner.on(", ").join(caps)));
            }

            if (AlchemicalWizardry.fifthTierBeacons.isEmpty()) { // T5 Beacon
                out.add(StatCollector.translateToLocal("structurelib.bloodmagic.altar.5"));
            }

            return out.toArray(new String[0]);
        }
    }
}
