package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

public class AltarUpgradeComponent {

    private int speedUpgrades = 0;
    private int efficiencyUpgrades = 0;
    private int sacrificeUpgrades = 0;
    private int selfSacrificeUpgrades = 0;
    private int displacementUpgrades = 0;
    private int altarCapacitiveUpgrades = 0;
    private int orbCapacitiveUpgrades = 0;
    private int betterCapacitiveUpgrades = 0;
    private int accelerationUpgrades = 0;
    private int quicknessUpgrades = 0;

    public void addSpeedUpgrade() {
        speedUpgrades++;
    }

    public void addEfficiencyUpgrade() {
        efficiencyUpgrades++;
    }

    public void addSacrificeUpgrade() {
        sacrificeUpgrades++;
    }

    public void addSelfSacrificeUpgrade() {
        selfSacrificeUpgrades++;
    }

    public void addDisplacementUpgrade() {
        displacementUpgrades++;
    }

    public void addaltarCapacitiveUpgrade() {
        altarCapacitiveUpgrades++;
    }

    public void addorbCapacitiveUpgrade() {
        orbCapacitiveUpgrades++;
    }

    public void addBetterCapacitiveUpgrade() {
        betterCapacitiveUpgrades++;
    }

    public void addAccelerationUpgrade() {
        accelerationUpgrades++;
    }

    public void addQuicknessUpgrade() {
        quicknessUpgrades++;
    }

    public int getSpeedUpgrades() {
        return speedUpgrades;
    }

    public int getEfficiencyUpgrades() {
        return efficiencyUpgrades;
    }

    public int getSacrificeUpgrades() {
        return sacrificeUpgrades;
    }

    public int getSelfSacrificeUpgrades() {
        return selfSacrificeUpgrades;
    }

    public int getDisplacementUpgrades() {
        return displacementUpgrades;
    }

    public int getAltarCapacitiveUpgrades() {
        return this.altarCapacitiveUpgrades;
    }

    public int getOrbCapacitiveUpgrades() {
        return this.orbCapacitiveUpgrades;
    }

    public int getBetterCapacitiveUpgrades() {
        return this.betterCapacitiveUpgrades;
    }

    public int getAccelerationUpgrades() {
        return this.accelerationUpgrades;
    }

    public int getQuicknessUpgrades() {
        return this.quicknessUpgrades;
    }
}
