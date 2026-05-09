package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmMelee;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmProjectile;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmSelf;
import WayofTime.alchemicalWizardry.api.spell.SpellParadigmTool;
import WayofTime.alchemicalWizardry.api.tile.ISpellParadigmTile;

public class TESpellParadigmBlock extends TESpellBlock implements ISpellParadigmTile {

    public SpellParadigm getSpellParadigm() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        return switch (meta) {
            case 0 -> new SpellParadigmProjectile();
            case 2 -> new SpellParadigmMelee();
            case 3 -> new SpellParadigmTool();
            default -> new SpellParadigmSelf();
        };
    }

    @Override
    protected void applySpellChange(SpellParadigm parad) {}

    @Override
    public boolean canInputRecieve() {
        return false;
    }

    @Override
    public void castSpell(World world, EntityPlayer entity, ItemStack spellCasterStack) {
        SpellParadigm parad = this.getSpellParadigm();
        this.modifySpellParadigm(parad);
        parad.applyAllSpellEffects();
        parad.castSpell(world, entity, spellCasterStack);
    }

    @Override
    public String getResourceLocationForMeta(int meta) {
        return switch (meta) {
            case 2 -> "alchemicalwizardry:textures/models/SpellParadigmMelee.png";
            case 3 -> "alchemicalwizardry:textures/models/SpellParadigmTool.png";
            default -> "alchemicalwizardry:textures/models/SpellParadigmProjectile.png";
        };
    }

    @Override
    public void setInputDirection(ForgeDirection direction) {}

    @Override
    public ForgeDirection getInputDirection() {
        return ForgeDirection.UNKNOWN;
    }
}
