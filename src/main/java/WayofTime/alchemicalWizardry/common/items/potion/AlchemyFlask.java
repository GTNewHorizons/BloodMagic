package WayofTime.alchemicalWizardry.common.items.potion;

import static WayofTime.alchemicalWizardry.AlchemicalWizardry.allowPotionRepair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import com.google.common.collect.HashMultimap;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyPotionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AlchemyFlask extends Item {

    public AlchemyFlask() {
        super();
        this.setMaxDamage(8);
        this.setMaxStackSize(1);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:PotionFlask");
    }

    public static ArrayList<AlchemyPotionHelper> getEffects(ItemStack item) {
        if (!item.hasTagCompound() || !item.getTagCompound().hasKey("CustomFlaskEffects")) {
            return null;
        }
        ArrayList<AlchemyPotionHelper> arraylist = new ArrayList<>();
        NBTTagList nbttaglist = item.getTagCompound().getTagList("CustomFlaskEffects", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            arraylist.add(AlchemyPotionHelper.readEffectFromNBT(nbttagcompound));
        }
        return arraylist;
    }

    public static ArrayList<PotionEffect> getPotionEffects(ItemStack item) {
        ArrayList<AlchemyPotionHelper> list = AlchemyFlask.getEffects(item);

        if (list == null) {
            return null;
        }
        ArrayList<PotionEffect> newList = new ArrayList<>();

        for (AlchemyPotionHelper aph : list) {
            newList.add(aph.getPotionEffect());
        }

        return newList;
    }

    public static void setEffects(ItemStack item, List<AlchemyPotionHelper> list) {
        NBTTagCompound itemTag = item.getTagCompound();

        if (itemTag == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagList nbttaglist = new NBTTagList();

        for (AlchemyPotionHelper aph : list) {
            nbttaglist.appendTag(AlchemyPotionHelper.setEffectToNBT(aph));
        }

        item.getTagCompound().setTag("CustomFlaskEffects", nbttaglist);
    }

    @Override
    public ItemStack onEaten(ItemStack item, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            item.setItemDamage(item.getItemDamage() + 1);
        }

        if (world.isRemote) {
            return item;
        }

        ArrayList<AlchemyPotionHelper> list = getEffects(item);
        if (list == null) {
            return item;
        }
        for (AlchemyPotionHelper aph : list) {
            PotionEffect pe = aph.getPotionEffect();

            if (pe != null) {
                player.addPotionEffect(pe);
            }
        }

        return item;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack item) {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack item) {
        if (this.isPotionThrowable(item)) {
            return EnumAction.none;
        }

        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (item.getItemDamage() < item.getMaxDamage()) {
            if (this.isPotionThrowable(item)) {
                if (!world.isRemote) {
                    EntityPotion entityPotion = this.getEntityPotion(item, world, player);

                    if (entityPotion != null) {
                        float velocityChange = 2.0f;
                        entityPotion.motionX *= velocityChange;
                        entityPotion.motionY *= velocityChange;
                        entityPotion.motionZ *= velocityChange;
                        world.spawnEntityInWorld(entityPotion);
                        item.setItemDamage(item.getItemDamage() + 1);
                    }
                }

                return item;
            }

            player.setItemInUse(item, this.getMaxItemUseDuration(item));
        }
        return item;
    }

    public void setConcentrationOfPotion(ItemStack item, int potionID, int concentration) {
        ArrayList<AlchemyPotionHelper> list = getEffects(item);
        if (list != null) {
            for (AlchemyPotionHelper aph : list) {
                if (aph.getPotionID() == potionID) {
                    aph.setConcentration(concentration);
                    break;
                }
            }
            setEffects(item, list);
        }
    }

    public void setDurationFactorOfPotion(ItemStack item, int potionID, int durationFactor) {
        ArrayList<AlchemyPotionHelper> list = getEffects(item);
        if (list != null) {
            for (AlchemyPotionHelper aph : list) {
                if (aph.getPotionID() == potionID) {
                    aph.setDurationFactor(durationFactor);
                    break;
                }
            }
            setEffects(item, list);
        }
    }

    public int getNumberOfPotionEffects(ItemStack item) {
        ArrayList<AlchemyPotionHelper> effects = getEffects(item);
        if (effects != null) {
            return effects.size();
        } else {
            return 0;
        }
    }

    public boolean addPotionEffect(ItemStack item, int potionID, int tickDuration) {
        ArrayList<AlchemyPotionHelper> list = getEffects(item);
        if (list != null) {
            for (AlchemyPotionHelper aph : list) {
                if (aph.getPotionID() == potionID) {
                    return false;
                }
            }
        } else {
            list = new ArrayList<>();
        }
        list.add(new AlchemyPotionHelper(potionID, tickDuration, 0, 0));
        setEffects(item, list);
        return true;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(
                EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemyflask.swigsleft")
                        + " "
                        + (item.getMaxDamage() - item.getItemDamage())
                        + "/"
                        + item.getMaxDamage());

        if (this.isPotionThrowable(item)) {
            tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("tooltip.alchemyflask.caution"));
        }

        List<PotionEffect> effects = AlchemyFlask.getPotionEffects(item);
        HashMultimap<String, AttributeModifier> hashmultimap = HashMultimap.create();

        if (effects != null && !effects.isEmpty()) {
            for (PotionEffect potioneffect : effects) {
                String s = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                Map<IAttribute, AttributeModifier> map = potion.func_111186_k();

                if (map != null && !map.isEmpty()) {
                    for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(
                                attributemodifier.getName(),
                                potion.func_111183_a(potioneffect.getAmplifier(), attributemodifier),
                                attributemodifier.getOperation());
                        hashmultimap.put(entry.getKey().getAttributeUnlocalizedName(), attributemodifier1);
                    }
                }

                if (potioneffect.getAmplifier() > 0) {
                    s = s + " "
                            + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }

                if (potioneffect.getDuration() > 20) {
                    s = s + " (" + Potion.getDurationString(potioneffect) + ")";
                }

                if (potion.isBadEffect()) {
                    tooltip.add(EnumChatFormatting.RED + s);
                } else {
                    tooltip.add(EnumChatFormatting.GRAY + s);
                }
            }
        } else {
            String s1 = StatCollector.translateToLocal("potion.empty").trim();
            tooltip.add(EnumChatFormatting.GRAY + s1);
        }

        if (!hashmultimap.isEmpty()) {
            tooltip.add("");
            tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));

            for (Entry<String, AttributeModifier> stringAttributeModifierEntry : hashmultimap.entries()) {
                AttributeModifier attributemodifier2 = stringAttributeModifierEntry.getValue();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
                    d1 = attributemodifier2.getAmount();
                } else {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D) {
                    tooltip.add(
                            EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted(
                                    "attribute.modifier.plus." + attributemodifier2.getOperation(),
                                    new Object[] { ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal(
                                            "attribute.name." + stringAttributeModifierEntry.getKey()) }));
                } else if (d0 < 0.0D) {
                    d1 *= -1.0D;
                    tooltip.add(
                            EnumChatFormatting.RED + StatCollector.translateToLocalFormatted(
                                    "attribute.modifier.take." + attributemodifier2.getOperation(),
                                    new Object[] { ItemStack.field_111284_a.format(d1), StatCollector.translateToLocal(
                                            "attribute.name." + stringAttributeModifierEntry.getKey()) }));
                }
            }
        }
    }

    public boolean isPotionThrowable(ItemStack item) {
        return item.hasTagCompound() && item.getTagCompound().getBoolean("throwable");
    }

    public void setIsPotionThrowable(boolean flag, ItemStack item) {
        if (!item.hasTagCompound()) {
            item.setTagCompound(new NBTTagCompound());
        }

        item.getTagCompound().setBoolean("throwable", flag);
    }

    public EntityPotion getEntityPotion(ItemStack item, World worldObj, EntityLivingBase entityLivingBase) {
        ItemStack potionStack = new ItemStack(Items.potionitem, 1, 0);
        potionStack.setTagCompound(new NBTTagCompound());
        ArrayList<PotionEffect> potionList = getPotionEffects(item);

        if (potionList == null) {
            return null;
        }
        NBTTagList nbttaglist = new NBTTagList();
        for (PotionEffect pe : potionList) {
            NBTTagCompound d = new NBTTagCompound();
            d.setByte("Id", (byte) pe.getPotionID());
            d.setByte("Amplifier", (byte) pe.getAmplifier());
            d.setInteger("Duration", pe.getDuration());
            d.setBoolean("Ambient", pe.getIsAmbient());
            nbttaglist.appendTag(d);
        }
        potionStack.getTagCompound().setTag("CustomPotionEffects", nbttaglist);
        return new EntityPotion(worldObj, entityLivingBase, potionStack);
    }

    @Override
    public boolean isRepairable() {
        if (allowPotionRepair) return super.isRepairable();
        return false;
    }
}
