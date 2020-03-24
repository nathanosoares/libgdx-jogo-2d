package dev.game.test.api.block;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;

public interface IBlockState {

    IBlock getBlock();

    IWorld getWorld();

    IWorldLayer getLayer();

    GridPoint2 getPosition();

    int getConnectedData();

    void setConnectedData(int data);
}
