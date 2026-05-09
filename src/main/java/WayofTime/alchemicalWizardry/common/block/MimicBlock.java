package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import WayofTime.alchemicalWizardry.common.tileEntity.TEMimicBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MimicBlock extends BlockContainer {

    public MimicBlock() {
        super(Material.water);
        setHardness(2.0F);
        setResistance(5.0F);
        this.setBlockName("blockMimic");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity TE = world.getTileEntity(x, y, z);
        if (!(TE instanceof TEMimicBlock mimic)) {
            return true;
        }

        Block block = mimic.getBlock();

        return block == null || block.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canCollideCheck(int meta, boolean bool) {
        return meta == 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntity TE = blockAccess.getTileEntity(x, y, z);
        TEMimicBlock mimic = (TEMimicBlock) TE;
        Block block = mimic.getBlock();
        int meta = mimic.getMetaOfMimic();

        return block != null ? block.getIcon(side, meta) : this.blockIcon;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TEMimicBlock();
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return false;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return 0;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TEMimicBlock) {
            Block block = ((TEMimicBlock) tile).getBlock();
            int mimicMeta = ((TEMimicBlock) tile).getMetaOfMimic();

            if (block != null) {
                return block.isBlockSolid(world, x, y, z, mimicMeta);
            }
        }
        return super.isBlockSolid(world, x, y, z, meta);
    }

    @Override
    public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
        TEMimicBlock tile = (TEMimicBlock) world.getTileEntity(target.blockX, target.blockY, target.blockZ);
        if (tile == null) {
            return super.addHitEffects(world, target, effectRenderer);
        }
        Block block = tile.getBlock();

        double xOffset = target.blockX
                + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - 0.1F * 2.0F)
                + 0.1F
                + block.getBlockBoundsMinX();
        double yOffset = target.blockY
                + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - 0.1F * 2.0F)
                + 0.1F
                + block.getBlockBoundsMinY();
        double zOffset = target.blockZ
                + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - 0.1F * 2.0F)
                + 0.1F
                + block.getBlockBoundsMinZ();

        switch (target.sideHit) {
            case 0 -> yOffset = target.blockY + block.getBlockBoundsMinY() - 0.1D;
            case 1 -> yOffset = target.blockY + block.getBlockBoundsMaxY() + 0.1D;
            case 2 -> zOffset = target.blockZ + block.getBlockBoundsMinZ() - 0.1D;
            case 3 -> zOffset = target.blockZ + block.getBlockBoundsMaxZ() + 0.1D;
            case 4 -> xOffset = target.blockX + block.getBlockBoundsMinX() - 0.1D;
            case 5 -> xOffset = target.blockX + block.getBlockBoundsMaxX() + 0.1D;
        }

        MimicBlock.addHitEffect(tile, target, xOffset, yOffset, zOffset, effectRenderer);

        return true;

    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        TEMimicBlock tile = (TEMimicBlock) world.getTileEntity(x, y, z);
        Block block = tile.getBlock();

        return block != null ? block.getCollisionBoundingBoxFromPool(world, x, y, z)
                : super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
        TEMimicBlock TE = (TEMimicBlock) blockAccess.getTileEntity(x, y, z);
        if (TE != null) {
            Block block = TE.getBlock();
            if (block != null) {
                return block.colorMultiplier(blockAccess, x, y, z);
            }
        }

        return super.colorMultiplier(blockAccess, x, y, z);
    }

    @Override
    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec) {
        TEMimicBlock tile = (TEMimicBlock) world.getTileEntity(x, y, z);
        if (tile != null) {
            Block block = tile.getBlock();
            if (block != null) {
                block.velocityToAddToEntity(world, x, y, z, entity, vec);
            }
        }
    }

    public static void addHitEffect(TEMimicBlock TE, MovingObjectPosition target, double x, double y, double z,
            EffectRenderer effectRenderer) {
        EntityDiggingFX particle = new EntityDiggingFX(
                TE.getWorldObj(),
                x,
                y,
                z,
                0.0D,
                0.0D,
                0.0D,
                TE.getBlock(),
                TE.getMetaOfMimic());
        effectRenderer.addEffect(
                particle.applyColourMultiplier(target.blockX, target.blockY, target.blockZ).multiplyVelocity(0.2F)
                        .multipleParticleScaleBy(0.6F));
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        TEMimicBlock tile = (TEMimicBlock) world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile.getBlockEffectWhileInside(entity, x, y, z)) {
                return;
            } else {
                Block block = tile.getBlock();
                if (block != null) {
                    block.onEntityCollidedWithBlock(world, x, y, z, entity);
                    return;
                }
            }
        }

        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }
}
