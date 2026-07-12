package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.items.ItemSpellMultiTool;
import WayofTime.alchemicalWizardry.api.spell.IDigAreaEffect;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.common.items.BoundPickaxe;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class DigAreaEffect implements IDigAreaEffect {

    protected int powerUpgrades;
    protected int potencyUpgrades;
    protected int costUpgrades;

    public DigAreaEffect(int power, int potency, int cost) {
        this.powerUpgrades = power;
        this.potencyUpgrades = potency;
        this.costUpgrades = cost;
    }

    @Override
    public int digSurroundingArea(ItemStack container, World world, EntityPlayer player, MovingObjectPosition blockPos,
            String usedToolClass, float blockHardness, int harvestLvl, ItemSpellMultiTool itemTool) {
        if (!blockPos.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK)) {
            return 0;
        }

        int x = blockPos.blockX;
        int y = blockPos.blockY;
        int z = blockPos.blockZ;

        for (int xPos = x - 1; xPos <= x + 1; xPos++) {
            for (int yPos = y - 1; yPos <= y + 1; yPos++) {
                for (int zPos = z - 1; zPos <= z + 1; zPos++) {
                    this.breakBlock(container, world, player, blockHardness, xPos, yPos, zPos, itemTool);
                }
            }
        }

        return 0;
    }

    public void breakBlock(ItemStack container, World world, EntityPlayer player, float blockHardness, int x, int y,
            int z, ItemSpellMultiTool itemTool) {
        Block block = world.getBlock(x, y, z);
        if (block == null || block == Blocks.air) return;
        int meta = world.getBlockMetadata(x, y, z);
        String blockRequiredTool = block.getHarvestTool(meta);
        if (blockRequiredTool != null && itemTool.getHarvestLevel(container, blockRequiredTool) != -1
                && block.getHarvestLevel(meta) > itemTool.getHarvestLevel(container, blockRequiredTool)) {
            return;
        }

        float localHardness = block.getBlockHardness(world, x, y, z);

        if (localHardness < 0 || localHardness - this.getHardnessDifference() > blockHardness
                || BoundPickaxe.isBreakDenied(world, x, y, z, player)) {
            return;
        }

        String localToolClass = itemTool.getToolClassForMaterial(block.getMaterial());

        if (player.capabilities.isCreativeMode) {
            world.setBlockToAir(x, y, z);
            world.func_147479_m(x, y, z);
            return;
        }
        if ((localToolClass == null
                || itemTool.getHarvestLevel(container, blockRequiredTool) < block.getHarvestLevel(meta))
                && !block.getMaterial().isToolNotRequired()) {
            return;
        }
        if (block.removedByPlayer(world, player, x, y, z, true)) {
            block.onBlockDestroyedByPlayer(world, x, y, z, meta);
        }
        block.onBlockHarvested(world, x, y, z, meta, player);
        if (localHardness > 0f) itemTool.onBlockDestroyed(container, world, block, x, y, z, player);

        List<ItemStack> items = SpellHelper.getItemsFromBlock(
                world,
                block,
                x,
                y,
                z,
                meta,
                itemTool.getSilkTouch(container),
                itemTool.getFortuneLevel(container));

        SpellParadigmTool parad = itemTool.loadParadigmFromStack(container);
        items = parad.handleItemList(container, items);

        if (!world.isRemote) {
            SpellHelper.spawnItemListInWorld(items, world, x + 0.5f, y + 0.5f, z + 0.5f);
        }

        world.func_147479_m(x, y, z);
    }

    public float getHardnessDifference() {
        return 1.5f;
    }
}
