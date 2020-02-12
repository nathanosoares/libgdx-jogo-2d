package dev.game.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dev.game.test.net.GameNet;
import dev.game.test.net.client.ClientGameNet;
import dev.game.test.screens.GameScreen;
import lombok.Getter;

public class GameApplication extends Game {

    @Getter
    private GameNet net;

    private Screen gameScreen;


    public GameApplication(String[] args) {
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.net = new ClientGameNet();
            this.gameScreen = new GameScreen(this);

            this.setScreen(this.gameScreen);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
