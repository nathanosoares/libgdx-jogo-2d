package dev.game.test.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dev.game.test.client.block.BlockClient;
import dev.game.test.client.block.BlocksRegistry;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.client.screens.ScreenManager;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupGameScreen;
import dev.game.test.core.GameApplication;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class ClientApplication extends GameApplication<ClientApplication> implements ApplicationListener {

    @Getter
    private static ClientApplication instance;

    @Getter
    private ScreenManager screenManager;

    @Getter
    @Setter
    private BlocksRegistry blocksRegistry;

    @Setter
    @Getter
    private Screen gameScreen;

    @Getter
    private String username;

    public ClientApplication(String[] args) {
        super(true);
        instance = this;

        if (System.getProperty("username") != null) {
            this.username = System.getProperty("username");
        } else {
            this.username = String.format("Player%d", new Random().nextInt(1000));
        }

        System.out.println(String.format("Hello %s", this.username));

        this.screenManager = new ScreenManager(this);
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.getRegistryManager()
                    .addRegistry(BlockClient.class, new RegistryBlocks());

            this.setupDefaultPipeline();

            this.getSetupPipeline()
                    .registerSetup(new SetupBlocks())
                    .registerSetup(new SetupGameScreen());

            this.getSetupPipeline().runAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (this.screenManager.getCurrentScreen() != null) {
            this.screenManager.getCurrentScreen().resize(width, height);
        }
    }

    @Override
    public void render() {
        if (this.screenManager.getCurrentScreen() != null) {
            this.screenManager.getCurrentScreen().render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void pause() {
        if (this.screenManager.getCurrentScreen() != null) {
            this.screenManager.getCurrentScreen().pause();
        }
    }

    @Override
    public void resume() {
        if (this.screenManager.getCurrentScreen() != null) {
            this.screenManager.getCurrentScreen().resume();
        }
    }

    @Override
    public void dispose() {
        this.screenManager.dispose();
    }
}
