package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BookEntityItem extends EntityItem {

    public BookEntityItem(World world, double par2, double par4, double par6) {
        super(world, par2, par4, par6);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
    }

    public BookEntityItem(World world, double x, double y, double z, ItemStack item) {
        this(world, x, y, z);
        this.setEntityItemStack(item);
        this.lifespan = (item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, world));
        this.isImmuneToFire = true;
    }

    public BookEntityItem(World world, Entity original, ItemStack stack) {
        this(world, original.posX, original.posY, original.posZ);
        this.delayBeforeCanPickup = 20;
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
        this.setEntityItemStack(stack);
        this.isImmuneToFire = true;
    }

    public BookEntityItem(World par1world) {
        super(par1world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        return par1DamageSource.getDamageType().equals("outOfWorld");
    }
}
