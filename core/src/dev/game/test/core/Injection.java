package dev.game.test.core;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.injection.FieldResolver;
import com.badlogic.gdx.utils.Array;

public class Injection {

    private static final Array<Object> singletons = new Array<>();

    //

    public static void registerSingleton(Object object) {
        singletons.add(object);
    }

    //

    public static WorldConfiguration injectSingletons(WorldConfiguration builder) {
        for(Object singleton : singletons) {
            builder = builder.register(singleton);
        }

        return builder;
    }

}
