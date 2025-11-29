package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.IFillingAgent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WeakFillingAgent extends Item implements IFillingAgent {

    public WeakFillingAgent() {
        super();
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public int getFilledAmountForPotionNumber(int potionEffects) {
        Random rand = new Random();

        if (potionEffects == 0) {
            return 8;
        }

        if (potionEffects == 1 || potionEffects == 2) {
            if (rand.nextFloat() > 0.5f) {
                return (4 - potionEffects);
            } else {
                return 3 - potionEffects;
            }
        }

        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WeakFillingAgent");
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List,
            boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.usedinalchemy"));
    }
}
