package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ActivationCrystal extends EnergyItems {

    private static final String[] ACTIVATION_CRYSTAL_NAMES = new String[] { "Weak", "Awakened", "Creative" };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ActivationCrystal() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(100);
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[ACTIVATION_CRYSTAL_NAMES.length];

        for (int i = 0; i < ACTIVATION_CRYSTAL_NAMES.length; ++i) {
            icons[i] = iconRegister
                    .registerIcon("AlchemicalWizardry:" + "activationCrystal" + ACTIVATION_CRYSTAL_NAMES[i]);
        }
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        switch (item.getItemDamage()) {
            case 0 -> tooltip.add(StatCollector.translateToLocal("tooltip.activationcrystal.lowlevelrituals"));
            case 1 -> tooltip.add(StatCollector.translateToLocal("tooltip.activationcrystal.powerfulrituals"));
            case 2 -> tooltip.add(StatCollector.translateToLocal("tooltip.activationcrystal.creativeonly"));
        }
        addBindingInformation(item, tooltip);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        IBindable.checkAndSetItemOwner(item, player);
        return item;
    }

    public int getCrystalLevel(ItemStack itemStack) {
        return itemStack.getItemDamage() > 1 ? Integer.MAX_VALUE : itemStack.getItemDamage() + 1;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        // This is what will do all the localisation things on the alchemy components so you dont have to set it :D
        int meta = MathHelper.clamp_int(itemStack.getItemDamage(), 0, ACTIVATION_CRYSTAL_NAMES.length - 1);
        return ("item.activationCrystal" + ACTIVATION_CRYSTAL_NAMES[meta]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        int j = MathHelper.clamp_int(meta, 0, ACTIVATION_CRYSTAL_NAMES.length - 1);
        return icons[j];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list) {
        for (int meta = 0; meta < ACTIVATION_CRYSTAL_NAMES.length; ++meta) {
            list.add(new ItemStack(id, 1, meta));
        }
    }
}
