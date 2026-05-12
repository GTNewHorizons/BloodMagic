package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.energy.IReagentHandler;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainerInfo;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilDivination extends Item implements ArmourUpgrade, IReagentManipulator, IBindable, ISigil {

    public SigilDivination() {
        super();
        this.maxStackSize = 1;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DivinationSigil");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.divinationsigil.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.divinationsigil.desc2"));
        addBindingInformation(item, tooltip);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        IBindable.checkAndSetItemOwner(item, player);

        if (!IBindable.checkAndSetItemOwner(item, player) || player.worldObj.isRemote) {
            return item;
        }

        if (!EnergyItems.syphonBatteries(item, player, 0)) {
            return item;
        }

        String ownerName = IBindable.getOwnerName(item);

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (movingobjectposition == null) {
            tellEssence(player, ownerName);
            return item;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = movingobjectposition.blockX;
            int y = movingobjectposition.blockY;
            int z = movingobjectposition.blockZ;

            TileEntity tile = world.getTileEntity(x, y, z);

            if (!(tile instanceof IReagentHandler relay)) {
                tellEssence(player, ownerName);
                return item;
            }

            ReagentContainerInfo[] infoList = relay.getContainerInfo(ForgeDirection.UNKNOWN);
            if (infoList != null) {
                for (ReagentContainerInfo info : infoList) {
                    if (info != null && info.reagent != null && info.reagent.reagent != null) {
                        tellReagent(player, info);
                    }
                }
            }
        }

        return item;
    }

    private static void tellReagent(EntityPlayer player, ReagentContainerInfo info) {
        player.addChatComponentMessage(
                new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                                "message.divinationsigil.reagent",
                                ReagentRegistry.getKeyForReagent(info.reagent.reagent))));
        player.addChatComponentMessage(
                new ChatComponentText(
                        StatCollector
                                .translateToLocalFormatted("message.divinationsigil.amount", info.reagent.amount)));
    }

    private static void tellEssence(EntityPlayer player, String ownerName) {
        player.addChatMessage(
                new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                                "message.divinationsigil.currentessence",
                                SoulNetworkHandler.getCurrentEssence(ownerName))));
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {}

    @Override
    public boolean isUpgrade() {
        return false;
    }

    @Override
    public int getEnergyForTenSeconds() {
        return 25;
    }
}
