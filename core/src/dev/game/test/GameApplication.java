package dev.game.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dev.game.test.net.GameNet;
import dev.game.test.net.client.ClientGameNet;
import dev.game.test.net.server.ServerGameNet;
import dev.game.test.screens.GameScreen;
import dev.game.test.screens.ServerScreen;
import lombok.Getter;

public class GameApplication extends Game {

    @Getter
    private GameNet net;

    private Screen gameScreen;

    @Getter
    private boolean server;

    public GameApplication(String[] args) {
        this.server = args.length > 0 && args[0].equalsIgnoreCase("server");
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        if(this.server) {
            this.net = new ServerGameNet();
            this.gameScreen = new ServerScreen();

        } else {
            this.net = new ClientGameNet();
            this.gameScreen = new GameScreen(this);
        }

        this.setScreen(this.gameScreen);

        if(this.server) {
            //((ServerGameNet) this.net).start(4097);
        } else {
            //((ClientGameNet) this.net).connect("127.0.0.1", 4097);
        }
    }
}
