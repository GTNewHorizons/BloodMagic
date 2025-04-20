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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector.translateToLocal("tooltip.divinationsigil.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.divinationsigil.desc2"));
        addBindingInformation(par1ItemStack, par3List);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (!IBindable.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer) || par3EntityPlayer.worldObj.isRemote) {
            return par1ItemStack;
        }

        if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 0)) {
            return par1ItemStack;
        }

        String ownerName = IBindable.getOwnerName(par1ItemStack);

        MovingObjectPosition movingobjectposition = this
                .getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (movingobjectposition == null) {
            tellEssence(par3EntityPlayer, ownerName);
            return par1ItemStack;
        } else {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = movingobjectposition.blockX;
                int y = movingobjectposition.blockY;
                int z = movingobjectposition.blockZ;

                TileEntity tile = par2World.getTileEntity(x, y, z);

                if (!(tile instanceof IReagentHandler)) {
                    tellEssence(par3EntityPlayer, ownerName);
                    return par1ItemStack;
                }

                IReagentHandler relay = (IReagentHandler) tile;

                ReagentContainerInfo[] infoList = relay.getContainerInfo(ForgeDirection.UNKNOWN);
                if (infoList != null) {
                    for (ReagentContainerInfo info : infoList) {
                        if (info != null && info.reagent != null && info.reagent.reagent != null) {
                            tellReagent(par3EntityPlayer, info);
                        }
                    }
                }
            }
        }

        return par1ItemStack;
    }

    private static void tellReagent(EntityPlayer par3EntityPlayer, ReagentContainerInfo info) {
        par3EntityPlayer.addChatComponentMessage(
                new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                                "message.divinationsigil.reagent",
                                ReagentRegistry.getKeyForReagent(info.reagent.reagent))));
        par3EntityPlayer.addChatComponentMessage(
                new ChatComponentText(
                        StatCollector
                                .translateToLocalFormatted("message.divinationsigil.amount", info.reagent.amount)));
    }

    private static void tellEssence(EntityPlayer par3EntityPlayer, String ownerName) {
        par3EntityPlayer.addChatMessage(
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
