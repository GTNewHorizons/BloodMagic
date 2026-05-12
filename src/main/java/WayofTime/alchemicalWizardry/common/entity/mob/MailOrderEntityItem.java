package WayofTime.alchemicalWizardry.common.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.book.BloodMagicGuide;
import amerifrance.guideapi.api.GuideRegistry;

public class MailOrderEntityItem extends EntityItem {

    public MailOrderEntityItem(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isImmuneToFire = true;
        this.lifespan = 72000;
    }

    public MailOrderEntityItem(World world, double x, double y, double z, ItemStack item) {
        this(world, x, y, z);
        this.setEntityItemStack(item);
        this.isImmuneToFire = true;
        this.lifespan = (item.getItem() == null ? 6000 : item.getItem().getEntityLifespan(item, world));
    }

    public MailOrderEntityItem(World world, Entity original, ItemStack stack) {
        this(world, original.posX, original.posY, original.posZ);
        this.delayBeforeCanPickup = 20;
        this.motionX = original.motionX;
        this.motionY = original.motionY;
        this.motionZ = original.motionZ;
        this.setEntityItemStack(stack);
        this.isImmuneToFire = true;
    }

    public MailOrderEntityItem(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!worldObj.isRemote && this.ticksExisted > 100 && !this.isDead) {
            worldObj.addWeatherEffect(new EntityLightningBolt(worldObj, this.posX, this.posY, this.posZ));

            EntityItem entity = new BookEntityItem(
                    worldObj,
                    this.posX,
                    this.posY,
                    this.posZ,
                    GuideRegistry.getItemStackForBook(BloodMagicGuide.bloodMagicGuide));
            entity.lifespan = 6000;
            entity.delayBeforeCanPickup = 20;
            entity.motionY = 1;
            worldObj.spawnEntityInWorld(entity);

            this.setDead();
        }
    }
}
