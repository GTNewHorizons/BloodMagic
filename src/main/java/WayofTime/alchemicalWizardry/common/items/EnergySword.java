package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.omega.OmegaParadigm;
import WayofTime.alchemicalWizardry.common.omega.OmegaRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergySword extends ItemSword implements IBindable {

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    private int energyUsed;

    public EnergySword() {
        super(AlchemicalWizardry.bloodBoundToolMaterial);
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setEnergyUsed(50);
        setFull3D();
        setMaxDamage(100);
    }

    public void setEnergyUsed(int i) {
        energyUsed = i;
    }

    public int getEnergyUsed() {
        return this.energyUsed;
    }

    @Override
    public int drainCost() {
        return this.energyUsed;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundSword_activated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundSword_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (IBindable.isActive(stack)) {
            return this.activeIcon;
        } else {
            return this.passiveIcon;
        }
    }

    private OmegaParadigm getOmegaParadigmOfWeilder(EntityPlayer player) {
        return OmegaRegistry.getOmegaParadigmOfWeilder(player);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        boolean isActive = IBindable.isActive(stack);
        if (isActive && !player.worldObj.isRemote) {
            OmegaParadigm parad = this.getOmegaParadigmOfWeilder(player);

            if (parad != null && parad.isPlayerWearingFullSet(player)
                    && !parad.onBoundSwordLeftClickEntity(stack, player, entity)) {
                return true;
            }
        }
        return !isActive;
    }

    @Override
    public boolean hitEntity(ItemStack item, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker instanceof EntityPlayer player && (!IBindable.checkAndSetItemOwner(item, player)
                || !EnergyItems.syphonBatteries(item, player, this.drainCost()))) {
            return false;
        }

        target.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        super.onItemRightClick(item, world, player);

        this.toggle(item, world, player);

        return item;
    }

    @Override
    public int getItemEnchantability() {
        return 30;
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean held) {
        if (!(entity instanceof EntityPlayer player)) {
            return;
        }

        checkPassiveDrain(item, world, player);
        item.setItemDamage(0);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.caution.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.caution.desc2"));
        addBindingInformation(item, tooltip);
    }
}
