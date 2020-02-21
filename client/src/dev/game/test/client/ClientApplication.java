package dev.game.test.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.google.inject.Singleton;
import dev.game.test.client.block.BlockClient;
import dev.game.test.client.block.BlocksRegistry;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.client.screens.ScreenManager;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupGameScreen;
import dev.game.test.core.GameApplication;
import dev.game.test.core.registry.RegistryManager;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Singleton
public class ClientApplication extends GameApplication {

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

    private final FPSLogger fpsLogger = new FPSLogger(false, true);

    public ClientApplication(String[] args) {
        super(true);
        instance = this;
    }

    @Override
    public void setupManagers() {
        super.setupManagers();

        this.screenManager = new ScreenManager(this);
    }

    @Override
    protected void setupPipeline(SetupPipeline pipeline) {
        super.setupPipeline(pipeline);

        pipeline
                .registerSetup(new SetupBlocks())
                .registerSetup(new SetupGameScreen());
    }

    @Override
    protected void setupRegistries(RegistryManager registryManager) {
        super.setupRegistries(registryManager);

        registryManager.addRegistry(BlockClient.class, new RegistryBlocks());
    }

    @Override
    public void create() {
        super.create();

        if (System.getProperty("username") != null) {
            this.username = System.getProperty("username");
        } else {
            this.username = String.format("Player%d", new Random().nextInt(1000));
        }

        System.out.println(String.format("Hello %s", this.username));
    }

    @Override
    public void resize(int width, int height) {
        if (this.screenManager.getCurrentScreen() != null) {
            this.screenManager.getCurrentScreen().resize(width, height);
        }
    }

    @Override
    public void render() {
        this.fpsLogger.log();

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
