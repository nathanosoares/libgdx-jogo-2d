package dev.game.test.setups;

import dev.game.test.GameApplication;
import dev.game.test.screens.GameScreen;
import dev.game.test.world.entity.Player;

import java.util.UUID;

public class SetupGameScreen extends Setup {

    @Override
    void setup(GameApplication application) {
        int mapWidth = 20;
        int mapHeight = 20;

//            this.world = new World("world", mapWidth, mapHeight);

        application.setPlayer(new Player(UUID.randomUUID()));
        GameScreen gameScreen = new GameScreen(application.getPlayer(), application);

        application.setGameScreen(gameScreen);
        application.setScreen(gameScreen);
    }
}
