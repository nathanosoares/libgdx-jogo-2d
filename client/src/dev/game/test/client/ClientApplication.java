package dev.game.test.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.client.block.BlockClient;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupGameScreen;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;
import lombok.Setter;

public class ClientApplication extends GameApplication<ClientGame> {

    @Setter
    @Getter
    private Screen gameScreen;

    //

    private final FPSLogger fpsLogger = new FPSLogger(false, true);

    //

    public ClientApplication() {
        super(ClientGame.class);
    }

    //

    @Override
    protected void setupPipeline(SetupPipeline pipeline) {
        super.setupPipeline(pipeline);

        pipeline
                .registerSetup(new SetupBlocks(this.getGame()))
                .registerSetup(new SetupGameScreen(this.getGame()));
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void resize(int width, int height) {
        getGame().getScreenManager().getCurrentScreen().resize(width, height);
    }

    @Override
    public void render() {
        this.fpsLogger.log();
        getGame().getScreenManager().getCurrentScreen().render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {
        getGame().getScreenManager().getCurrentScreen().pause();
    }

    @Override
    public void resume() {
        getGame().getScreenManager().getCurrentScreen().resume();
    }

    @Override
    public void dispose() {
        getGame().getScreenManager().getCurrentScreen().dispose();
    }
}
