package WayofTime.alchemicalWizardry.common.items.energy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.Int3;
import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.items.interfaces.IReagentManipulator;
import WayofTime.alchemicalWizardry.common.tileEntity.TEReagentConduit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDestinationClearer extends Item implements IReagentManipulator {

    public ItemDestinationClearer() {
        super();
        this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        this.maxStackSize = 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:TankClearer");
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean adv) {
        tooltip.add(StatCollector.translateToLocal("tooltip.destclearer.desc1"));
        tooltip.add(StatCollector.translateToLocal("tooltip.destclearer.desc2"));
        tooltip.add(StatCollector.translateToLocal("tooltip.destclearer.desc3"));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote) {
            return itemStack;
        }

        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);
        if (movingobjectposition == null
                || movingobjectposition.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return itemStack;
        }

        int x = movingobjectposition.blockX;
        int y = movingobjectposition.blockY;
        int z = movingobjectposition.blockZ;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TEReagentConduit relay)) {
            return itemStack;
        }

        if (player.isSneaking()) {
            int removed = clearIncomingConnections(world, x, y, z);
            player.addChatComponentMessage(
                    new ChatComponentTranslation("message.destinationclearer.incoming.cleared", removed));
        } else {
            relay.reagentTargetList.clear();
            world.markBlockForUpdate(x, y, z);
            player.addChatComponentMessage(new ChatComponentTranslation("message.destinationclearer.cleared"));
        }

        return itemStack;
    }

    private static int clearIncomingConnections(World world, int x, int y, int z) {
        int range = ItemAttunedCrystal.maxDistance;
        int totalRemoved = 0;
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    TileEntity neighbor = world.getTileEntity(x + dx, y + dy, z + dz);
                    if (!(neighbor instanceof TEReagentConduit src)) continue;

                    boolean changed = false;
                    Iterator<Map.Entry<Reagent, List<Int3>>> entries = src.reagentTargetList.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<Reagent, List<Int3>> entry = entries.next();
                        List<Int3> coords = entry.getValue();
                        if (coords == null) continue;
                        Iterator<Int3> it = coords.iterator();
                        while (it.hasNext()) {
                            Int3 off = it.next();
                            if (src.xCoord + off.x() == x && src.yCoord + off.y() == y
                                    && src.zCoord + off.z() == z) {
                                it.remove();
                                changed = true;
                                totalRemoved++;
                            }
                        }
                        if (coords.isEmpty()) entries.remove();
                    }
                    if (changed) {
                        world.markBlockForUpdate(src.xCoord, src.yCoord, src.zCoord);
                    }
                }
            }
        }
        return totalRemoved;
    }
}
