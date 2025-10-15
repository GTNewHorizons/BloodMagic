package WayofTime.alchemicalWizardry.common.summoning.meteor;

import static WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorComponent.defaultMeteorBlock;
import static WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorComponent.getTotalListWeight;
import static WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorReagentRegistry.getFillerList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;

public class Meteor {

    public List<MeteorComponent> ores;
    public List<MeteorComponent> filler;
    public int radius;
    public int cost;
    public float fillerChance; // Out of 100.0
    public ItemStack focusItem;
    private String focusModId;
    private String focusName;
    private int focusMeta;
    private int maxWeight;

    public static Random rand = new Random();

    public Meteor() {
        this.radius = 1;
        this.cost = AlchemicalWizardry.defaultMeteorCost;
        this.fillerChance = 0;
        this.maxWeight = 0;
        this.preventNullLists();
    }

    public Meteor(ItemStack focusStack, int radius, int cost, int fillerChance, List<MeteorComponent> oreList,
            List<MeteorComponent> fillerList) {
        this.focusItem = focusStack;
        this.radius = radius;
        this.cost = cost;
        this.fillerChance = fillerChance;
        this.ores = oreList;
        this.filler = fillerList;
    }

    public void createMeteorImpact(World world, int x, int y, int z, List<Reagent> reagents) {
        int radius = getNewRadius(this.radius, reagents);
        float fillerChance = getNewFillerChance(this.fillerChance, reagents);

        if (MeteorReagentRegistry.doExplosions(reagents)) {
            world.createExplosion(null, x, y, z, radius * 4, MeteorReagentRegistry.doMeteorsDestroyBlocks(reagents));
        }

        List<MeteorComponent> oreList = removeBlocksMissingRequiredReagents(this.ores, reagents);
        List<MeteorComponent> fillerList = getNewFillerList(this.filler, reagents);

        int totalComponentWeight = MeteorComponent.getTotalListWeight(oreList);
        int totalFillerWeight = MeteorComponent.getTotalListWeight(fillerList);

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f)) {
                        continue;
                    }

                    if (!world.isAirBlock(x + i, y + j, z + k)) {
                        continue;
                    }

                    if (fillerChance < 100 && (fillerChance <= 0 || world.rand.nextFloat() * 100 >= fillerChance)) {
                        setMeteorBlock(x + i, y + j, z + k, world, oreList, totalComponentWeight);
                    } else {
                        setMeteorBlock(x + i, y + j, z + k, world, fillerList, totalFillerWeight);
                    }
                }
            }
        }
    }

    public static int getNewRadius(int radius, List<Reagent> reagents) {
        radius += MeteorReagentRegistry.getLargestRadiusIncrease(reagents);
        radius += MeteorReagentRegistry.getLargestRadiusDecrease(reagents);
        return Math.max(radius, 1);
    }

    public static float getNewFillerChance(float fillerChance, List<Reagent> reagents) {
        // Don't add filler to meteors that have none
        if (fillerChance <= 0) {
            return 0;
        }

        float increase = MeteorReagentRegistry.getLargestFillerChanceIncrease(reagents);
        float decrease = MeteorReagentRegistry.getLargestFillerChanceDecrease(reagents);
        // Avoid division by zero
        if (decrease == -100) {
            return 0;
        }

        fillerChance += MeteorReagentRegistry.getLargestRawFillerChanceIncrease(reagents);
        fillerChance += MeteorReagentRegistry.getLargestRawFillerChanceDecrease(reagents);
        if (increase > 0) {
            fillerChance = 100 * (fillerChance + increase) / (100 + increase);
        } else if (decrease < 0) {
            fillerChance = 100 * (fillerChance + decrease) / (100 + decrease);
        }
        return Math.max(0, Math.min(fillerChance, 100));
    }

    /**
     * @return a new filler list. The original list is entirely replaced if any of the supplied reagents have a filler
     *         config.
     */
    public static List<MeteorComponent> getNewFillerList(List<MeteorComponent> fillerList, List<Reagent> reagents) {
        List<MeteorComponent> reagentFillers = getFillerList(reagents);
        reagentFillers = removeBlocksMissingRequiredReagents(reagentFillers, reagents);
        if (!reagentFillers.isEmpty() && reagentFillers.get(0) != defaultMeteorBlock) {
            return reagentFillers;
        }
        return removeBlocksMissingRequiredReagents(fillerList, reagents);
    }

    public static List<MeteorComponent> removeBlocksMissingRequiredReagents(List<MeteorComponent> blockList,
            List<Reagent> reagents) {
        ArrayList<MeteorComponent> newList = new ArrayList<>();
        for (MeteorComponent component : blockList) {
            if (component.checkForReagent(reagents)) {
                newList.add(component);
            }
        }
        if (newList.isEmpty()) { // Use the default if every filler requires a reagent
            newList.add(defaultMeteorBlock);
        }
        return newList;
    }

    /**
     * Sets a randomly chosen block from blocklist according to the MeteorComponent weights at position x, y, z in the
     * provided world.
     */
    private void setMeteorBlock(int x, int y, int z, World world, List<MeteorComponent> blockList,
            int totalListWeight) {
        int randNum = world.rand.nextInt(totalListWeight);
        for (MeteorComponent component : blockList) {
            randNum -= component.getWeight();

            if (randNum < 0) {
                ItemStack blockStack = component.getBlock();
                if (blockStack != null && blockStack.getItem() instanceof ItemBlock) {
                    ((ItemBlock) blockStack.getItem())
                            .placeBlockAt(blockStack, null, world, x, y, z, 0, 0, 0, 0, blockStack.getItemDamage());
                    if (AlchemicalWizardry.isGregTechLoaded) setGTOresNaturalIfNeeded(world, x, y, z);
                    world.markBlockForUpdate(x, y, z);
                    break;
                }
            }
        }
    }

    public void validate() {
        this.preventNullLists();
        this.clampNumericalValues();
        this.buildFocusItem();
        this.maxWeightToFillerChance();
    }

    private void preventNullLists() {
        if (ores == null || ores.isEmpty()) {
            ores = new ArrayList<>();
            ores.add(defaultMeteorBlock);
        }
        while (ores.contains(null)) {
            ores.remove(null);
        }
        if (filler == null || filler.isEmpty()) {
            filler = new ArrayList<>();
            filler.add(defaultMeteorBlock);
        }
        while (filler.contains(null)) {
            filler.remove(null);
        }
    }

    /**
     * Clamps all numerical values to their minimum/maximum values.
     */
    private void clampNumericalValues() {
        radius = Math.max(1, radius);
        cost = Math.max(0, cost);
        fillerChance = Math.max(0f, Math.min(fillerChance, 100.0f));
        focusMeta = Math.max(0, focusMeta);
        maxWeight = Math.max(0, maxWeight);
    }

    /**
     * Construct the focus item for this meteor from its modid, name, and metadata if using the old config format.
     */
    private void buildFocusItem() {
        if (this.focusItem != null) return;
        this.focusItem = GameRegistry.findItemStack(this.focusModId, this.focusName, 1);
        if (this.focusItem == null) return;
        this.focusItem.setItemDamage(this.focusMeta);
        AlchemicalWizardry.logger.warn(
                "Setting focusModId, focusName, and focusMeta individually in meteor"
                        + "configs has been deprecated. Set \"focusItem\" with format \"modId:name(:meta optional)\""
                        + " instead.");
    }

    /**
     * Turn the maximum weight into an equivalent filler chance based on the provided block list. Does NOT restrict
     * blocks over the max weight like the old weight system! Specify required reagents for the blocks you want to
     * restrict in the ores tag for the meteor's config instead! This is used for the default meteors which include
     * certain ores which may not be present in all environments.
     */
    private void maxWeightToFillerChance() {
        if (maxWeight <= 0 || fillerChance > 0) {
            return;
        }
        this.fillerChance = Math.max(0f, Math.min(100f * (1 - ((float) getTotalListWeight(ores) / maxWeight)), 100.0f));
    }

    @Optional.Method(modid = "gregtech")
    private static void setGTOresNaturalIfNeeded(World world, int x, int y, int z) {
        try (OreInfo<?> info = OreManager.getOreInfo(world, x, y, z)) {
            if (info == null) return;

            info.isNatural = true;

            ImmutableBlockMeta bm = OreManager.getAdapter(info).getBlock(info);

            world.setBlock(x, y, z, bm.getBlock(), bm.getBlockMeta(), 2);
        }
    }
}
