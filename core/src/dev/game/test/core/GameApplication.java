package dev.game.test.core;

import dev.game.test.core.event.EventManager;
import dev.game.test.core.keybind.Keybind;
import dev.game.test.core.registry.RegistryManager;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import dev.game.test.core.setup.SetupPipeline;
import lombok.Getter;

@Getter
public abstract class GameApplication<T extends GameApplication<T>> {

    protected final boolean clientSide;

    private final SetupPipeline<T> setupPipeline;

    private final RegistryManager<T> registryManager;

    private final EventManager eventManager;

    public GameApplication(boolean clientSide) {
        this.clientSide = clientSide;

        this.setupPipeline = new SetupPipeline<>((T) this);

        this.registryManager = new RegistryManager<>((T) this);
        this.registryManager.addRegistry(Keybind.class, new RegistryKeybinds());

        this.eventManager = new EventManager();

    }

    protected void setupDefaultPipeline() {
    }
}
