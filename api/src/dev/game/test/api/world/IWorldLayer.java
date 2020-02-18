package dev.game.test.api.world;

import dev.game.test.api.block.IBlockState;

public interface IWorldLayer {

    IBlockState[][] getStates();

    IBlockState getBlockState(int x, int y);

    IBlockState getBlockState(float x, float y);

    boolean isOrigin(int x, int y);
}
