package WayofTime.alchemicalWizardry.api.alchemy.energy;

public record Reagent(String name, int red, int green, int blue, int intensity) {

    public static final int REAGENT_SIZE = 1000;

}
