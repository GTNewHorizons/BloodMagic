package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.spell.simple.HomSpell;
import WayofTime.alchemicalWizardry.common.spell.simple.HomSpellRegistry;

public class TEHomHeart extends TileEntity {

    public boolean canCastSpell(ItemStack item, World world, EntityPlayer player) {
        return true;
    }

    public int getCostForSpell() {
        HomSpell spell = getSpell();

        if (spell != null) {
            return switch (getModifiedParadigm()) {
                case 0 -> spell.getOffensiveRangedEnergy();
                case 1 -> spell.getOffensiveMeleeEnergy();
                case 2 -> spell.getDefensiveEnergy();
                case 3 -> spell.getEnvironmentalEnergy();
                default -> throw new IllegalStateException("Unexpected value: " + getModifiedParadigm());
            };
        }

        return 0;
    }

    public int castSpell(ItemStack item, World world, EntityPlayer player) {
        HomSpell spell = getSpell();

        if (spell != null) {
            switch (getModifiedParadigm()) {
                case 0 -> {
                    spell.onOffensiveRangedRightClick(item, world, player);
                    return spell.getOffensiveRangedEnergy();
                }
                case 1 -> {
                    spell.onOffensiveMeleeRightClick(item, world, player);
                    return spell.getOffensiveMeleeEnergy();
                }
                case 2 -> {
                    spell.onDefensiveRightClick(item, world, player);
                    return spell.getDefensiveEnergy();
                }
                case 3 -> {
                    spell.onEnvironmentalRightClick(item, world, player);
                    return spell.getEnvironmentalEnergy();
                }
            }
        }

        return 0;
    }

    public HomSpell getSpell() {
        TileEntity tileEntity = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);

        if (tileEntity instanceof TEAltar) {
            ItemStack itemStack = ((TEAltar) tileEntity).getStackInSlot(0);

            if (itemStack != null) {
                HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

                if (spell != null) {
                    return spell;
                }
            }
        }

        tileEntity = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);

        if (tileEntity instanceof TEAltar) {
            ItemStack itemStack = ((TEAltar) tileEntity).getStackInSlot(0);

            if (itemStack != null) {
                HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

                if (spell != null) {
                    return spell;
                }
            }
        }

        tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);

        if (tileEntity instanceof TEAltar) {
            ItemStack itemStack = ((TEAltar) tileEntity).getStackInSlot(0);

            if (itemStack != null) {
                HomSpell spell = HomSpellRegistry.getSpellForItemStack(itemStack);

                if (spell != null) {
                    return spell;
                }
            }
        }

        tileEntity = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);

        if (tileEntity instanceof TEAltar altar) {
            ItemStack itemStack = altar.getStackInSlot(0);

            if (itemStack != null) {
                return HomSpellRegistry.getSpellForItemStack(itemStack);
            }
        }

        return null;
    }

    public int getModifiedParadigm() {
        // TODO change so that it works with a Tile Entity for a custom head or whatnot

        TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);

        if (tileEntity instanceof TileEntitySkull skull) {
            int skullType = skull.func_145904_a();

            return switch (skullType) {
                case 0 -> 0;
                case 1 -> 1;
                case 2 -> 2;
                case 4 -> 3;
                default -> -1;
            };
        }

        return -1;
    }
}
