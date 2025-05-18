package WayofTime.alchemicalWizardry.common.items.routing;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitDefault;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitGlobal;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitIgnMeta;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitMatchNBT;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OutputRoutingFocus extends RoutingFocus implements ILimitedRoutingFocus {

    IIcon modItemIcon;
    IIcon ignMetaIcon;
    IIcon matchNBTIcon;
    IIcon globalIcon;

    public OutputRoutingFocus() {
        super();
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List,
            boolean par4) {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        if (!(par1ItemStack.getItem() instanceof OutputRoutingFocus focus)) return;
        par3List.addAll(focus.getLogic(par1ItemStack).getDescription());

        if (!(par1ItemStack.getTagCompound() == null)) {
            int limit = this.getRoutingFocusLimit(par1ItemStack);
            if (limit > 0) {
                par3List.add(StatCollector.translateToLocal("tooltip.routingFocus.limit") + " " + limit);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocus");
        this.modItemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusModItems");
        this.ignMetaIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusIgnMeta");
        this.matchNBTIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusMatchNBT");
        this.globalIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusGlobal");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return switch (damage) {
            case 1 -> this.modItemIcon;
            case 2 -> this.ignMetaIcon;
            case 3 -> this.matchNBTIcon;
            case 4 -> this.globalIcon;
            default -> this.itemIcon;
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list) {
        for (int meta = 0; meta < 5; ++meta) {
            list.add(new ItemStack(id, 1, meta));
        }
    }

    @Override
    public RoutingFocusLogic getLogic(ItemStack itemStack) {
        if (itemStack != null) {
            return switch (itemStack.getItemDamage()) {
                case 0 -> new RoutingFocusLogicLimitDefault(itemStack);
                case 1 -> new RoutingFocusLogicLimitModItems(itemStack);
                case 2 -> new RoutingFocusLogicLimitIgnMeta(itemStack);
                case 3 -> new RoutingFocusLogicLimitMatchNBT(itemStack);
                case 4 -> new RoutingFocusLogicLimitGlobal(itemStack);
                default -> new RoutingFocusLogic();
            };
        }

        return new RoutingFocusLogic();
    }

    public int getDefaultStackLimit(int damage) {
        return 0;
    }

    public int getRoutingFocusLimit(ItemStack itemStack) {
        if (!(itemStack.getTagCompound() == null)) {
            return itemStack.getTagCompound().getInteger("stackLimit");
        } else {
            return getDefaultStackLimit(itemStack.getItemDamage());
        }
    }

    public void setRoutingFocusLimit(ItemStack itemStack, int amt) {
        if ((itemStack.getTagCompound() == null)) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.getTagCompound().setInteger("stackLimit", amt);
    }
}
