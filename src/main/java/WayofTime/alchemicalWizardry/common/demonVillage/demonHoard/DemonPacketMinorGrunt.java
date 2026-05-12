package WayofTime.alchemicalWizardry.common.demonVillage.demonHoard;

import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardian;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntGuardianWind;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntWind;
import WayofTime.alchemicalWizardry.common.demonVillage.tileEntity.TEDemonPortal;

public class DemonPacketMinorGrunt extends DemonHoardPacket {

    @Override
    public boolean canFitType(DemonType type) {
        return true;
    }

    @Override
    public float getRelativeChance(DemonType type, int tier, boolean spawnGuardian) {
        return 1.0f;
    }

    @Override
    public int summonDemons(TEDemonPortal teDemonPortal, World world, int x, int y, int z, DemonType type, int tier,
            boolean spawnGuardian) {
        EntityMinorDemonGrunt entity = switch (type) {
            case FIRE -> {
                if (spawnGuardian) {
                    yield new EntityMinorDemonGruntGuardianFire(world);
                } else {
                    yield new EntityMinorDemonGruntFire(world);
                }
            }
            case ICE -> {
                if (spawnGuardian) {
                    yield new EntityMinorDemonGruntGuardianIce(world);
                } else {
                    yield new EntityMinorDemonGruntIce(world);
                }
            }
            case EARTH -> {
                if (spawnGuardian) {
                    yield new EntityMinorDemonGruntGuardianEarth(world);
                } else {
                    yield new EntityMinorDemonGruntEarth(world);
                }
            }
            case WIND -> {
                if (spawnGuardian) {
                    yield new EntityMinorDemonGruntGuardianWind(world);
                } else {
                    yield new EntityMinorDemonGruntWind(world);
                }
            }
            default -> {
                if (spawnGuardian) {
                    yield new EntityMinorDemonGruntGuardian(world);
                } else {
                    yield new EntityMinorDemonGrunt(world);
                }
            }
        };

        entity.setPosition(x, y, z);

        world.spawnEntityInWorld(entity);

        teDemonPortal.enthrallDemon(entity);
        entity.setAggro(true);
        entity.setDropCrystal(false);

        return spawnGuardian ? 3 : 1;
    }
}
