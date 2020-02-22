package dev.game.test.api;

import dev.game.test.api.client.screens.IScreenManager;

public interface IClientGame extends IGame {

    IScreenManager getScreenManager();

}
