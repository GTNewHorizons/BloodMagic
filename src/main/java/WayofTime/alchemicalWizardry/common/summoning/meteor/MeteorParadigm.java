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
    public List<MeteorParadigmComponent> fillerList = new ArrayList<>();
    public ItemStack focusStack;
    public int radius;
    public int cost;
    public int fillerChance; // Out of 100

    public static Random rand = new Random();

    public MeteorParadigm(ItemStack focusStack, int radius, int cost) {
        new MeteorParadigm(focusStack, radius, cost, 0);
    }

    public MeteorParadigm(ItemStack focusStack, int radius, int cost, int fillerChance) {
        this.focusStack = focusStack;
        this.radius = radius;
        this.cost = cost;
        this.fillerChance = fillerChance;
    }

    // modId:itemName:meta:weight
    private static final Pattern itemNamePattern = Pattern.compile("(.*):(.*):(\\d+):(\\d+)");
    // OREDICT:oreDictName:weight
    private static final Pattern oredictPattern = Pattern.compile("OREDICT:(.*):(\\d+)");

    public void parseStringArray(String[] componentArray) {
        parseStringArray(componentArray, false);
    }

    public void parseStringArray(String[] blockArray, boolean filler) {
        List<MeteorParadigmComponent> addList = filler ? fillerList : componentList;
        for (int i = 0; i < blockArray.length; ++i) {
            String blockName = blockArray[i];
            boolean success = false;

            Matcher matcher = itemNamePattern.matcher(blockName);
            if (matcher.matches()) {
                String modID = matcher.group(1);
                String itemName = matcher.group(2);
                int meta = Integer.parseInt(matcher.group(3));
                int weight = Integer.parseInt(matcher.group(4));

                ItemStack stack = GameRegistry.findItemStack(modID, itemName, 1);
                if (stack != null && stack.getItem() instanceof ItemBlock) {
                    stack.setItemDamage(meta);
                    addList.add(new MeteorParadigmComponent(stack, weight));
                    success = true;
                }

            } else if ((matcher = oredictPattern.matcher(blockName)).matches()) {
                String oreDict = matcher.group(1);
                int weight = Integer.parseInt(matcher.group(2));

                List<ItemStack> list = OreDictionary.getOres(oreDict);
                for (ItemStack stack : list) {
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        addList.add(new MeteorParadigmComponent(stack, weight));
                        success = true;
                        break;
                    }
                }

            } else {
                // Legacy config
                String oreDict = blockName;
                int weight = Integer.parseInt(blockArray[++i]);

                List<ItemStack> list = OreDictionary.getOres(oreDict);
                for (ItemStack stack : list) {
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        addList.add(new MeteorParadigmComponent(stack, weight));
                        success = true;
                        break;
                    }
                }
            }

            if (!success) {
                AlchemicalWizardry.logger.warn("Unable to add Meteor Paradigm \"" + blockName + "\"");
            }
        }
    }

    public int getTotalListWeight(List<MeteorParadigmComponent> blockList) {
        int totalWeight = 0;
        for (MeteorParadigmComponent mpc : blockList) {
            totalWeight += mpc.getWeight();
        }
        return totalWeight;
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
        int fillerChance = this.fillerChance;
        if (hasOrbisTerrae) {
            newRadius += 2;
            fillerChance *= 1.12;
        } else if (hasTerrae) {
            newRadius += 1;
            fillerChance *= 1.06;
        }
        if (fillerChance > 100) {
            fillerChance = 100;
        }

        world.createExplosion(null, x, y, z, newRadius * 4, AlchemicalWizardry.doMeteorsDestroyBlocks);

        List<MeteorParadigmComponent> fillerList;

        if (hasCrystallos || hasIncendium || hasTennebrae) {
            fillerList = new ArrayList<>();
            if (hasCrystallos) {
                fillerList.add(new MeteorParadigmComponent(new ItemStack(Blocks.ice), 180)); // 180 = 2^2 * 3^2 * 5
            }
            if (hasIncendium) {
                fillerList.add(new MeteorParadigmComponent(new ItemStack(Blocks.netherrack), 60));
                fillerList.add(new MeteorParadigmComponent(new ItemStack(Blocks.soul_sand), 60));
                fillerList.add(new MeteorParadigmComponent(new ItemStack(Blocks.glowstone), 60));
            }
            if (hasTennebrae) {
                fillerList.add(new MeteorParadigmComponent(new ItemStack(Blocks.obsidian), 180));
            }
        } else {
            fillerList = this.fillerList;
        }

        int totalComponentWeight = getTotalListWeight(componentList);
        int totalFillerWeight = getTotalListWeight(fillerList);

        for (int i = -newRadius; i <= newRadius; i++) {
            for (int j = -newRadius; j <= newRadius; j++) {
                for (int k = -newRadius; k <= newRadius; k++) {
                    if (i * i + j * j + k * k >= (newRadius + 0.50f) * (newRadius + 0.50f)) {
                        continue;
                    }

                    if (!world.isAirBlock(x + i, y + j, z + k)) {
                        continue;
                    }

                    if (fillerChance <= 0 || world.rand.nextInt(100) >= fillerChance) {
                        setMeteorBlock(x + i, y + j, z + k, world, componentList, totalComponentWeight);
                    } else {
                        setMeteorBlock(x + i, y + j, z + k, world, fillerList, totalFillerWeight);
                    }
                }
            }
        }
    }

    private void setMeteorBlock(int x, int y, int z, World world, List<MeteorParadigmComponent> blockList,
            int totalListWeight) {
        int randNum = world.rand.nextInt(totalListWeight);
        for (MeteorParadigmComponent mpc : blockList) {
            randNum -= mpc.getWeight();

            if (randNum < 0) {
                ItemStack blockStack = mpc.getValidBlockParadigm();
                if (blockStack != null && blockStack.getItem() instanceof ItemBlock) {
                    ((ItemBlock) blockStack.getItem())
                            .placeBlockAt(blockStack, null, world, x, y, z, 0, 0, 0, 0, blockStack.getItemDamage());
                    if (AlchemicalWizardry.isGregTechLoaded) setGTOresNaturalIfNeeded(world, x, y, z);
                    world.markBlockForUpdate(x, y, z);
                    break;
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
