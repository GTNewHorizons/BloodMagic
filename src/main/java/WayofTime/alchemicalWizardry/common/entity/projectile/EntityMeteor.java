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

    public EntityMeteor(World par1World) {
        super(par1World);
        this.meteorID = 0;
    }

    public EntityMeteor(World par1World, double par2, double par4, double par6, int meteorID) {
        super(par1World, par2, par4, par6);
        this.meteorID = meteorID;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("meteorID", meteorID);

        for (Reagent r : reagentList) {
            par1NBTTagCompound.setBoolean("reagent." + r.name, true);
        }

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);

        meteorID = par1NBTTagCompound.getInteger("meteorID");
        for (Reagent r : ReagentRegistry.reagentList.values()) {
            if (par1NBTTagCompound.getBoolean("reagent." + r.name)) {
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
    public void onImpact(Entity mop) {
        MeteorRegistry
                .createMeteorImpact(worldObj, (int) this.posX, (int) this.posY, (int) this.posZ, meteorID, reagentList);

        this.setDead();
    }
}
