package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OrbOfTesting extends EnergyItems {

    public OrbOfTesting() {
        super();
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName("orbOfTesting");
        setMaxDamage(100);
        setFull3D();
        this.setEnergyUsed(100);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:Untitled");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!player.shouldHeal()) {
            return item;
        }

        if (syphonBatteries(item, player, this.getEnergyUsed())) {
            player.heal(1);
        }

        return item;
    }
}
