package dev.game.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.net.ConnectionHandler;
import com.badlogic.gdx.physics.box2d.Box2D;
import dev.game.test.net.client.ClientConnectionHandler;
import dev.game.test.screens.GameScreen;
import dev.game.test.world.World;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.block.BlocksRegistry;
import dev.game.test.world.entity.Player;
import lombok.Getter;

import java.util.Random;
import java.util.UUID;

public class GameApplication extends Game {

    @Getter
    private static GameApplication instance;

    @Getter
    private ConnectionHandler net;

    @Getter
    private BlocksRegistry blocksRegistry;

    private Screen gameScreen;

    //

    @Getter
    private String username;

    @Getter
    private Player player;

    @Getter
    private World world;

    public GameApplication(String[] args) {
        if (System.getProperty("username") != null) {
            this.username = System.getProperty("username");
        } else {
            this.username = String.format("Player%d", new Random().nextInt(1000));
        }

        System.out.println(String.format("Hello %s", this.username));

        instance = this;
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            Box2D.init();

            this.net = new ClientConnectionHandler(this);

            this.player = new Player(UUID.randomUUID());

            int mapWidth = 20;
            int mapHeight = 20;

            this.world = new World("world", mapWidth, mapHeight);

            this.gameScreen = new GameScreen(this.world, this.player, this);
            this.blocksRegistry = new BlocksRegistry();

            this.blocksRegistry.registerBlock(0, Blocks.AIR);
            this.blocksRegistry.registerBlock(1, Blocks.DIRT);
            this.blocksRegistry.registerBlock(2, Blocks.STONE);
            this.blocksRegistry.registerBlock(3, Blocks.WATER);
            this.blocksRegistry.registerBlock(4, Blocks.GRASS);

            this.setScreen(this.gameScreen);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
