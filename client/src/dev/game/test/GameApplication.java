package dev.game.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2D;
import dev.game.test.net.ConnectionHandler;
import dev.game.test.net.client.ClientConnectionHandler;
import dev.game.test.screens.GameScreen;
import dev.game.test.setups.RegistrySetups;
import dev.game.test.setups.SetupBlocks;
import dev.game.test.setups.SetupGameScreen;
import dev.game.test.world.World;
import dev.game.test.world.block.BlocksRegistry;
import dev.game.test.world.entity.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.UUID;

public class GameApplication extends Game {

    @Getter
    private static GameApplication instance;

    @Getter
    private ConnectionHandler net;

    @Getter
    @Setter
    private BlocksRegistry blocksRegistry;

    @Setter
    @Getter
    private Screen gameScreen;

    @Getter
    private String username;

    @Getter
    @Setter
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

            RegistrySetups registrySetups = new RegistrySetups();

            registrySetups.registerSetup(new SetupBlocks());
            registrySetups.registerSetup(new SetupGameScreen());

            registrySetups.runAll(this);

            this.net = new ClientConnectionHandler(this);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
