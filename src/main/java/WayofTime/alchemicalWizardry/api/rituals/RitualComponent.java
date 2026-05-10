package WayofTime.alchemicalWizardry.api.rituals;

public record RitualComponent(int x, int y, int z, int stoneType) {

    public static final int BLANK = 0;
    public static final int WATER = 1;
    public static final int FIRE = 2;
    public static final int EARTH = 3;
    public static final int AIR = 4;
    public static final int DUSK = 5;
    public static final int DAWN = 6;

    public int getX(int direction) {
        return switch (direction) {
            case 2 -> -this.z();
            case 3 -> -this.x();
            case 4 -> this.z();
            default -> this.x();
        };
    }

    public int getZ(int direction) {
        return switch (direction) {
            case 2 -> this.x();
            case 3 -> -this.z();
            case 4 -> -this.x();
            default -> this.z();
        };
    }
}
