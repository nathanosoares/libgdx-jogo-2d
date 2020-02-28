package dev.game.test.api;

import dev.game.test.api.client.IClientManager;
import dev.game.test.api.client.handler.IClientConnectionHandler;
import dev.game.test.api.client.screens.IScreenManager;

public interface IClientGame extends IGame {

    IScreenManager getScreenManager();

    IClientManager getClientManager();

    IClientConnectionHandler getConnectionHandler();

    String getUsername();

}
