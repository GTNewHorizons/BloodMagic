package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;

import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentContainer;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentStack;

public class ItemBlockCrystalBelljar extends ItemBlock {

    public ItemBlockCrystalBelljar(Block par1) {
        super(par1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    public ReagentContainer[] getReagentContainers(ItemStack stack) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();

            NBTTagList tagList = tag.getTagList("reagentTanks", Constants.NBT.TAG_COMPOUND);

            int size = tagList.tagCount();
            ReagentContainer[] tanks = new ReagentContainer[size];

            for (int i = 0; i < size; i++) {
                NBTTagCompound savedTag = tagList.getCompoundTagAt(i);
                tanks[i] = ReagentContainer.readFromNBT(savedTag);
            }

            return tanks;
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean adv) {
        ReagentContainer[] tanks = this.getReagentContainers(stack);

        if (tanks == null) {
            tooltip.add(StatCollector.translateToLocal("tooltip.crystalbelljar.empty"));
        } else {
            tooltip.add(StatCollector.translateToLocal("tooltip.crystalbelljar.contents"));
            for (ReagentContainer tank : tanks) {
                if (tank == null || tank.getReagent() == null || tank.getReagent().reagent == null) {
                    tooltip.add("- Empty");
                } else {
                    ReagentStack reagentStack = tank.getReagent();
                    tooltip.add("- " + reagentStack.reagent.name() + ": " + reagentStack.amount
                                    + "/"
                                    + tank.getCapacity() / 1000
                                    + "k AR");
                }
            }
        }
    }
}
