package dev.game.test.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import dev.game.test.core.event.EventManager;
import dev.game.test.core.keybind.Keybind;
import dev.game.test.core.registry.RegistryManager;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;

@Getter
public abstract class GameApplication<T extends GameApplication<T>> extends ApplicationAdapter {

    protected final boolean clientSide;

    private SetupPipeline<T> setupPipeline;

    private RegistryManager<T> registryManager;

    private EventManager eventManager;

    public GameApplication(boolean clientSide) {
        this.clientSide = clientSide;
    }

    @Override
    public void create() {
        try {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);

            this.setupPipeline = new SetupPipeline<>((T) this);

            this.registryManager = new RegistryManager<>((T) this);

            setupRegistries(this.registryManager);

            this.eventManager = new EventManager();

            this.setupManagers();

            setupPipeline(this.setupPipeline);

            this.setupPipeline.runAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setupRegistries(RegistryManager<T> registryManager) {
        this.registryManager.addRegistry(Keybind.class, new RegistryKeybinds());
    }

    protected void setupManagers() {
    }


    protected void setupPipeline(SetupPipeline<T> pipeline) {
    }
}
