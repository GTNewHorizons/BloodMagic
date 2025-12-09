package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ICatalyst;

public class LengtheningCatalyst extends Item implements ICatalyst {

    private int catalystStrength;

    public LengtheningCatalyst(int catalystStrength) {
        this.catalystStrength = catalystStrength;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public int getCatalystLevel() {
        return catalystStrength;
    }

    @Override
    public boolean isConcentration() {
        return false;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List,
            boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.usedinalchemy"));
    }
}
