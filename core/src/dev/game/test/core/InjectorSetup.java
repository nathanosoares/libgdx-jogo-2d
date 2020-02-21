package dev.game.test.core;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorSetup {

    public static Injector setup(GameApplication application) {

        return Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                bind(GameApplication.class).toInstance(application);
            }
        });
    }
}
