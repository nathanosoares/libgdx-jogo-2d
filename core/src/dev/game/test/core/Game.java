package dev.game.test.core;

import com.badlogic.ashley.core.Engine;
import dev.game.test.core.event.EventManager;
import dev.game.test.core.registry.RegistryManager;
import lombok.Getter;

public abstract class Game {

    @Getter
    private final RegistryManager registryManager;

    @Getter
    private final EventManager eventManager;

    @Getter
    private final Engine engine;

    //

    protected Game() {
        this.registryManager = new RegistryManager();
        this.eventManager = new EventManager();
        this.engine = new Engine();
    }

}
