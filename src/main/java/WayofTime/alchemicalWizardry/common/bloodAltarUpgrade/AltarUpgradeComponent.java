package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

public class AltarUpgradeComponent
{
    private int speedUpgrades;
    private int efficiencyUpgrades;
    private int sacrificeUpgrades;
    private int selfSacrificeUpgrades;
    private int displacementUpgrades;
    private int altarCapacitiveUpgrades;
    private int orbCapacitiveUpgrades;
    private int betterCapacitiveUpgrades;
    private int accelerationUpgrades;
    private int superSpeedUpgrades;
    private int ultraSpeedUpgrades;
    private int superSacrificeUpgrades;
    private int ultraSacrificeUpgrades;
    private int superDisplacementUpgrades;
    private int ultraDisplacementUpgrades;

    public AltarUpgradeComponent()
    {
        speedUpgrades = 0;
        efficiencyUpgrades = 0;
        sacrificeUpgrades = 0;
        selfSacrificeUpgrades = 0;
        displacementUpgrades = 0;
        altarCapacitiveUpgrades = 0;
        orbCapacitiveUpgrades = 0;
        betterCapacitiveUpgrades = 0;
        accelerationUpgrades = 0;
        superSpeedUpgrades = 0;
        ultraSpeedUpgrades = 0;
        superSacrificeUpgrades = 0;
        ultraSacrificeUpgrades = 0;
        superDisplacementUpgrades = 0;
        ultraDisplacementUpgrades = 0;
    }

    public void addSpeedUpgrade()
    {
        speedUpgrades++;
    }

    public void addEfficiencyUpgrade()
    {
        efficiencyUpgrades++;
    }

    public void addSacrificeUpgrade()
    {
        sacrificeUpgrades++;
    }

    public void addSelfSacrificeUpgrade()
    {
        selfSacrificeUpgrades++;
    }

    public void addDisplacementUpgrade()
    {
        displacementUpgrades++;
    }

    public void addaltarCapacitiveUpgrade()
    {
        altarCapacitiveUpgrades++;
    }

    public void addorbCapacitiveUpgrade()
    {
        orbCapacitiveUpgrades++;
    }

    public void addBetterCapacitiveUpgrade()
    {
        betterCapacitiveUpgrades++;
    }
    
    public void addAccelerationUpgrade()
    {
    	accelerationUpgrades++;
    }

    public void addSuperSpeedUpgrade()
    {
    	superSpeedUpgrades++;
    }
    
    public void addUltraSpeedUpgrade()
    {
    	ultraSpeedUpgrades++;
    }
    
    public void addSuperSacrificeUpgrade()
    {
    	superSacrificeUpgrades++;
    }
    
    public void addUltraSacrificeUpgrade()
    {
    	ultraSacrificeUpgrades++;
    }
    
    public void addSuperDisplacementUpgrade()
    {
    	superDisplacementUpgrades++;
    }
    
    public void addUltraDisplacementUpgrade()
    {
    	ultraDisplacementUpgrades++;
    }
     
    public int getSpeedUpgrades()
    {
        return speedUpgrades;
    }

    public int getEfficiencyUpgrades()
    {
        return efficiencyUpgrades;
    }

    public int getSacrificeUpgrades()
    {
        return sacrificeUpgrades;
    }

    public int getSelfSacrificeUpgrades()
    {
        return selfSacrificeUpgrades;
    }

    public int getDisplacementUpgrades()
    {
        return displacementUpgrades;
    }

    public int getAltarCapacitiveUpgrades()
    {
        return this.altarCapacitiveUpgrades;
    }

    public int getOrbCapacitiveUpgrades()
    {
        return this.orbCapacitiveUpgrades;
    }

    public int getBetterCapacitiveUpgrades()
    {
        return this.betterCapacitiveUpgrades;
    }
    
    public int getAccelerationUpgrades()
    {
    	return this.accelerationUpgrades;
    }
    
    public int getSuperSpeedUpgrades()
    {
    	return this.superSpeedUpgrades;
    }
    
    public int getUltraSpeedUpgrades()
    {
    	return this.ultraSpeedUpgrades;
    }
    
    public int getSuperSacrificeUpgrades()
    {
    	return this.superSacrificeUpgrades;
    }
    
    public int getUltraSacrificeUpgrades()
    {
    	return this.ultraSacrificeUpgrades;
    }
    
    public int getSuperDisplacementUpgrades()
    {
    	return this.superDisplacementUpgrades;
    }
    
    public int getUltraDisplacementUpgrades()
    {
    	return this.ultraDisplacementUpgrades;
    }
}
