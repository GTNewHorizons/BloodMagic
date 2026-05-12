package WayofTime.alchemicalWizardry.common.demonVillage;

import net.minecraft.nbt.NBTTagCompound;

public class GridSpace {

    public static final int EMPTY = 0;
    public static final int MAIN_PORTAL = 1;
    public static final int ROAD = 3;
    public static final int CROSSROAD = 4;
    public static final int HOUSE = 5;

    private final int yLevel;
    private final int type;

    public GridSpace() {
        this(EMPTY, -1);
    }

    public GridSpace(int type, int yLevel) {
        this.type = type;
        this.yLevel = yLevel;
    }

    public int getYLevel() {
        return this.yLevel;
    }

    public boolean isEmpty() {
        return type == EMPTY;
    }

    public static GridSpace getGridFromTag(NBTTagCompound tag) {
        return new GridSpace(tag.getInteger("type"), tag.getInteger("yLevel"));
    }

    public NBTTagCompound getTag() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("type", type);
        tag.setInteger("yLevel", yLevel);

        return tag;
    }

    public boolean isRoadSegment() {
        return type == ROAD || type == CROSSROAD;
    }

    public boolean isBuilding() {
        return type == HOUSE;
    }
}
