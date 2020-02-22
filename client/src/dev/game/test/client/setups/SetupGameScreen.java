package dev.game.test.client.setups;

import dev.game.test.api.IClientGame;
import dev.game.test.client.screens.GameScreen;
import dev.game.test.core.setup.Setup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetupGameScreen implements Setup {

    private final IClientGame clientGame;

    @Override
    public void setup() {
        GameScreen screenGame = new GameScreen(clientGame);
        clientGame.getScreenManager().setCurrentScreen(screenGame);
    }
}
