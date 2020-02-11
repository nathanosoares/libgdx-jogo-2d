package dev.game.test;

import com.badlogic.gdx.Game;
import dev.game.test.screens.GameScreen;

public class GameApplication extends Game {

    private GameScreen gameScreen;

    @Override
    public void create() {
        this.gameScreen = new GameScreen();

        this.setScreen(this.gameScreen);
    }
}
