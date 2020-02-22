package dev.game.test.core.registry;

import com.google.common.collect.Maps;
import dev.game.test.api.registry.IRegistry;
import dev.game.test.api.registry.IRegistryManager;

import java.util.Map;

public class RegistryManager implements IRegistryManager {

    private final Map<Class<?>, IRegistry<?>> registries = Maps.newHashMap();

    public <T> RegistryManager addRegistry(Class<T> objectClass, IRegistry<T> registry) {
        this.registries.put(objectClass, registry);
        return this;
    }

    public <T, V extends IRegistry<T>> V getRegistry(Class<T> objectClass) {
        return (V) registries.get(objectClass);
    }
}
