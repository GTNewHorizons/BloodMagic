package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonBuilding {

    public static final int BUILDING_HOUSE = 0;
    public static final int BUILDING_PORTAL = 1;

    public BuildingSchematic schematic;
    public GridSpaceHolder area;
    public int buildingTier;
    public int buildingType;
    public Int3 doorGridSpace;

    public DemonBuilding(BuildingSchematic schematic) {
        this.schematic = schematic;
        this.buildingType = schematic.buildingType;
        this.buildingTier = schematic.buildingTier;
        this.area = this.createGSHForSchematic(schematic);
        this.doorGridSpace = schematic.getGridSpotOfDoor();
    }

    public String getName() {
        return schematic.getName();
    }

    public boolean isValid(GridSpaceHolder master, int gridX, int gridZ, ForgeDirection dir) {
        return area.doesContainAll(master, gridX, gridZ, dir);
    }

    public void buildAll(TEDemonPortal teDemonPortal, World world, int xCoord, int yCoord, int zCoord,
            ForgeDirection dir, boolean populateInventories) {
        schematic.buildAll(teDemonPortal, world, xCoord, yCoord, zCoord, dir, populateInventories);
    }

    public void setAllGridSpaces(int xInit, int zInit, int yLevel, ForgeDirection dir, int type,
            GridSpaceHolder master) {
        area.setAllGridSpaces(xInit, zInit, yLevel, dir, type, master);
    }

    public GridSpaceHolder createGSHForSchematic(BuildingSchematic scheme) {
        switch (this.buildingType) {
            case DemonBuilding.BUILDING_HOUSE:
                return scheme.createGSH();
            case DemonBuilding.BUILDING_PORTAL:
        }
        return scheme.createGSH();
    }

    public Int3 getDoorSpace(ForgeDirection dir) {
        return switch (dir) {
            case SOUTH -> new Int3(-doorGridSpace.x(), doorGridSpace.y(), -doorGridSpace.z());
            case WEST -> new Int3(doorGridSpace.z(), doorGridSpace.y(), -doorGridSpace.x());
            case EAST -> new Int3(-doorGridSpace.z(), doorGridSpace.y(), doorGridSpace.x());
            default -> new Int3(doorGridSpace.x(), doorGridSpace.y(), doorGridSpace.z());
        };
    }

    public Int3 getGridOffsetFromRoad(ForgeDirection sideOfRoad, int yLevel) {
        Int3 doorSpace = this.getDoorSpace(sideOfRoad);
        int x = doorSpace.x();
        int z = doorSpace.z();

        switch (sideOfRoad) {
            case SOUTH -> z++;
            case EAST -> x++;
            case WEST -> x--;
            default -> z--;
        }

        return new Int3(x, yLevel, z);
    }

    public void destroyAllInField(World world, int xCoord, int yCoord, int zCoord, ForgeDirection dir) {
        schematic.destroyAllInField(world, xCoord, yCoord, zCoord, dir);
    }

    public int getNumberOfGridSpaces() {
        return area.getNumberOfGridSpaces();
    }
}
