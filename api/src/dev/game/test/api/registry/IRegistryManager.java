package dev.game.test.api.registry;

public interface IRegistryManager {

    <T> IRegistryManager addRegistry(Class<T> objectClass, IRegistry<T> registry);

    <T, V extends IRegistry<T>> V getRegistry(Class<T> objectClass);

}
