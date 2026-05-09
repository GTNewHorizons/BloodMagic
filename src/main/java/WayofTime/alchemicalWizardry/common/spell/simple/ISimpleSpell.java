package WayofTime.alchemicalWizardry.common.spell.simple;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISimpleSpell {

    ItemStack onOffensiveRangedRightClick(ItemStack item, World world, EntityPlayer player);

    ItemStack onOffensiveMeleeRightClick(ItemStack item, World world, EntityPlayer player);

    ItemStack onDefensiveRightClick(ItemStack item, World world, EntityPlayer player);

    ItemStack onEnvironmentalRightClick(ItemStack item, World world, EntityPlayer player);
}
