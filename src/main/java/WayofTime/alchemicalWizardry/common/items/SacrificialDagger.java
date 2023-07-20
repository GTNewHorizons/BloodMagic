package WayofTime.alchemicalWizardry.common.items;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.event.SacrificeKnifeUsedEvent;
import WayofTime.alchemicalWizardry.api.sacrifice.DamageSourceBloodMagic;
import WayofTime.alchemicalWizardry.api.sacrifice.PlayerSacrificeHandler;
import WayofTime.alchemicalWizardry.api.tile.IBloodAltar;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SacrificialDagger extends Item {

    public SacrificialDagger() {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        setFull3D();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        if (AlchemicalWizardry.wimpySettings) {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
        } else {
            this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SacrificialDagger");
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (AlchemicalWizardry.wimpySettings) {
            par3List.add(StatCollector.translateToLocal("tooltip.sacrificialdagger.desc1"));
        } else {
            par3List.add(StatCollector.translateToLocal("tooltip.sacrificialdagger.desc2"));
            par3List.add(StatCollector.translateToLocal("tooltip.sacrificialdagger.desc3"));
        }
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemInUseCount) {
        int usageCount = this.getMaxItemUseDuration(stack) - itemInUseCount;
        int hpCount = (int) Math.min(((usageCount * 0.35)), player.getMaxHealth());

        Random rnd = new Random();
        int shuffle = rnd.nextInt(3);

        if (player instanceof FakePlayer) {
            return;
        }

        if (player.isPotionActive(AlchemicalWizardry.customPotionSoulFray)) {
            if (world.isRemote) player.addChatComponentMessage(
                    new ChatComponentTranslation("message.sacrifice.soulfray" + shuffle)
                            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            return;
        }

        if (hpCount < 1) {
            return;
        }

        if (this.canUseForSacrifice(stack)) {
            PlayerSacrificeHandler.sacrificePlayerHealth(player);
        } else {
            SacrificeKnifeUsedEvent evt = new SacrificeKnifeUsedEvent(player, true, true, hpCount);

            if (MinecraftForge.EVENT_BUS.post(evt)) {
                return;
            }

            if (evt.shouldDrainHealth && !player.capabilities.isCreativeMode) {
                if (!world.isRemote && player.getHealth() <= hpCount) {
                    DamageSourceBloodMagic damageSrc = DamageSourceBloodMagic.INSTANCE;
                    player.hurtResistantTime = 0;
                    player.attackEntityFrom(damageSrc, Float.MAX_VALUE);
                }
                player.setHealth(player.getHealth() - hpCount);
                player.addPotionEffect(
                        new PotionEffect(
                                new PotionEffect(AlchemicalWizardry.customPotionSoulFray.id, (1 + hpCount * 10), 0)));
            }

            if (!evt.shouldFillAltar) {
                return;
            }

            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect(
                    ((float) posX + 0.5F),
                    (double) ((float) posY + 0.5F),
                    (double) ((float) posZ + 0.5F),
                    "random.fizz",
                    0.5F,
                    2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            float f = 1.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = f * f * 0.7F - 0.5F;
            float f3 = f * f * 0.6F - 0.7F;

            for (int l = 0; l < 8; ++l) {
                world.spawnParticle(
                        "reddust",
                        posX + Math.random() - Math.random(),
                        posY + Math.random() - Math.random(),
                        posZ + Math.random() - Math.random(),
                        f1,
                        f2,
                        f3);
            }

            if (!world.isRemote && SpellHelper.isFakePlayer(world, player)) {
                return;
            }

            findAndFillAltar(world, player, (hpCount * AlchemicalWizardry.lpPerSelfSacrifice));
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items are being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    public void findAndFillAltar(World world, EntityPlayer player, int amount) {
        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);
        IBloodAltar altarEntity = getAltar(world, posX, posY, posZ);

        if (altarEntity == null) {
            return;
        }

        altarEntity.sacrificialDaggerCall(amount, false);
        altarEntity.startCycle();
    }

    public IBloodAltar getAltar(World world, int x, int y, int z) {
        TileEntity tileEntity;

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 1; k++) {
                    tileEntity = world.getTileEntity(i + x, k + y, j + z);

                    if (tileEntity instanceof IBloodAltar) {
                        return (IBloodAltar) tileEntity;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        if (!world.isRemote && entity instanceof EntityPlayer) {
            this.setUseForSacrifice(stack, this.isPlayerPreparedForSacrifice(world, (EntityPlayer) entity));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (AlchemicalWizardry.wimpySettings) {
            return "Sacrificial Orb";
        }
        return super.getItemStackDisplayName(stack);
    }

    public boolean isPlayerPreparedForSacrifice(World world, EntityPlayer player) {
        return !world.isRemote && (PlayerSacrificeHandler.getPlayerIncense(player) > 0);
    }

    public boolean canUseForSacrifice(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();

        return tag != null && tag.getBoolean("sacrifice");
    }

    public void setUseForSacrifice(ItemStack stack, boolean sacrifice) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        tag.setBoolean("sacrifice", sacrifice);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return this.canUseForSacrifice(stack) || super.hasEffect(stack, pass);
    }
}
