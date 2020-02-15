package dev.game.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dev.game.test.net.GameNet;
import dev.game.test.net.client.ClientGameNet;
import dev.game.test.screens.GameScreen;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.block.impl.*;
import dev.game.test.world.block.BlocksRegistry;
import lombok.Getter;

public class GameApplication extends Game {

    @Getter
    private static GameApplication instance;

    @Getter
    private GameNet net;

    @Getter
    private BlocksRegistry blocksRegistry;

    private Screen gameScreen;


    public GameApplication(String[] args) {
        instance = this;
        this.blocksRegistry = new BlocksRegistry();

        this.blocksRegistry.registerBlock(0, Blocks.AIR);
        this.blocksRegistry.registerBlock(1, Blocks.DIRT);
        this.blocksRegistry.registerBlock(2, Blocks.STONE);
        this.blocksRegistry.registerBlock(3, Blocks.WATER);
        this.blocksRegistry.registerBlock(4, Blocks.GRASS);
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.net = new ClientGameNet();
            this.gameScreen = new GameScreen(this);

            this.setScreen(this.gameScreen);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
