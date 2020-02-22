package dev.game.test.api.client;

import dev.game.test.api.IGameManager;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;

public interface IClientManager extends IGameManager {

    IWorld getCurrentWorld();

    void setCurrentWorld(IWorld world);

    IPlayer getPlayer();

}
