package dev.game.test.core.registry;

import com.google.common.collect.Maps;
import dev.game.test.api.registry.IRegistry;
import dev.game.test.api.registry.IRegistryManager;
import dev.game.test.core.GameApplication;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class RegistryManager implements IRegistryManager {

    @Inject
    private GameApplication application;

    private final Map<Class<?>, IRegistry<?>> registries = Maps.newHashMap();

    public <T> RegistryManager addRegistry(Class<T> objectClass, Class<IRegistry<T>> registryClass) {
        IRegistry<T> registry = Inje

        this.registries.put(objectClass, registry);
        return this;
    }

    public <T, V extends IRegistry<T>> V getRegistry(Class<T> objectClass) {
        return (V) registries.get(objectClass);
    }
}
