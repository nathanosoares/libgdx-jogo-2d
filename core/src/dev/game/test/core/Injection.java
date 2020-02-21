package dev.game.test.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Injection {

    public static Injector injector;

    protected static void setup(GameApplication application) {
        injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(GameApplication.class).toInstance(application);
            }
        });
    }
}
