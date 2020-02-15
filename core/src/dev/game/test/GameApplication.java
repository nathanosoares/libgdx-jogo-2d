package dev.game.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dev.game.test.net.GameNet;
import dev.game.test.net.client.ClientConnectionHandler;
import dev.game.test.screens.GameScreen;
import lombok.Getter;

import java.util.Random;

public class GameApplication extends Game {

    @Getter
    private GameNet net;

    private Screen gameScreen;

    //

    @Getter
    private String username;

    public GameApplication(String[] args) {
        if(System.getProperty("username") != null) {
            this.username = System.getProperty("username");
        } else {
            this.username = String.format("Player%d", new Random().nextInt(1000));
        }

        System.out.println(String.format("Hello %s", this.username));
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.net = new ClientConnectionHandler();
            this.gameScreen = new GameScreen(this);

            this.setScreen(this.gameScreen);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
