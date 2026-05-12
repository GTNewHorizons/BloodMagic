package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.IDemon;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.IHoardDemon;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DaggerOfSacrifice extends Item {

    public DaggerOfSacrifice() {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setFull3D();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DaggerOfSacrifice");
    }

    @Override
    public boolean hitEntity(ItemStack item, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker == null || target == null
                || attacker.worldObj.isRemote
                || (attacker instanceof EntityPlayer
                        && SpellHelper.isFakePlayer(attacker.worldObj, (EntityPlayer) attacker))) {
            return false;
        }

        if ((!(target instanceof EntityMob) && target.isChild()) || target instanceof EntityPlayer
                || target instanceof IBossDisplayData
                || target instanceof IHoardDemon) {
            return false;
        }

        World world = target.worldObj;

        if (target.isDead || target.getHealth() < 0.5f) {
            return false;
        }

        if (target instanceof IDemon demon) {
            demon.setDropCrystal(false);
            this.findAndNotifyAltarOfDemon(world, target);
        }

        int lifeEssence = AlchemicalWizardry.lpPerSactificeCustom.containsKey(target.getClass())
                ? AlchemicalWizardry.lpPerSactificeCustom.get(target.getClass())
                : AlchemicalWizardry.lpPerSacrificeBase;

        if (findAndFillAltar(target.worldObj, target, lifeEssence)) {
            double posX = target.posX;
            double posY = target.posY;
            double posZ = target.posZ;

            for (int i = 0; i < 8; i++) {
                SpellHelper.sendIndexedParticleToAllAround(
                        world,
                        posX,
                        posY,
                        posZ,
                        20,
                        world.provider.dimensionId,
                        1,
                        posX,
                        posY,
                        posZ);
            }

            target.setHealth(-1);
            target.onDeath(DamageSource.generic);
        }

        return false;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.caution.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.caution.desc2"));
    }

    @Override
    public float func_150893_a(ItemStack item, Block block) {
        if (block == Blocks.web) {
            return 15.0F;
        } else {
            Material material = block.getMaterial();
            return material != Material.plants && material != Material.vine
                    && material != Material.coral
                    && material != Material.leaves
                    && material != Material.gourd ? 1.0F : 1.5F;
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put(
                SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
                new AttributeModifier(field_111210_e, "Tool modifier", 1.0d, 0));
        return multimap;
    }

    public boolean findAndNotifyAltarOfDemon(World world, EntityLivingBase sacrifice) {
        int posX = (int) Math.round(sacrifice.posX - 0.5f);
        int posY = (int) sacrifice.posY;
        int posZ = (int) Math.round(sacrifice.posZ - 0.5f);
        IBloodAltar altarEntity = this.getAltar(world, posX, posY, posZ);

        if (altarEntity == null) {
            return false;
        }

        altarEntity.addToDemonBloodDuration(50);

        return true;
    }

    public boolean findAndFillAltar(World world, EntityLivingBase sacrifice, int amount) {
        int posX = (int) Math.round(sacrifice.posX - 0.5f);
        int posY = (int) sacrifice.posY;
        int posZ = (int) Math.round(sacrifice.posZ - 0.5f);
        IBloodAltar altarEntity = this.getAltar(world, posX, posY, posZ);

        if (altarEntity == null) {
            return false;
        }

        altarEntity.sacrificialDaggerCall(amount, true);
        altarEntity.startCycle();
        return true;
    }

    public IBloodAltar getAltar(World world, int x, int y, int z) {
        TileEntity tileEntity;

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 1; k++) {
                    tileEntity = world.getTileEntity(i + x, k + y, j + z);

                    if ((tileEntity instanceof IBloodAltar)) {
                        return (IBloodAltar) tileEntity;
                    }
                }
            }
        }

        return null;
    }
}
