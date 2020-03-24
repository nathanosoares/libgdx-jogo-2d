package dev.game.test.api.block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import dev.game.test.api.entity.IEntity;

public interface IPhysicBlockState extends IBlockState {

    Body getBody();

    Vector2 getBodyOffset();

    void createPhysics(com.badlogic.gdx.physics.box2d.World box2dWorld);

    void onHit(IEntity entity);
}
