package dev.game.test.api.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.util.Identifiable;
import dev.game.test.api.world.IWorld;

public interface IEntity extends Identifiable {

    Entity getHandle();

    boolean isSpawned();

    IWorld getWorld();

    void setWorld(IWorld world);

    Vector2 getPosition();

    Vector2 setPosition(Vector2 vec);

    void setVelocity(Vector2 vec);

    default void onSpawn(IWorld world) {}

    default void onDestroy(IWorld world) {}
}
