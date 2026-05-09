package WayofTime.alchemicalWizardry.api.soulNetwork;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import WayofTime.alchemicalWizardry.api.event.AddToNetworkEvent;
import WayofTime.alchemicalWizardry.api.event.ItemBindEvent;
import WayofTime.alchemicalWizardry.api.event.ItemDrainInContainerEvent;
import WayofTime.alchemicalWizardry.api.event.ItemDrainNetworkEvent;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class SoulNetworkHandler {

    public static boolean syphonFromNetworkWhileInContainer(ItemStack ist, int damageToBeDone) {
        String ownerName = "";
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").isEmpty())) {
            ownerName = ist.getTagCompound().getString("ownerName");
        }

        ItemDrainInContainerEvent event = new ItemDrainInContainerEvent(ist, ownerName, damageToBeDone);

        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY) {
            return false;
        }

        return syphonFromNetwork(event.ownerNetwork, event.drainAmount) >= damageToBeDone;
    }

    /**
     * @deprecated Use {@link #getMaxEssence(String ownerName)} to get maximum soul network capacity
     */
    @Deprecated
    public static int getCurrentMaxOrb(String ownerName) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return 0;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        return data.maxOrb;
    }

    public static int getMaxEssence(String ownerName) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return 0;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        return data.maxEssence;
    }

    /**
     * @deprecated Use {@link #setMaxEssenceToMax(String ownerName, int maxEssence)} to set maximum soul network
     *             capacity
     */
    @Deprecated
    public static void setMaxOrbToMax(String ownerName, int maxOrb) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        data.maxOrb = Math.max(maxOrb, data.maxOrb);
        data.markDirty();
    }

    public static void setMaxEssenceToMax(String ownerName, int maxEssence) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        data.maxEssence = Math.max(maxEssence, data.maxEssence);
        data.markDirty();
    }

    /**
     * @deprecated Use {@link #getMaxEssence(String ownerName)} to get maximum soul network capacity
     */
    @Deprecated
    public static int getMaximumForOrbTier(int maxOrb) {
        return switch (maxOrb) {
            case 1 -> 5000;
            case 2 -> 25000;
            case 3 -> 150000;
            case 4 -> 1000000;
            case 5 -> 10000000;
            case 6 -> 30000000;
            default -> 1;
        };
    }

    public static int syphonFromNetwork(ItemStack ist, int damageToBeDone) {
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").isEmpty())) {
            String ownerName = ist.getTagCompound().getString("ownerName");

            return syphonFromNetwork(ownerName, damageToBeDone);
        }
        return 0;
    }

    public static int syphonFromNetwork(String ownerName, int damageToBeDone) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null || mcServer.worldServers.length == 0) {
            return 0;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        if (data.currentEssence >= damageToBeDone) {
            data.currentEssence -= damageToBeDone;
            data.markDirty();
            return damageToBeDone;
        }

        return 0;
    }

    /**
     * Master method used to syphon from the player's network, and will damage them accordingly if they do not have
     * enough LP. Does not drain on the client side.
     *
     * @param ist    Owned itemStack
     * @param player Player using the item
     * @param drain  The amount of LP to drain
     * @return True if the action should be executed and false if it should not. Always returns false if client-sided.
     */
    public static boolean syphonAndDamageFromNetwork(ItemStack ist, EntityPlayer player, int drain) {
        if (player.worldObj.isRemote) {
            return false;
        }

        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").isEmpty())) {
            String ownerName = ist.getTagCompound().getString("ownerName");

            ItemDrainNetworkEvent event = new ItemDrainNetworkEvent(player, ownerName, ist, drain);

            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }

            int drainAmount = syphonFromNetwork(event.ownerNetwork, event.drainAmount);
            if (drainAmount == 0 || event.shouldDamage) {
                hurtPlayer(player, event.damageAmount);
            }

            return (event.getResult() != Event.Result.DENY); // The event has been told to prevent the action but allow
                                                             // all repercussions of
            // using the item.
        }

        int amount = SoulNetworkHandler.syphonFromNetwork(ist, drain);

        hurtPlayer(player, drain - amount);

        return true;
    }

    public static boolean syphonAndDamageFromNetwork(String ownerName, EntityPlayer player, int damageToBeDone) {
        if (player.worldObj.isRemote) {
            return false;
        }

        World world = player.worldObj;
        world.playSoundEffect(
                (float) player.posX + 0.5F,
                (float) player.posY + 0.5F,
                (float) player.posZ + 0.5F,
                "random.fizz",
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        int amount = SoulNetworkHandler.syphonFromNetwork(ownerName, damageToBeDone);

        hurtPlayer(player, damageToBeDone - amount);

        return true;
    }

    public static boolean canSyphonFromOnlyNetwork(ItemStack ist, int damageToBeDone) {
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").isEmpty())) {
            String ownerName = ist.getTagCompound().getString("ownerName");

            return canSyphonFromOnlyNetwork(ownerName, damageToBeDone);
        }

        return false;
    }

    public static boolean canSyphonFromOnlyNetwork(String ownerName, int damageToBeDone) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return false;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        return data.currentEssence >= damageToBeDone;
    }

    public static int getCurrentEssence(String ownerName) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return 0;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        return data.currentEssence;
    }

    public static void setCurrentEssence(String ownerName, int essence) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        data.currentEssence = essence;
        data.markDirty();
    }

    /**
     * A method to add to an owner's network up to a maximum value.
     *
     * @return amount added to the network
     */
    public static int addCurrentEssenceToMaximum(String ownerName, int addedEssence, int maximum) {
        AddToNetworkEvent event = new AddToNetworkEvent(ownerName, addedEssence, maximum);

        if (MinecraftForge.EVENT_BUS.post(event)) {
            return 0;
        }

        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return 0;
        }

        World world = mcServer.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, event.ownerNetwork);

        if (data == null) {
            data = new LifeEssenceNetwork(event.ownerNetwork);
            world.setItemData(event.ownerNetwork, data);
        }

        int currEss = data.currentEssence;

        if (currEss >= event.maximum) {
            return 0;
        }

        int newEss = (int) Math.min(Integer.MAX_VALUE, Math.min(event.maximum, (long) currEss + event.addedAmount));
        if (event.getResult() != Event.Result.DENY) {
            data.currentEssence = newEss;
        }

        return newEss - currEss;
    }

    public static void hurtPlayer(EntityPlayer user, int energySyphoned) {
        if (energySyphoned < 100 && energySyphoned > 0) {
            if (!user.capabilities.isCreativeMode) {
                user.setHealth((user.getHealth() - 1));

                if (user.getHealth() <= 0.0005f) {
                    user.onDeath(DamageSource.generic);
                }
            }
        } else if (energySyphoned >= 100) {
            if (!user.capabilities.isCreativeMode) {
                for (int i = 0; i < ((energySyphoned + 99) / 100); i++) {
                    user.setHealth((user.getHealth() - 1));

                    if (user.getHealth() <= 0.0005f) {
                        user.onDeath(DamageSource.generic);
                        break;
                    }
                }
            }
        }
    }

    public static void hurtPlayer(EntityPlayer user, float damage) {
        if (!user.capabilities.isCreativeMode) {
            user.setHealth((user.getHealth() - damage));

            if (user.getHealth() <= 0.0005f) {
                user.onDeath(DamageSource.generic);
            }
        }
    }

    public static void checkAndSetItemOwner(ItemStack item, EntityPlayer player) {
        checkAndSetItemPlayer(item, player);
    }

    public static boolean checkAndSetItemPlayer(ItemStack item, EntityPlayer player) {
        if (item.getItem() instanceof IBloodOrb orb) {
            String currentOwner = item.hasTagCompound() ? item.getTagCompound().getString("ownerName") : "";
            String thisPlayer = getUsername(player);
            if (currentOwner.isEmpty() || currentOwner.equals(thisPlayer)) {
                setMaxEssenceToMax(thisPlayer, orb.getMaxEssence());
            }
        }

        if (item.hasTagCompound() && !item.getTagCompound().getString("ownerName").isEmpty()) return true;

        ItemBindEvent event = new ItemBindEvent(player, SoulNetworkHandler.getUsername(player), item);

        if (!MinecraftForge.EVENT_BUS.post(event)) {
            if (!item.hasTagCompound()) {
                item.setTagCompound(new NBTTagCompound());
            }
            item.getTagCompound().setString("ownerName", event.key);
            return true;
        }
        return false;
    }

    @Deprecated
    public static void checkAndSetItemOwner(ItemStack item, String ownerName) {
        IBindable.checkAndSetItemOwner(item, ownerName);
    }

    public static String getUsername(EntityPlayer player) {
        return player.getCommandSenderName();
    }

    public static EntityPlayer getPlayerForUsername(String str) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return null;
        }
        return mcServer.getConfigurationManager().func_152612_a(str);
    }

    public static void causeNauseaToPlayer(ItemStack stack) {
        if (stack.getTagCompound() != null && !(stack.getTagCompound().getString("ownerName").isEmpty())) {
            String ownerName = stack.getTagCompound().getString("ownerName");

            SoulNetworkHandler.causeNauseaToPlayer(ownerName);
        }
    }

    public static void causeNauseaToPlayer(String ownerName) {
        EntityPlayer entityOwner = SoulNetworkHandler.getPlayerForUsername(ownerName);

        if (entityOwner == null) {
            return;
        }

        entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
    }

    public static String getOwnerName(ItemStack item) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        return item.getTagCompound().getString("ownerName");
    }
}
