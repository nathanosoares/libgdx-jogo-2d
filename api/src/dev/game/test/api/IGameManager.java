package dev.game.test.api;

import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;

import java.util.List;
import java.util.UUID;

public interface IGameManager {

    List<IWorld> getWorlds();

    void addWorld(IWorld world);

    IWorld getWorld(String worldName);

    IEntity getEntity(UUID uuid);

    void addEntity(IEntity entity);

    void removeEntity(IEntity entity);

}
