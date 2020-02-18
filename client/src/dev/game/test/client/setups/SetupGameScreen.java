package dev.game.test.client.setups;

import dev.game.test.client.GameApplication;
import dev.game.test.client.screens.GameScreen;

public class SetupGameScreen extends Setup {

    @Override
    void setup(GameApplication application) {
        int mapWidth = 20;
        int mapHeight = 20;

        GameScreen gameScreen = new GameScreen(application);

        application.setGameScreen(gameScreen);
        application.setScreen(gameScreen);
    }
}
