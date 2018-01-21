package com.github.mefisto94.Blustone.blocks;

import com.github.mefisto94.Blustone.Blustone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class UnlimitedBlockRedstoneWire extends BlockRedstoneWire {
    public UnlimitedBlockRedstoneWire() {
        setRegistryName(Blustone.MODID, "unlimited-wire");
        setUnlocalizedName(Blustone.MODID + "." + getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    /*@Override
    public int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
    {
        if (worldIn.getBlockState(pos).getBlock() != this) {
            return strength;
        } else {
            int i = ((Integer)worldIn.getBlockState(pos).getValue(POWER)).intValue();
            if (i > 0) {
                i = 15;
            }
            return i > strength ? i : strength;
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        int wp = super.getWeakPower(blockState, blockAccess, pos, side);
        return wp == 0 ? 0 : Blustone.WEAK_POWER; // No beautiful overrides possible because of private values
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int i = ((Integer)stateIn.getValue(POWER)).intValue();

        if (i != 0)
        {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / Blustone.WEAK_POWER;
            float f3 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f1 = Math.max(0.0F, f * f * 0.6F - 0.7F) - 1.0f;
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3, new int[0]);
        }
    }
}
