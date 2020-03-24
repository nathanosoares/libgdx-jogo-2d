package dev.game.test.core.block.states;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.block.IPhysicBlockState;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import lombok.Getter;

public abstract class PhysicBlockState extends BlockState implements IPhysicBlockState {

    @Getter
    protected Body body;

    @Getter
    protected final Vector2 bodyOffset = new Vector2();

    public PhysicBlockState(IBlock block, IWorld world, IWorldLayer layer, GridPoint2 position) {
        super(block, world, layer, position);
    }

    @Override
    public void onHit(IEntity entity) {

    }
}
