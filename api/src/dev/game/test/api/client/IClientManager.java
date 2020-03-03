package dev.game.test.api.client;

import dev.game.test.api.IGameManager;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.IConnectionManager;
import dev.game.test.api.world.IWorld;

import java.util.List;
import java.util.UUID;

public interface IClientManager extends IGameManager {

    IWorld getCurrentWorld();

    void setCurrentWorld(IWorld world);

    IPlayer getPlayer();

    void setPlayer(IPlayer player);

    IEntity getEntity(UUID uuid);

    void addEntity(IEntity entity);

    void removeEntity(IEntity entity);
}
