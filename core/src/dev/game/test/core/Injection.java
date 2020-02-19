package dev.game.test.core;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.injection.FieldResolver;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;

public class Injection {

    private static final Map<String, Object> singletons = Maps.newHashMap();

    //

    public static void registerSingleton(Object object) {
        singletons.put(object.getClass().getName(), object);
    }

    public static void registerSingleton(Object object, Class<?> singletonClass) {
        singletons.put(singletonClass.getName(), object);
    }

    //

    public static WorldConfiguration injectSingletons(WorldConfiguration builder) {
        for(Entry<String, Object> singleton : singletons.entrySet()) {
            builder = builder.register(singleton.getKey(), singleton.getValue());
        }

        return builder;
    }

}
