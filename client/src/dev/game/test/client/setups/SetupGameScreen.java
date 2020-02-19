package dev.game.test.client.setups;

import com.artemis.annotations.Wire;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.core.setup.Setup;

public class SetupGameScreen implements Setup {

    @Wire
    private ClientApplication application;

    @Override
    public void setup() {

        GameScreen screenGame = new GameScreen(application);
        application.getScreenManager().setCurrentScreen(screenGame);
    }
}
