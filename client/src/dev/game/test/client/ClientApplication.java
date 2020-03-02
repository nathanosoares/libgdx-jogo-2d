package dev.game.test.client;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupGameScreen;
import dev.game.test.client.world.animations.OpacityAccessor;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.SetupPipeline;
import dev.game.test.core.utils.DummyConnection;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

public class ClientApplication extends GameApplication<ClientGame> {

    @Getter
    private static ClientApplication instance;

    @Getter
    private TweenManager tweenManager;

    public static EmbeddedServerApplication EMBEDDED_SERVER;

    public static final Connection DUMMY_CONNECTION = new DummyConnection();

    private final FPSLogger fpsLogger = new FPSLogger(false, true);

    public ClientApplication() {
        super(ClientGame.class);
        instance = this;

        EMBEDDED_SERVER = new EmbeddedServerApplication(this);
    }

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

        Tween.registerAccessor(AtomicReference.class, new OpacityAccessor());
        this.tweenManager = new TweenManager();


        EMBEDDED_SERVER.create();
        EMBEDDED_SERVER.getGame().getConnectionHandler().createHandler(DUMMY_CONNECTION);

        getGame().getConnectionHandler().createHandler(DUMMY_CONNECTION);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        getGame().getScreenManager().getCurrentScreen().resize(width, height);

        EMBEDDED_SERVER.resize(width, height);
    }

    @Override
    public void render() {
        GameUtils.clearScreen(0, 0, 0, 0);
        super.render();

        this.tweenManager.update(Gdx.graphics.getDeltaTime());

        this.fpsLogger.log();

        getGame().getScreenManager().getCurrentScreen().render(Gdx.graphics.getDeltaTime());

        EMBEDDED_SERVER.render();
    }

    @Override
    public void pause() {
        super.pause();

        getGame().getScreenManager().getCurrentScreen().pause();

        EMBEDDED_SERVER.pause();
    }

    @Override
    public void resume() {
        super.resume();

        getGame().getScreenManager().getCurrentScreen().resume();

        EMBEDDED_SERVER.resume();
    }

    @Override
    public void dispose() {
        super.dispose();

        getGame().getScreenManager().getCurrentScreen().dispose();

        EMBEDDED_SERVER.dispose();
    }
}
