package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class EnergyItems extends Item implements IBindable {

    private int energyUsed;

    public EnergyItems() {
        super();
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    public void setEnergyUsed(int par1int) {
        this.energyUsed = par1int;
    }

    public int getEnergyUsed() {
        return this.energyUsed;
    }

    @Override
    public int drainCost() {
        return this.energyUsed;
    }

    protected void damagePlayer(World world, EntityPlayer player, int damage) {
        if (world != null) {
            double posX = player.posX;
            double posY = player.posY;
            double posZ = player.posZ;
            world.playSoundEffect(
                    (double) ((float) posX + 0.5F),
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
        }
        for (int i = 0; i < damage; i++) {
            player.setHealth((player.getHealth() - 1));

            if (player.getHealth() <= 0.0005) {
                player.inventory.dropAllItems();
                break;
            }
        }
    }

    public static boolean syphonBatteries(ItemStack ist, EntityPlayer player, int damageToBeDone) {
        if (!player.worldObj.isRemote) {
            return SoulNetworkHandler.syphonAndDamageFromNetwork(ist, player, damageToBeDone);
        } else {
            World world = player.worldObj;
            if (world != null) {
                double posX = player.posX;
                double posY = player.posY;
                double posZ = player.posZ;

                SpellHelper.sendIndexedParticleToAllAround(
                        world,
                        posX,
                        posY,
                        posZ,
                        20,
                        world.provider.dimensionId,
                        4,
                        posX,
                        posY,
                        posZ);
                world.playSoundEffect(
                        ((float) player.posX + 0.5F),
                        ((float) player.posY + 0.5F),
                        ((float) player.posZ + 0.5F),
                        "random.fizz",
                        0.5F,
                        2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            }
        }
        return true;
    }

    @Deprecated
    public static boolean syphonWhileInContainer(ItemStack ist, int damageToBeDone) {
        if (ist.getTagCompound() != null && !(ist.getTagCompound().getString("ownerName").equals(""))) {
            String ownerName = ist.getTagCompound().getString("ownerName");

            if (MinecraftServer.getServer() == null) {
                return false;
            }

            World world = MinecraftServer.getServer().worldServers[0];
            LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

            if (data == null) {
                data = new LifeEssenceNetwork(ownerName);
                world.setItemData(ownerName, data);
            }

            if (data.currentEssence >= damageToBeDone) {
                data.currentEssence -= damageToBeDone;
                data.markDirty();
                return true;
            }
        }

        return false;
    }

    public static boolean canSyphonInContainer(ItemStack ist, int damageToBeDone) {
        if (!(ist.getItem() instanceof IBindable)) {
            return false;
        }

        String ownerName = IBindable.getOwnerName(ist);
        MinecraftServer server = MinecraftServer.getServer();

        if (ownerName.isEmpty() || server == null) {
            return false;
        }

        World world = server.worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        return data.currentEssence >= damageToBeDone;
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

    public static boolean syphonAndDamageWhileInContainer(ItemStack ist, EntityPlayer player, int damageToBeDone) {
        if (!syphonWhileInContainer(ist, damageToBeDone)) {
            hurtPlayer(player, damageToBeDone);
        }

        return true;
    }

    // Used by some addon mods like Blood Arsenal
    @Deprecated
    public static boolean checkAndSetItemOwner(ItemStack item, EntityPlayer player) {
        return IBindable.checkAndSetItemOwner(item, player);
    }

    // Used by some addon mods like Blood Arsenal
    @Deprecated
    public static void setItemOwner(ItemStack item, String ownerName) {
        IBindable.setItemOwner(item, ownerName);
    }

    // Used by some addon mods like Blood Arsenal
    @Deprecated
    public static void checkAndSetItemOwner(ItemStack item, String ownerName) {
        IBindable.checkAndSetItemOwner(item, ownerName);
    }

    // Used by some addon mods like Blood Arsenal
    @Deprecated
    public static String getOwnerName(ItemStack item) {
        return IBindable.getOwnerName(item);
    }

    @Deprecated
    public static void drainPlayerNetwork(EntityPlayer player, int damageToBeDone) {
        String ownerName = SpellHelper.getUsername(player);

        if (MinecraftServer.getServer() == null) {
            return;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        if (data.currentEssence >= damageToBeDone) {
            data.currentEssence -= damageToBeDone;
            data.markDirty();
        } else {
            hurtPlayer(player, damageToBeDone);
        }
    }

    @Deprecated
    public static int getCurrentEssence(String ownerName) {
        if (MinecraftServer.getServer() == null) {
            return 0;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        return data.currentEssence;
    }

    @Deprecated
    public static void setCurrentEssence(String ownerName, int amount) {
        if (MinecraftServer.getServer() == null) {
            return;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        data.currentEssence = amount;
        data.markDirty();
    }

    @Deprecated
    public static void addEssenceToMaximum(String ownerName, int amount, int maximum) {
        if (MinecraftServer.getServer() == null) {
            return;
        }

        World world = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) world.loadItemData(LifeEssenceNetwork.class, ownerName);

        if (data == null) {
            data = new LifeEssenceNetwork(ownerName);
            world.setItemData(ownerName, data);
        }

        if (data.currentEssence >= maximum) {
            return;
        }

        data.currentEssence = Math.min(maximum, data.currentEssence + amount);
        data.markDirty();
    }
}
