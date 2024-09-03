package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.common.blocks.TileEntityOres;

public class MeteorParadigm {

    public List<MeteorParadigmComponent> componentList = new ArrayList<>();
    public ItemStack focusStack;
    public int radius;
    public int cost;

    public static Random rand = new Random();

    public MeteorParadigm(ItemStack focusStack, int radius, int cost) {
        this.focusStack = focusStack;
        this.radius = radius;
        this.cost = cost;
    }

    // modId:itemName:meta:weight
    private static final Pattern itemNamePattern = Pattern.compile("(.*):(.*):(\\d+):(\\d+)");
    // OREDICT:oreDictName:weight
    private static final Pattern oredictPattern = Pattern.compile("OREDICT:(.*):(\\d+)");

    public void parseStringArray(String[] oreArray) {
        for (int i = 0; i < oreArray.length; ++i) {
            String oreName = oreArray[i];
            boolean success = false;

            Matcher matcher = itemNamePattern.matcher(oreName);
            if (matcher.matches()) {
                String modID = matcher.group(1);
                String itemName = matcher.group(2);
                int meta = Integer.parseInt(matcher.group(3));
                int weight = Integer.parseInt(matcher.group(4));

                ItemStack stack = GameRegistry.findItemStack(modID, itemName, 1);
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    stack.setItemDamage(meta);
                    componentList.add(new MeteorParadigmComponent(stack, weight));
                    success = true;
                }

            } else if ((matcher = oredictPattern.matcher(oreName)).matches()) {
                String oreDict = matcher.group(1);
                int weight = Integer.parseInt(matcher.group(2));

                List<ItemStack> list = OreDictionary.getOres(oreDict);
                for (ItemStack stack : list) {
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        componentList.add(new MeteorParadigmComponent(stack, weight));
                        success = true;
                        break;
                    }
                }

            } else {
                // Legacy config
                String oreDict = oreName;
                int weight = Integer.parseInt(oreArray[++i]);

                List<ItemStack> list = OreDictionary.getOres(oreDict);
                for (ItemStack stack : list) {
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        componentList.add(new MeteorParadigmComponent(stack, weight));
                        success = true;
                        break;
                    }
                }
            }

            if (!success) {
                AlchemicalWizardry.logger.warn("Unable to add Meteor Paradigm \"" + oreName + "\"");
            }
        }
    }

    public int getTotalMeteorWeight() {
        int totalMeteorWeight = 0;
        for (MeteorParadigmComponent mpc : componentList) {
            totalMeteorWeight += mpc.getChance();
        }
        return totalMeteorWeight;
    }

    public void createMeteorImpact(World world, int x, int y, int z, boolean[] flags) {
        boolean hasTerrae = false;
        boolean hasOrbisTerrae = false;
        boolean hasCrystallos = false;
        boolean hasIncendium = false;
        boolean hasTennebrae = false;

        if (flags != null && flags.length >= 5) {
            hasTerrae = flags[0];
            hasOrbisTerrae = flags[1];
            hasCrystallos = flags[2];
            hasIncendium = flags[3];
            hasTennebrae = flags[4];
        }

        int newRadius = radius;

        if (hasOrbisTerrae) {
            newRadius += 2;
        } else if (hasTerrae) {
            newRadius += 1;
        }

        world.createExplosion(null, x, y, z, newRadius * 4, AlchemicalWizardry.doMeteorsDestroyBlocks);

        float iceChance = hasCrystallos ? 1 : 0;
        float soulChance = hasIncendium ? 1 : 0;
        float obsidChance = hasTennebrae ? 1 : 0;

        float totalChance = iceChance + soulChance + obsidChance;

        int totalMeteorWeight = getTotalMeteorWeight();

        for (int i = -newRadius; i <= newRadius; i++) {
            for (int j = -newRadius; j <= newRadius; j++) {
                for (int k = -newRadius; k <= newRadius; k++) {
                    if (i * i + j * j + k * k >= (newRadius + 0.50f) * (newRadius + 0.50f)) {
                        continue;
                    }

                    if (!world.isAirBlock(x + i, y + j, z + k)) {
                        continue;
                    }

                    int randNum = world.rand.nextInt(totalMeteorWeight);

                    boolean hasPlacedBlock = false;

                    for (MeteorParadigmComponent mpc : componentList) {
                        randNum -= mpc.getChance();

                        if (randNum < 0) {
                            ItemStack blockStack = mpc.getValidBlockParadigm();
                            if (blockStack != null && blockStack.getItem() instanceof ItemBlock) {
                                ((ItemBlock) blockStack.getItem()).placeBlockAt(
                                        blockStack,
                                        null,
                                        world,
                                        x + i,
                                        y + j,
                                        z + k,
                                        0,
                                        0,
                                        0,
                                        0,
                                        blockStack.getItemDamage());
                                if (AlchemicalWizardry.isGregTechLoaded)
                                    setGTOresNaturalIfNeeded(world, x + i, y + j, z + k);
                                world.markBlockForUpdate(x + i, y + j, z + k);
                                hasPlacedBlock = true;
                                break;
                            }
                            // world.setBlock(x + i, y + j, z + k,
                            // Block.getBlockById(Item.getIdFromItem(blockStack.getItem())), blockStack.getItemDamage(),
                            // 3);
                            // hasPlacedBlock = true;
                            // break;
                        }
                    }

                    if (!hasPlacedBlock) {
                        float randChance = rand.nextFloat() * totalChance;

                        if (randChance < iceChance) {
                            world.setBlock(x + i, y + j, z + k, Blocks.ice, 0, 3);
                        } else {
                            randChance -= iceChance;

                            if (randChance < soulChance) {
                                switch (rand.nextInt(3)) {
                                    case 0:
                                        world.setBlock(x + i, y + j, z + k, Blocks.soul_sand, 0, 3);
                                        break;
                                    case 1:
                                        world.setBlock(x + i, y + j, z + k, Blocks.glowstone, 0, 3);
                                        break;
                                    case 2:
                                        world.setBlock(x + i, y + j, z + k, Blocks.netherrack, 0, 3);
                                        break;
                                }
                            } else {
                                randChance -= soulChance;

                                if (randChance < obsidChance) {
                                    world.setBlock(x + i, y + j, z + k, Blocks.obsidian, 0, 3);
                                } else {
                                    randChance -= obsidChance;

                                    world.setBlock(x + i, y + j, z + k, Blocks.stone, 0, 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Optional.Method(modid = "gregtech")
    private static void setGTOresNaturalIfNeeded(World world, int x, int y, int z) {
        final TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityOres) {
            ((TileEntityOres) tileEntity).mNatural = true;
        }
    }
}
