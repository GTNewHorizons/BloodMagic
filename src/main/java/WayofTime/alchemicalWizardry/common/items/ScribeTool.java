package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ScribeTool extends EnergyItems {

    private final int meta;
    private final String iconName;

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public ScribeTool(int inkType) {
        this(inkType, "unknown");
    }

    public ScribeTool(int inkType, String iconName) {
        super();
        this.meta = inkType;
        this.iconName = iconName;
        setMaxStackSize(1);
        setMaxDamage(10);
        setEnergyUsed(10);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    public int getType() {
        return this.meta;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean sneaking) {
        list.add(StatCollector.translateToLocal("tooltip.scribetool.desc"));
        addBindingInformation(stack, list);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (IBindable.checkAndSetItemOwner(stack, player) && stack.getItemDamage() > 0
                && EnergyItems.syphonBatteries(stack, player, getEnergyUsed())) {
            stack.setItemDamage(stack.getItemDamage() - 1);
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icon = register.registerIcon("AlchemicalWizardry:" + iconName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.icon;
    }
}
