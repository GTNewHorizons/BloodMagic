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

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAlchemyBase extends Item {

    private static final String[] ITEM_NAMES = new String[] { "Offensa", "Praesidium", "OrbisTerrae",
            "StrengthenedCatalyst", "ConcentratedCatalyst", "FracturedBone", "Virtus", "Reductus", "Potentia" };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public ItemAlchemyBase() {
        super();
        this.maxStackSize = 64;
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.hasSubtypes = true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[ITEM_NAMES.length];

        for (int i = 0; i < ITEM_NAMES.length; ++i) {
            icons[i] = iconRegister.registerIcon("AlchemicalWizardry:" + "baseAlchemyItem" + ITEM_NAMES[i]);
        }
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List,
            boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.alchemy.usedinalchemy"));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = MathHelper.clamp_int(itemStack.getItemDamage(), 0, ITEM_NAMES.length - 1);
        return ("item.bloodMagicAlchemyItem." + ITEM_NAMES[meta]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        int j = MathHelper.clamp_int(meta, 0, ITEM_NAMES.length - 1);
        return icons[j];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list) {
        for (int meta = 0; meta < ITEM_NAMES.length; ++meta) {
            list.add(new ItemStack(id, 1, meta));
        }
    }
}
