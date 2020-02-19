package dev.game.test.api.block;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;

public interface IBlockState {

    IBlock getBlock();

    IWorld getWorld();

    IWorldLayer getLayer();

    Vector2 getPosition();
}