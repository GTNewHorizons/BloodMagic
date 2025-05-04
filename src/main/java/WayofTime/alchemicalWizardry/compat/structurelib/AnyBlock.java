package WayofTime.alchemicalWizardry.compat.structurelib;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.AutoPlaceEnvironment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

public class AnyBlock<T> implements IStructureElement<T> {

    @Override
    public boolean check(Object t, World world, int x, int y, int z) {
        return !world.isAirBlock(x, y, z);
    }

    @Override
    public boolean couldBeValid(Object t, World world, int x, int y, int z, ItemStack trigger) {
        return check(t, world, x, y, z);
    }

    @Override
    public boolean spawnHint(Object o, World world, int x, int y, int z, ItemStack trigger) {
        StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), 14);
        return true;
    }

    @Override
    public boolean placeBlock(Object o, World world, int x, int y, int z, ItemStack trigger) {
        world.setBlock(x, y, z, Blocks.stonebrick, 0, 2);
        return true;
    }

    @Override
    public BlocksToPlace getBlocksToPlace(Object o, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
        return BlocksToPlace.create(Blocks.stonebrick, 0);
    }

    @Override
    public PlaceResult survivalPlaceBlock(Object o, World world, int x, int y, int z, ItemStack trigger,
            AutoPlaceEnvironment env) {
        if (world.provider.dimensionId == Integer.MAX_VALUE) { // Required for the NEI preview to work
            world.setBlock(x, y, z, Blocks.stonebrick, 0, 2);
            return PlaceResult.ACCEPT;
        }
        return PlaceResult.SKIP;
    }
}
