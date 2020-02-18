package dev.game.test.api.entity;

import dev.game.test.api.util.Identifiable;
import dev.game.test.api.world.IWorld;

public interface IEntity extends Identifiable {

    IWorld getWorld();

    void setWorld(IWorld world);
}
