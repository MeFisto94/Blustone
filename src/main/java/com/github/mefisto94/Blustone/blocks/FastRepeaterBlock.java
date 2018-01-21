package com.github.mefisto94.Blustone.blocks;

import com.github.mefisto94.Blustone.Blustone;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.logging.Logger;

public class FastRepeaterBlock extends BlockRedstoneRepeater {

    public static final Logger LOG = Logger.getLogger(FastRepeaterBlock.class.getSimpleName());

    public FastRepeaterBlock(boolean powered) {
        super(powered);
        setRegistryName(Blustone.MODID, "fast-repeater" + (powered ? "-powered" : "-unpowered"));
        setUnlocalizedName(Blustone.MODID + "." + getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    /**
     * Whether this Repeater is set on "delay = 0" aka. hyper speed.
     * This might introduce nondeterminism and lead to bugs but at least it triggers another update tick
     * which makes the repeater as fast as regular redstone wire.
     *
     * @param state The Block State required
     * @return Whether this Repeater has been set to hyper speed.
     */
    public boolean isHyperSpeed(IBlockState state) {
        return super.getDelay(state) <= 2;
    }

    @Override
    protected int getDelay(IBlockState state) {
        // since the Repeater returns "Delay * 2" (redstone ticks at 1/10s instead of 1/20s)
        int delay = super.getDelay(state);
        if (delay <= 4) {
            return 0;
        } else {
            return delay - 4;
        }
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blustone.ITEM_BLOCK_REPEATER_OFF;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Blustone.ITEM_BLOCK_REPEATER_OFF);
    }

    protected IBlockState getPoweredState(IBlockState unpoweredState) {
        Integer integer = (Integer)unpoweredState.getValue(DELAY);
        Boolean obool = (Boolean)unpoweredState.getValue(LOCKED);
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        //LOG.log(Level.WARNING, "getPoweredState");
        return Blustone.FAST_REPEATER_BLOCK_POWERED.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    protected IBlockState getUnpoweredState(IBlockState poweredState) {
        Integer integer = (Integer)poweredState.getValue(DELAY);
        Boolean obool = (Boolean)poweredState.getValue(LOCKED);
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        //LOG.log(Level.WARNING, "getUnpoweredState");
        return Blustone.FAST_REPEATER_BLOCK_UNPOWERED.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
    }

    /*
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (determinism) {
            super.updateTick(worldIn, pos, state, rand);
        } else {
            // Copied from BlockRedstoneDiode:73 (on 1.10.2) and commented and formatted
            if (!this.isLocked(worldIn, pos, state)) {
                boolean shouldBePowered = this.shouldBePowered(worldIn, pos, state); // renamed from flag.

                if (this.isRepeaterPowered && !shouldBePowered) { // Switch to off-state.
                    worldIn.setBlockState(pos, this.getUnpoweredState(state), BlockStateFlags.FLAG_SEND_CHANGES_TO_CLIENTS | BlockStateFlags.FLAG_BLOCK_UPDATE);
                } else if (!this.isRepeaterPowered) { // since we've been told to update, it seems we _have_ to be activated.
                    worldIn.setBlockState(pos, this.getPoweredState(state), BlockStateFlags.FLAG_SEND_CHANGES_TO_CLIENTS | BlockStateFlags.FLAG_BLOCK_UPDATE);

                    if (!shouldBePowered) { // not sure why this is only called when we are off and should be off?
                        // shouldBePowered is when the input redstone is on.
                        // During debugging this was never the case.
                        worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), this.getTickDelay(state), -1);
                        // this actually calls the following but with a delay of one tick
                        // iblockstate.getBlock().updateTick(this, nextticklistentry1.position, iblockstate, this.rand);
                    }
                }
            }
        }
    }*/

    // Copied from BlockRedstoneDiode:123 (on 1.10.2) and commented and formatted
    @Override
    protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
        if (!isHyperSpeed(state)) {
            super.updateState(worldIn, pos, state);
        } else {
            if (!this.isLocked(worldIn, pos, state)) {
                boolean flag = this.shouldBePowered(worldIn, pos, state);

                if ((this.isRepeaterPowered && !flag || !this.isRepeaterPowered && flag) && !worldIn.isBlockTickPending(pos, this)) {
                    /*int i = -1;

                    if (this.isFacingTowardsRepeater(worldIn, pos, state)) {
                        i = -3;
                    } else if (this.isRepeaterPowered) {
                        i = -2;
                    }*/

                    worldIn.immediateBlockTick(pos, state, worldIn.rand);
                    //worldIn.updateBlockTick(pos, this, this.getDelay(state), i);
                }
            }
        }
    }

    // Copied from BlockRedstoneRepeater:131 (on 1.10.2) and commented and formatted
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isRepeaterPowered) {
            EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
            double d0 = (double)((float)pos.getX() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.4F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            double d2 = (double)((float)pos.getZ() + 0.5F) + (double)(rand.nextFloat() - 0.5F) * 0.2D;
            float f = -5.0F;

            if (rand.nextBoolean()) {
                f = (float)(((Integer)stateIn.getValue(DELAY)).intValue() * 2 - 1);
            }

            f = f / 16.0F;
            double d3 = (double)(f * (float)enumfacing.getFrontOffsetX());
            double d4 = (double)(f * (float)enumfacing.getFrontOffsetZ());
            // Note: spawnParticle for REDSTONE treats xspeed, yspeed and zspeed as RGB Colors but internally adds (1, 0, 0).
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, -0.5D, 0.0D, 1.0D, new int[0]);
        }
    }
}
