package WayofTime.alchemicalWizardry.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import com.google.common.collect.HashMultiset;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.common.ItemType;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BoundAxe extends ItemAxe implements IBindable {

    public float efficiencyOnProperMaterial = 12.0F;
    public int rightClickCost = 10000;

    @SideOnly(Side.CLIENT)
    private IIcon activeIcon;

    @SideOnly(Side.CLIENT)
    private IIcon passiveIcon;

    private int energyUsed;

    public BoundAxe() {
        super(AlchemicalWizardry.bloodBoundToolMaterial);
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setEnergyUsed(5);
        this.setHarvestLevel("axe", 7);
    }

    public void setEnergyUsed(int i) {
        energyUsed = i;
    }

    @Override
    public int drainCost() {
        return this.energyUsed;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.boundaxe.desc"));
        addBindingInformation(item, tooltip);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundAxe_activated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundAxe_activated");
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

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (checkRightClick(item, world, player)) {
            return item;
        }

        Vec3 blockVec = SpellHelper.getEntityBlockVector(player);
        int posX = (int) (blockVec.xCoord);
        int posY = (int) (blockVec.yCoord);
        int posZ = (int) (blockVec.zCoord);
        boolean silkTouch = EnchantmentHelper.getSilkTouchModifier(player);
        int fortuneLvl = EnchantmentHelper.getFortuneModifier(player);

        HashMultiset<ItemType> dropMultiset = HashMultiset.create();

        for (int i = -5; i <= 5; i++) {
            for (int j = 0; j <= 10; j++) {
                for (int k = -5; k <= 5; k++) {
                    int x = posX + i;
                    int y = posY + j;
                    int z = posZ + k;
                    Block block = world.getBlock(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);

                    if (block == null || block.getBlockHardness(world, x, y, z) == -1
                            || !world.canMineBlock(player, x, y, z)) {
                        continue;
                    }

                    // getStrVsBlock
                    if ((!ForgeHooks.isToolEffective(item, block, meta) && func_150893_a(item, block) <= 1f
                            && !(block instanceof BlockLeavesBase))
                            || BoundPickaxe.isBreakDenied(world, x, y, z, block, meta, player)) {
                        continue;
                    }
                    if (silkTouch && block.canSilkHarvest(world, player, x, y, z, meta)) {
                        dropMultiset.add(new ItemType(block, meta));
                    } else {
                        ArrayList<ItemStack> itemDropList = block.getDrops(world, x, y, z, meta, fortuneLvl);

                        if (itemDropList != null) {
                            for (ItemStack stack : itemDropList)
                                dropMultiset.add(ItemType.fromStack(stack), stack.stackSize);
                        }
                    }

                    world.setBlockToAir(x, y, z);
                }
            }
        }

        BoundPickaxe.dropMultisetStacks(dropMultiset, world, posX, posY + player.getEyeHeight(), posZ);

        return item;
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean held) {
        if (!(entity instanceof EntityPlayer player)) {
            return;
        }

        checkPassiveDrain(item, world, player);
        item.setItemDamage(0);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float func_150893_a(ItemStack item, Block block) {
        if (!IBindable.isActive(item)) {
            return 0.0F;
        }

        return super.func_150893_a(item, block);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack item, EntityLivingBase target, EntityLivingBase attacker) {
        return IBindable.isActive(item);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack item, World world, Block block, int x, int y, int z,
            EntityLivingBase user) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 30;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (!IBindable.isActive(stack)) {
            return 0.0F;
        }

        if (ForgeHooks.isToolEffective(stack, block, meta)) {
            return efficiencyOnProperMaterial;
        }

        return func_150893_a(stack, block);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return !IBindable.isActive(stack);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return IBindable.isActive(stack) ? super.getHarvestLevel(stack, toolClass) : -1;
    }

    @Override
    public boolean isBoundTool() {
        return true;
    }

    @Override
    public int rightClickCost() {
        return rightClickCost;
    }
}
