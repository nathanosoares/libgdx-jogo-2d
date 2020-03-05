package dev.game.test.api.entity;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.util.Identifiable;
import dev.game.test.api.world.IWorld;

public interface IEntity extends Identifiable {

    boolean isSpawned();

    IWorld getWorld();

    Vector2 getPosition();

    void setPosition(Vector2 vec);

    void setPosition(IWorld world, Vector2 vec);

    default void onSpawn(IWorld world) {
    }

    default void onDestroy(IWorld world) {
    }
}
