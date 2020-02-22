package dev.game.test.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.google.inject.Injector;
import dev.game.test.core.entity.systems.MovementSystem;
import dev.game.test.core.entity.systems.PlayerStateSystem;
import dev.game.test.core.event.EventManager;
import dev.game.test.core.keybind.Keybind;
import dev.game.test.core.registry.RegistryManager;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;

@Getter
public abstract class GameApplication extends ApplicationAdapter {

    protected final boolean clientSide;

    private final Engine engine = new Engine();

    public GameApplication(boolean clientSide) {
        this.clientSide = clientSide;

        Injection.setup(this);
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            RegistryManager registryManager = Injection.injector.getInstance(RegistryManager.class);

            Gdx.app.debug("registryManager ", String.valueOf(registryManager));
            setupRegistries(registryManager);

            EventManager eventManager = new EventManager();

            this.setupManagers();

            SetupPipeline setupPipeline = new SetupPipeline(this);

            setupPipeline(setupPipeline);

            setupPipeline.runAll();

            this.engine.addSystem(new MovementSystem());
            this.engine.addSystem(new PlayerStateSystem());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setupRegistries(RegistryManager registryManager) {
        registryManager.addRegistry(Keybind.class, new RegistryKeybinds());
    }

    protected void setupManagers() {
    }


    protected void setupPipeline(SetupPipeline pipeline) {
    }
}
