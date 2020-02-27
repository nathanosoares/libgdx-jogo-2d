package dev.game.test.api;

import com.badlogic.ashley.core.Engine;
import dev.game.test.api.event.IEventManager;
import dev.game.test.api.registry.IRegistryManager;

public interface IGame {

    IRegistryManager getRegistryManager();

    IEventManager getEventManager();

    Engine getEngine();

    /*

     */


    default void setupRegistries(IRegistryManager registryManager) {}

    default void setupEngine(Engine engine){}

}
