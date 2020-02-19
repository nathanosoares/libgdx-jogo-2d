package dev.game.test.client.setups;

import dev.game.test.client.ClientApplication;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.Setup;

public class SetupGameScreen implements Setup {

    @Override
    public void setup(GameApplication application) {

        ClientApplication clientApplication = (ClientApplication) application;

        int mapWidth = 20;
        int mapHeight = 20;

        GameScreen screenGame = new GameScreen(clientApplication);
        clientApplication.getScreenManager().setCurrentScreen(screenGame);
    }
}
