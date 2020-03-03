package dev.game.test.api;

import dev.game.test.api.server.IServerManager;
import dev.game.test.api.net.handler.IServerConnectionHandler;

public interface IServerGame extends IGame {

    IServerManager getServerManager();

    IServerConnectionHandler getConnectionHandler();

}
