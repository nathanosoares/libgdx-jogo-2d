package dev.game.test.client;

import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2D;
import dev.game.test.client.block.BlocksRegistry;
import dev.game.test.client.net.ConnectionHandler;
import dev.game.test.client.net.client.ClientConnectionHandler;
import dev.game.test.client.setups.RegistrySetups;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupGameScreen;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

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


    public GameApplication(String[] args) {
        if (System.getProperty("username") != null) {
            this.username = System.getProperty("username");
        } else {
            this.username = String.format("Player%d", new Random().nextInt(1000));
        }

        System.out.println(String.format("Hello %s", this.username));

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder();

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
