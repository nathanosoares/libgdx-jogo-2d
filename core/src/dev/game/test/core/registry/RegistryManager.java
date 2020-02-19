package dev.game.test.core.registry;

import com.badlogic.gdx.ApplicationAdapter;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class RegistryManager {

    private final ApplicationAdapter application;

    private final Map<Class<?>, Registry<?>> registries = Maps.newHashMap();

    public <T> RegistryManager addRegistry(Class<T> objectClass, Registry<T> registry) {
        this.registries.put(objectClass, registry);

        return this;
    }

    public <T, V extends Registry<T>> V getRegistry(Class<T> objectClass) {
        return (V) registries.get(objectClass);
    }
}
