package dev.game.test.core.registry.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.google.common.collect.Maps;
import dev.game.test.api.registry.IRegistry;
import lombok.Getter;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

public class PacketPayloadSerializerRegistry implements IRegistry<Serializer> {

    @Getter
    private final Map<Integer, Map.Entry<Class<?>, Function<Kryo, Serializer<?>>>> serializers = Maps.newHashMap();

    public <T> void registerSerializer(int id, Class<T> clazz, Function<Kryo, Serializer<?>> serializer) {
        serializers.put(id, new AbstractMap.SimpleEntry<>(clazz, serializer));
    }

    public <T> void registerSerializer(int id, Class<T> clazz) {
        serializers.put(id, new AbstractMap.SimpleEntry<>(clazz, kryo -> kryo.getDefaultSerializer(clazz)));
    }

    public void apply(Kryo kryo) {
        getSerializers().forEach((id, entry) -> kryo.register(entry.getKey(), entry.getValue().apply(kryo), id));
    }
}
