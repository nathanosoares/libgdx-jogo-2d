package dev.game.test.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import dev.game.test.api.IApplication;
import dev.game.test.api.IGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.core.entity.systems.MovementSystem;
import dev.game.test.core.entity.systems.PlayerStateSystem;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class GameApplication<G extends IGame> extends ApplicationAdapter implements IApplication<G> {

    protected final Class<G> gameClass;

    //

    @Getter
    private G game;

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.game = gameClass.newInstance();

            this.game.setupRegistries(this.game.getRegistryManager());

            SetupPipeline setupPipeline = new SetupPipeline();
            setupPipeline(setupPipeline);
            setupPipeline.runAll();

            this.game.setupEngine(this.game.getEngine());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setupPipeline(SetupPipeline pipeline) {
    }
}
