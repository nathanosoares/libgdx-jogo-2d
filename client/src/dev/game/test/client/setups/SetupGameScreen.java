package dev.game.test.client.setups;

import com.artemis.annotations.Wire;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.Setup;

public class SetupGameScreen implements Setup {

    @Wire
    private GameApplication application;

    @Override
    public void setup() {

        ClientApplication clientApplication = (ClientApplication) application;

        int mapWidth = 20;
        int mapHeight = 20;

        GameScreen screenGame = new GameScreen(clientApplication);
        clientApplication.getScreenManager().setCurrentScreen(screenGame);
    }
}
