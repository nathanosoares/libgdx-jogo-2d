package dev.game.test.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Connection;
import dev.game.test.api.IClientGame;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupGameScreen;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.SetupPipeline;
import dev.game.test.core.utils.DummyConnection;
import lombok.Getter;
import lombok.Setter;

public class ClientApplication extends GameApplication<ClientGame> {

    public static EmbeddedServerApplication EMBEDDED_SERVER;

    public static final Connection DUMMY_CONNECTION = new DummyConnection();

    //

    @Setter
    @Getter
    private Screen gameScreen;

    //

    private final FPSLogger fpsLogger = new FPSLogger(false, true);

    //


    //

    public ClientApplication() {
        super(ClientGame.class);

        EMBEDDED_SERVER = new EmbeddedServerApplication(this);
    }

    //

    @Override
    protected void setupPipeline(SetupPipeline pipeline) {
        super.setupPipeline(pipeline);

        pipeline
                .registerSetup(new SetupBlocks(this.getGame()))
                .registerSetup(new SetupGameScreen(this.getGame()));
    }

    /*

     */

    @Override
    public void create() {
        super.create();

        EMBEDDED_SERVER.create();
        EMBEDDED_SERVER.getGame().getConnectionHandler().createHandler(DUMMY_CONNECTION);
    }

    @Override
    public void resize(int width, int height) {
        getGame().getScreenManager().getCurrentScreen().resize(width, height);

        EMBEDDED_SERVER.resize(width, height);
    }

    @Override
    public void render() {
        this.fpsLogger.log();

        getGame().getScreenManager().getCurrentScreen().render(Gdx.graphics.getDeltaTime());
        EMBEDDED_SERVER.render();
    }

    @Override
    public void pause() {
        getGame().getScreenManager().getCurrentScreen().pause();
        EMBEDDED_SERVER.pause();
    }

    @Override
    public void resume() {
        getGame().getScreenManager().getCurrentScreen().resume();
        EMBEDDED_SERVER.resume();
    }

    @Override
    public void dispose() {
        getGame().getScreenManager().getCurrentScreen().dispose();
        EMBEDDED_SERVER.dispose();
    }
}
