package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.tileEntity.TEPlinth;

public class RitualEffectBiomeChanger extends RitualEffect {

    @Override
    public void performEffect(IMasterRitualStone ritualStone) {
        String owner = ritualStone.getOwner();

        int cooldown = ritualStone.getCooldown();
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();
        if (cooldown > 0) {
            ritualStone.setCooldown(cooldown - 1);

            if (world.rand.nextInt(15) == 0) {
                world.addWeatherEffect(
                        new EntityLightningBolt(
                                world,
                                x - 1 + world.rand.nextInt(3),
                                y + 1,
                                z - 1 + world.rand.nextInt(3)));
            }

            return;
        }

        int currentEssence = SoulNetworkHandler.getCurrentEssence(owner);

        int range = 10;

        if (currentEssence < this.getCostPerRefresh()) {
            EntityPlayer entityOwner = SpellHelper.getPlayerForUsername(owner);

            if (entityOwner == null) {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else {
            boolean[][] boolList = new boolean[range * 2 + 1][range * 2 + 1];

            for (int i = 0; i < 2 * range + 1; i++) {
                for (int j = 0; j < 2 * range + 1; j++) {
                    boolList[i][j] = false;
                }
            }

            boolList[range][range] = true;

            Queue<Integer> BFSqueue = new ArrayDeque<>();
            BFSqueue.add(x);
            BFSqueue.add(z);

            BiConsumer<Integer, Integer> tryEnqueue = (nextX, nextZ) -> {
                if (Math.abs(nextX - x) > range || Math.abs(nextZ - z) > range) return;
                if (boolList[nextX - (x - range)][nextZ - (z - range)]) return;

                Block block = world.getBlock(nextX, y + 1, nextZ);
                if (block == null || block.equals(ModBlocks.bloodStoneBrick)
                        || block.equals(ModBlocks.largeBloodStoneBrick))
                    return;

                boolList[nextX - (x - range)][nextZ - (z - range)] = true;
                BFSqueue.add(nextX);
                BFSqueue.add(nextZ);
            };

            while (!BFSqueue.isEmpty()) {
                Integer curX = BFSqueue.remove();
                Integer curZ = BFSqueue.remove();

                tryEnqueue.accept(curX + 1, curZ);
                tryEnqueue.accept(curX, curZ + 1);
                tryEnqueue.accept(curX - 1, curZ);
                tryEnqueue.accept(curX, curZ - 1);
            }

            float temperature = 0.5f;
            float humidity = 0.5f;
            float acceptableRange = 0.0999f;
            int biomeSkip = 0;

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }

                    boolean isItemConsumed = false;
                    TileEntity tileEntity = world.getTileEntity(x + i, y, z + j);

                    if (!(tileEntity instanceof TEPlinth)) {
                        continue;
                    }

                    TEPlinth tilePlinth = (TEPlinth) tileEntity;
                    ItemStack itemStack = tilePlinth.getStackInSlot(0);

                    if (itemStack != null) {
                        Item itemTest = itemStack.getItem();

                        if (itemTest != null) {
                            if (itemTest instanceof ItemBlock) {
                                Block item = ((ItemBlock) itemTest).field_150939_a;
                                if (item == (Blocks.sand)) {
                                    humidity -= 0.1f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.lapis_block)) {
                                    humidity += 0.4f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.sand)) {
                                    humidity -= 0.1f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.sandstone)) {
                                    humidity -= 0.2f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.netherrack)) {
                                    humidity -= 0.4f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.coal_block)) {
                                    temperature += 0.2f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.ice)) {
                                    temperature -= 0.4f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.snow)) {
                                    temperature -= 0.2f;
                                    isItemConsumed = true;
                                } else if (item == (Blocks.wool)) {
                                    int skip = itemStack.getItemDamage() + 1;
                                    biomeSkip += skip;
                                    isItemConsumed = true;
                                }
                            } else if (itemTest.equals(Items.dye) && itemStack.getItemDamage() == 4) {
                                humidity += 0.1f;
                                isItemConsumed = true;
                            } else if (itemTest.equals(Items.lava_bucket)) {
                                temperature += 0.4f;
                                isItemConsumed = true;
                            } else if (itemTest.equals(Items.water_bucket)) {
                                humidity += 0.2f;
                                isItemConsumed = true;
                            } else if (itemTest.equals(Items.coal)) {
                                temperature += 0.1f;
                                isItemConsumed = true;
                            } else if (itemTest.equals(Items.snowball)) {
                                temperature -= 0.1f;
                                isItemConsumed = true;
                            }
                        }
                    }

                    if (isItemConsumed) {
                        tilePlinth.setInventorySlotContents(0, null);
                        world.markBlockForUpdate(x + i, y, z + j);
                        world.addWeatherEffect(new EntityLightningBolt(world, x + i, y + 1, z + j));
                    }
                }
            }

            boolean wantsSnow = false;
            boolean wantsRain = true;
            int biomeID = 1;
            BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();

            for (BiomeGenBase biome : biomeList) {
                if (biome == null) {
                    continue;
                }

                float temp = biome.temperature;
                float rainfall = biome.rainfall;
                temperature = Math.min(2.0f, Math.max(0.0f, temperature));
                humidity = Math.min(2.0f, Math.max(0.0f, humidity));

                if (Math.abs(rainfall - humidity) < acceptableRange && Math.abs(temperature - temp) < acceptableRange) {
                    biomeID = biome.biomeID;
                    if (biomeSkip == 0) {
                        break;
                    } else {
                        biomeSkip--;
                    }
                }
            }

            // Default to Plains if too much biome skip is used
            if (biomeSkip != 0) {
                biomeID = 1;
            }

            List<Chunk> chunkList = new ArrayList<>();

            for (int chunkX = (x - range) >> 4; chunkX <= (x + range) >> 4; ++chunkX) {
                for (int chunkZ = (z - range) >> 4; chunkZ <= (z + range) >> 4; ++chunkZ) {
                    chunkList.add(world.getChunkFromChunkCoords(chunkX, chunkZ));
                }
            }

            for (Chunk chunk : chunkList) {
                byte[] byteArray = chunk.getBiomeArray();
                BitSet mask = new BitSet();
                boolean changed = false;

                for (int cZ = 0; cZ < 16; ++cZ) {
                    int offsetZ = (chunk.zPosition << 4 | cZ) - (z - range);
                    if (0 <= offsetZ && offsetZ < 2 * range + 1) {
                        for (int cX = 0; cX < 16; ++cX) {
                            int offsetX = (chunk.xPosition << 4 | cX) - (x - range);
                            if (0 <= offsetX && offsetX < 2 * range + 1) {
                                if (boolList[offsetX][offsetZ]) {
                                    mask.set(cZ << 4 | cX, true);
                                    byteArray[cZ << 4 | cX] = (byte) biomeID;
                                    changed = true;
                                }
                            }
                        }
                    }
                }

                if (changed) {
                    chunk.setBiomeArray(byteArray);
                    NewPacketHandler.INSTANCE.sendToDimension(
                            NewPacketHandler
                                    .getGaiaBiomeChangePacket(chunk.xPosition, chunk.zPosition, (byte) biomeID, mask),
                            world.provider.dimensionId);
                }
            }

            SoulNetworkHandler.syphonFromNetwork(owner, this.getCostPerRefresh());
            ritualStone.setActive(false);
        }
    }

    @Override
    public int getCostPerRefresh() {
        return AlchemicalWizardry.ritualCostGaia[1];
    }

    @Override
    public int getInitialCooldown() {
        return 200;
    }

    @Override
    public List<RitualComponent> getRitualComponentList() {
        ArrayList<RitualComponent> biomeChangerRitual = new ArrayList();
        biomeChangerRitual.add(new RitualComponent(1, 0, -2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(1, 0, -3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(2, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(3, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(1, 0, 2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(1, 0, 3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(2, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(3, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-2, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-3, 0, -1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 2, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 3, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-2, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-3, 0, 1, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(3, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(3, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, -5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(5, 0, -4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(3, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(3, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, 5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(5, 0, 4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-3, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-3, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-3, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-3, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -3, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -5, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-5, 0, -4, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(0, 0, -5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(1, 0, -6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(0, 0, -8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(1, 0, -8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-1, 0, -10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(0, 0, -10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(1, 0, -10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(0, 0, 5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(1, 0, 6, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(0, 0, 8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(1, 0, 8, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-1, 0, 10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(0, 0, 10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(1, 0, 10, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 0, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-6, 0, -1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-6, 0, 1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-8, 0, -1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 0, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(-10, 0, -1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-10, 0, 0, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-10, 0, 1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(5, 0, 0, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(6, 0, -1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(6, 0, 1, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(8, 0, -1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(8, 0, 0, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(8, 0, 1, RitualComponent.BLANK));
        biomeChangerRitual.add(new RitualComponent(10, 0, -1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(10, 0, 0, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(10, 0, 1, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(6, 0, -6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(6, 0, -7, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(7, 0, -6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(7, 0, -5, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(5, 0, -7, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(8, 0, -5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(8, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(9, 0, -4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(5, 0, -8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(4, 0, -8, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(4, 0, -9, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-6, 0, 6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-6, 0, 7, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-7, 0, 6, RitualComponent.AIR));
        biomeChangerRitual.add(new RitualComponent(-7, 0, 5, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 7, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-9, 0, 4, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-5, 0, 8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 8, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(-4, 0, 9, RitualComponent.EARTH));
        biomeChangerRitual.add(new RitualComponent(6, 0, 6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(6, 0, 7, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(7, 0, 6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(7, 0, 5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(5, 0, 7, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(8, 0, 5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(8, 0, 4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(9, 0, 4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(5, 0, 8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(4, 0, 8, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(4, 0, 9, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-6, 0, -6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-6, 0, -7, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-7, 0, -6, RitualComponent.FIRE));
        biomeChangerRitual.add(new RitualComponent(-7, 0, -5, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-5, 0, -7, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-8, 0, -5, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-8, 0, -4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-9, 0, -4, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-5, 0, -8, RitualComponent.DUSK));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -8, RitualComponent.WATER));
        biomeChangerRitual.add(new RitualComponent(-4, 0, -9, RitualComponent.WATER));
        return biomeChangerRitual;
    }
}
