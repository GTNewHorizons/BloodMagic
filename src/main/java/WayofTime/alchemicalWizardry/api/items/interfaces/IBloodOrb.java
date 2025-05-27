package WayofTime.alchemicalWizardry.api.items.interfaces;

public interface IBloodOrb {

    int getMaxEssence();

    int getOrbLevel();

    default boolean isFilledForFree() {
        return false;
    }
}
