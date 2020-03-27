package dev.game.test.api.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import dev.game.test.api.util.Identifiable;
import dev.game.test.api.world.IWorld;

public interface IEntity extends Identifiable {

    boolean isSpawned();

    IWorld getWorld();

    void setPosition(Vector2 vec);

    void setPosition(IWorld world, Vector2 vec);

    Vector2 getPosition(Vector2 vector2);

    Vector2 getPosition();

    float getAltitude();

    void setVelocity(Vector2 vec);

    double getDirection();

    void setDirection(double degrees);

    Vector2 getVelocity();

    EnumEntityType getType();

    Shape createShape();

    float getWidth();

    float getHeight();

    default void onSpawn(IWorld world) {
    }

    default void onDestroy(IWorld world) {
    }
}
