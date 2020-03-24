package dev.game.test.api.block;

import com.badlogic.gdx.physics.box2d.Body;

public interface IPhysicBlockState extends IBlockState {

    Body getBody();

    void createPhysics(com.badlogic.gdx.physics.box2d.World box2dWorld);
}
