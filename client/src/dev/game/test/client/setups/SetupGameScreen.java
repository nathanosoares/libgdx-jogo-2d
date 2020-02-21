package dev.game.test.client.setups;

import com.google.inject.Inject;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.Setup;

public class SetupGameScreen implements Setup {

    @Inject
    private GameApplication application;

    @Override
    public void setup() {

        GameScreen screenGame = new GameScreen((ClientApplication) application);
        ((ClientApplication) application).getScreenManager().setCurrentScreen(screenGame);
    }
}
