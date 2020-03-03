package dev.game.test.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import dev.game.test.api.IApplication;
import dev.game.test.api.IGame;
import dev.game.test.core.setup.SetupPipeline;
import dev.game.test.core.setup.impl.PacketPayloadSetup;
import dev.game.test.core.setup.impl.SetupKeybinds;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class GameApplication<G extends IGame> extends ApplicationAdapter implements IApplication<G> {

    protected final Class<G> gameClass;

    @Getter
    private G game;

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.game = createGame();

            this.game.setupRegistries(this.game.getRegistryManager());

            SetupPipeline setupPipeline = new SetupPipeline();
            setupPipeline(setupPipeline);
            setupPipeline.runAll();

            this.game.setupEngine(this.game.getEngine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void render() {
        super.render();

        this.game.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    protected void setupPipeline(SetupPipeline pipeline) {
        pipeline.registerSetup(new SetupKeybinds(this.game));
        pipeline.registerSetup(new PacketPayloadSetup(this.game));
    }

    protected G createGame() throws Exception {
        return this.gameClass.newInstance();
    }
}
