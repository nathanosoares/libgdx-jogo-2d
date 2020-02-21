package dev.game.test.core.registry;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.game.test.core.GameApplication;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Singleton
public class RegistryManager {

    @Inject
    private GameApplication application;

    private final Map<Class<?>, Registry<?>> registries = Maps.newHashMap();

    public <T> RegistryManager addRegistry(Class<T> objectClass, Registry<T> registry) {
        this.registries.put(objectClass, registry);

        this.application.getInjector().injectMembers(registry);

        return this;
    }

    public <T, V extends Registry<T>> V getRegistry(Class<T> objectClass) {
        return (V) registries.get(objectClass);
    }
}
