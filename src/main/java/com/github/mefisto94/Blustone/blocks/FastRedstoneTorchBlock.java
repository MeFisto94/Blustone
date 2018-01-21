package com.github.mefisto94.Blustone.blocks;

import com.github.mefisto94.Blustone.BlockStateFlags;
import com.github.mefisto94.Blustone.Blustone;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class FastRedstoneTorchBlock extends BlockRedstoneTorch {
    public FastRedstoneTorchBlock(boolean isOn) {
        super(isOn);
        setRegistryName(Blustone.MODID, "fast-redstone-torch" + (isOn ? "-on" : "-off"));
        setUnlocalizedName(Blustone.MODID + "." + getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        int pwr = super.getWeakPower(blockState, blockAccess, pos, side);
        //return this.isOn && blockState.getValue(FACING) != side ? Blustone.WEAK_POWER : 0;
        return pwr == 0 ? 0 : Blustone.WEAK_POWER;
    }

    @Override
    public int tickRate(World worldIn) {
        return 0;
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blustone.ITEM_BLOCK_TORCH_ON;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(Blustone.ITEM_BLOCK_TORCH_ON);
    }

    @Override
    public boolean isAssociatedBlock(Block other) {
        return super.isAssociatedBlock(other) || other == Blustone.FAST_TORCH_BLOCK_LIT || other == Blustone.FAST_TORCH_BLOCK_UNLIT;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        // Since we cannot replicate the logic of updateTick again, we'll just call it and change the blockState again.
        Block bTorch = worldIn.getBlockState(pos).getBlock();
        if (bTorch.equals(Blocks.UNLIT_REDSTONE_TORCH)) {
            worldIn.setBlockState(pos, Blustone.FAST_TORCH_BLOCK_UNLIT.getDefaultState().withProperty(FACING, state.getValue(FACING)), BlockStateFlags.FLAG_SEND_CHANGES_TO_CLIENTS | BlockStateFlags.FLAG_BLOCK_UPDATE);
        } else if (bTorch.equals(Blocks.REDSTONE_TORCH)) {
            worldIn.setBlockState(pos, Blustone.FAST_TORCH_BLOCK_LIT.getDefaultState().withProperty(FACING, state.getValue(FACING)), BlockStateFlags.FLAG_SEND_CHANGES_TO_CLIENTS | BlockStateFlags.FLAG_BLOCK_UPDATE);
        }
    }
}
