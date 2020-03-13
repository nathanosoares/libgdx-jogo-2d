package dev.game.test.client;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.google.common.primitives.Ints;
import dev.game.test.client.setups.SetupBlocks;
import dev.game.test.client.setups.SetupEntities;
import dev.game.test.client.world.animations.OpacityAccessor;
import dev.game.test.core.GameApplication;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class ClientApplication extends GameApplication<ClientGame> {

    @Getter
    private static ClientApplication instance;

    @Getter
    private TweenManager tweenManager;

    private final FPSLogger fpsLogger = new FPSLogger(false, true);

    public ClientApplication() {
        super(ClientGame.class);
        instance = this;
    }

    @Override
    protected void setupPipeline(SetupPipeline pipeline) {
        super.setupPipeline(pipeline);

        pipeline.registerSetup(new SetupBlocks(this.getGame()));
        pipeline.registerSetup(new SetupEntities());
    }

    @Override
    public void create() {
        super.create();

        Tween.registerAccessor(AtomicReference.class, new OpacityAccessor());
        this.tweenManager = new TweenManager();

        try {
            String ip = System.getProperty("ip");
            String rawPort = System.getProperty("port");

            if (rawPort != null && Ints.tryParse(rawPort) != null) {
                getGame().getConnectionHandler().connect(ip, Ints.tryParse(rawPort));
                return;
            }

            throw new RuntimeException();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        if (getGame().getScreenManager().getCurrentScreen() != null) {
            getGame().getScreenManager().getCurrentScreen().resize(width, height);
        }
    }

    @Override
    public void render() {
        if (getGame().getScreenManager().getCurrentScreen() != null) {
            getGame().getScreenManager().getCurrentScreen().render(Gdx.graphics.getDeltaTime());
        }

        super.render();

        this.tweenManager.update(Gdx.graphics.getDeltaTime());

        this.fpsLogger.log();
    }

    @Override
    public void pause() {
        super.pause();

        if (getGame().getScreenManager().getCurrentScreen() != null) {
            getGame().getScreenManager().getCurrentScreen().pause();
        }
    }

    @Override
    public void resume() {
        super.resume();

        if (getGame().getScreenManager().getCurrentScreen() != null) {
            getGame().getScreenManager().getCurrentScreen().resume();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (getGame().getScreenManager().getCurrentScreen() != null) {
            getGame().getScreenManager().getCurrentScreen().dispose();
        }
    }
}
