package dev.game.test.api.world;

import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;

public interface IWorldLayer {

    IWorld getWorld();

    IBlockState[][] getStates();

    IBlockState getBlockState(int x, int y);

    IBlockState getBlockState(float x, float y);

    void setBlockState(IBlockState blockState);

    boolean isOrigin(int x, int y);

    IBlockState getFacingBlock(IBlockState blockState, EnumFacing... facings);
}
