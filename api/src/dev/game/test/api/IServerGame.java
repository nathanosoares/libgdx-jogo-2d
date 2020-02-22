package dev.game.test.api;

import dev.game.test.api.server.IServerManager;
import dev.game.test.api.server.handler.IServerConnectionHandler;
import dev.game.test.api.world.IWorld;

public interface IServerGame extends IGame {

    IServerManager getServerManager();

    IServerConnectionHandler getConnectionHandler();

}
