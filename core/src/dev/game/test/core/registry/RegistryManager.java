package dev.game.test.core.registry;

import com.google.common.collect.Maps;
import dev.game.test.core.GameApplication;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class RegistryManager<T extends GameApplication<T>> {

    private final T application;

    private final Map<Class<?>, Registry<?>> registries = Maps.newHashMap();

    public <R> RegistryManager<T> addRegistry(Class<R> objectClass, Registry<R> registry) {
        this.registries.put(objectClass, registry);

        return this;
    }

    public <T, V extends Registry<T>> V getRegistry(Class<T> objectClass) {
        return (V) registries.get(objectClass);
    }
}
