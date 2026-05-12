package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IHolding;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfEnderSeverance extends EnergyItems implements IHolding, ISigil {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    public SigilOfEnderSeverance() {
        super();
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilEnderSeveranceCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.sigilofenderseverance.desc"));
        addBindingInformation(item, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfSeverance_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfSeverance_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SigilOfSeverance_deactivated");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (IBindable.isActive(stack)) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        if (meta == 1) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!IBindable.checkAndSetItemOwner(item, player) || player.isSneaking()) {
            return item;
        }

        toggleSigil(item, world, player);

        return item;
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean held) {
        if (!(entity instanceof EntityPlayer player)) {
            return;
        }

        if (IBindable.isActive(item)) {
            List<Entity> list = SpellHelper.getEntitiesInRange(world, entity.posX, entity.posY, entity.posZ, 4.5, 4.5);
            for (Entity e : list) {
                if (!e.equals(entity) && e instanceof EntityLiving living) {
                    living.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionPlanarBinding.id, 5, 0));
                }
            }
        }

        checkPassiveDrain(item, world, player);
    }
}
