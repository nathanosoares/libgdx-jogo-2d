package dev.game.test.api.entity;

import com.badlogic.ashley.core.Entity;
import dev.game.test.api.util.Identifiable;
import dev.game.test.api.world.IWorld;

public interface IEntity extends Identifiable {

    Entity getHandle();

    boolean isSpawned();

    IWorld getWorld();

    default void onSpawn(IWorld world) {}

    default void onDestroy(IWorld world) {}
}
