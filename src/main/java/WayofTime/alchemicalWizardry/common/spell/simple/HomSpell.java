package WayofTime.alchemicalWizardry.common.spell.simple;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class HomSpell implements ISimpleSpell {

    private int offensiveRangedEnergy;
    private int offensiveMeleeEnergy;
    private int defensiveEnergy;
    private int environmentalEnergy;

    public HomSpell() {
        // super(id);
        // this.setMaxStackSize(1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public abstract ItemStack onOffensiveRangedRightClick(ItemStack item, World world, EntityPlayer player);

    @Override
    public abstract ItemStack onOffensiveMeleeRightClick(ItemStack item, World world, EntityPlayer player);

    @Override
    public abstract ItemStack onDefensiveRightClick(ItemStack item, World world, EntityPlayer player);

    @Override
    public abstract ItemStack onEnvironmentalRightClick(ItemStack item, World world, EntityPlayer player);

    public int getOffensiveRangedEnergy() {
        return offensiveRangedEnergy;
    }

    public int getOffensiveMeleeEnergy() {
        return offensiveMeleeEnergy;
    }

    public int getDefensiveEnergy() {
        return defensiveEnergy;
    }

    public int getEnvironmentalEnergy() {
        return environmentalEnergy;
    }

    public void setEnergies(int offensiveRanged, int offensiveMelee, int defensive, int environmental) {
        this.offensiveRangedEnergy = offensiveRanged;
        this.offensiveMeleeEnergy = offensiveMelee;
        this.defensiveEnergy = defensive;
        this.environmentalEnergy = environmental;
    }
}
