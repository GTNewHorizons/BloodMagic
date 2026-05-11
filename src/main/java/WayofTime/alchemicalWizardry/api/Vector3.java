package WayofTime.alchemicalWizardry.api;

import javax.annotation.Nonnull;

public record Vector3(int x, int y, int z) {

    public Vector3 add(Vector3 vec) {
        return new Vector3(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    @Override
    @Nonnull
    public String toString() {
        return "V3({" + x + "}, {" + y + "}, {" + z + "})";
    }

}
