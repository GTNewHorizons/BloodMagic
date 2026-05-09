package WayofTime.alchemicalWizardry.common.entity.projectile;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.api.alchemy.energy.Reagent;
import WayofTime.alchemicalWizardry.api.alchemy.energy.ReagentRegistry;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;

public class EntityMeteor extends EnergyBlastProjectile {

    private int meteorID;

    public ArrayList<Reagent> reagentList = new ArrayList<>();

    public EntityMeteor(World world) {
        super(world);
        this.meteorID = 0;
    }

    public EntityMeteor(World world, double x, double y, double z, int meteorID) {
        super(world, x, y, z);
        this.meteorID = meteorID;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);

        tag.setInteger("meteorID", meteorID);

        for (Reagent r : reagentList) {
            tag.setBoolean("reagent." + r.name, true);
        }

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);

        meteorID = tag.getInteger("meteorID");
        for (Reagent r : ReagentRegistry.reagentList.values()) {
            if (tag.getBoolean("reagent." + r.name)) {
                reagentList.add(r);
            }
        }
    }

    @Override
    public DamageSource getDamageSource() {
        return DamageSource.fallingBlock;
    }

    @Override
    public void onImpact(MovingObjectPosition mop) {
        if (worldObj.isRemote) {
            return;
        }

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null) {
            this.onImpact(mop.entityHit);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            MeteorRegistry.createMeteorImpact(worldObj, mop.blockX, mop.blockY, mop.blockZ, this.meteorID, reagentList);
        }

        this.setDead();
    }

    @Override
    public void onImpact(Entity target) {
        MeteorRegistry
                .createMeteorImpact(worldObj, (int) this.posX, (int) this.posY, (int) this.posZ, meteorID, reagentList);

        this.setDead();
    }
}
