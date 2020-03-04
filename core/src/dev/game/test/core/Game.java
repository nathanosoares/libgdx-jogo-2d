package dev.game.test.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Serializer;
import dev.game.test.api.IGame;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.core.entity.player.systems.MovementSystem;
import dev.game.test.core.entity.player.systems.PlayerStateSystem;
import dev.game.test.core.event.EventManager;
import dev.game.test.core.registry.RegistryManager;
import dev.game.test.core.registry.impl.KeybindsRegistry;
import dev.game.test.core.registry.impl.PacketPayloadSerializerRegistry;
import lombok.Getter;

public abstract class Game implements IGame {

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

    @Override
    public void setupRegistries(IRegistryManager registryManager) {
        Gdx.app.debug(this.getClass().getSimpleName(), "Setup Registries");

        registryManager.addRegistry(Keybind.class, new KeybindsRegistry());
        registryManager.addRegistry(Serializer.class, new PacketPayloadSerializerRegistry());
    }


    @Override
    public void setupEngine(Engine engine) {
        Gdx.app.debug(this.getClass().getSimpleName(), "Setup Engine");

        engine.addSystem(new PlayerStateSystem(this));
        engine.addSystem(new MovementSystem(this));
    }
}
