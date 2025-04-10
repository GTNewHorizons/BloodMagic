package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        switch (par1ItemStack.getItemDamage()) {
            case 0: {
                par3List.add(StatCollector.translateToLocal("tooltip.activationcrystal.lowlevelrituals"));
                break;
            }

            case 1: {
                par3List.add(StatCollector.translateToLocal("tooltip.activationcrystal.powerfulrituals"));

                if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

                    if (recipe != null) {
                        par3List.add(
                                EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemy.recipe"));

                        for (ItemStack item : recipe) {
                            if (item != null) {
                                par3List.add("" + item.getDisplayName());
                            }
                        }
                    }
                } else {
                    par3List.add(
                            "-" + StatCollector.translateToLocal("tooltip.alchemy.press")
                                    + " "
                                    + EnumChatFormatting.BLUE
                                    + StatCollector.translateToLocal("tooltip.alchemy.shift")
                                    + EnumChatFormatting.GRAY
                                    + " "
                                    + StatCollector.translateToLocal("tooltip.alchemy.forrecipe")
                                    + "-");
                }

                break;
            }

            case 2: {
                par3List.add(StatCollector.translateToLocal("tooltip.activationcrystal.creativeonly"));

                break;
            }
        }

        addBindingInformation(par1ItemStack, par3List);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        return par1ItemStack;
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
    public void getSubItems(Item id, CreativeTabs creativeTab, List list) {
        for (int meta = 0; meta < ACTIVATION_CRYSTAL_NAMES.length; ++meta) {
            list.add(new ItemStack(id, 1, meta));
        }
    }
}
