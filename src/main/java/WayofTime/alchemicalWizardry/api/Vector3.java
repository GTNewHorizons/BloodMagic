package WayofTime.alchemicalWizardry.api;

import javax.annotation.Nonnull;

/*
 * Created in Scala by Alex-Hawks Translated and implemented by Arcaratus
 */
public record Vector3(int x, int y, int z) {

    public Vector3 add(Vector3 vec) {
        return new Vector3(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    @Override
    @Nonnull
    public String toString() {
        return "V3(" + x + "}, " + y + "}," + z + "})";
    }

    @Override
    public boolean equals(Object object) {
        return object == this || object instanceof Vector3(int x1, int y1, int z1)
                && this.x == x1
                && this.y == y1
                && this.z == z1;
    }

}
