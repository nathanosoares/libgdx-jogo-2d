package dev.game.test.setups;

import com.badlogic.gdx.Gdx;
import com.google.common.collect.Maps;
import dev.game.test.GameApplication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public class RegistrySetups {

    private final Map<Class<? extends Setup>, Setup> REGISTRY = Collections.synchronizedMap(Maps.newLinkedHashMap());

    public void registerSetup(Setup setup) {
        if (REGISTRY.containsKey(setup.getClass())) {
            Gdx.app.error("Setup", String.format("Duplicated setup: %s", setup.getClass().getName()));
            return;
        }

        REGISTRY.put(setup.getClass(), setup);
    }

    public void runAll(GameApplication application) {
        Duration totalDuration = Duration.ZERO;

        for (Map.Entry<Class<? extends Setup>, Setup> entry : REGISTRY.entrySet()) {
            LocalDateTime start = LocalDateTime.now();
            String className = entry.getKey().getSimpleName();

            Gdx.app.debug("Setup", String.format("[%s] Running...", className));

            try {
                entry.getValue().setup(application);
            } catch (Exception | StackOverflowError e) {
                Gdx.app.error("Setup", String.format("[%s] Error while running. Shutting down...", className));
                e.printStackTrace();
                break;
            }

            Duration duration = Duration.between(start, LocalDateTime.now());
            totalDuration = totalDuration.plus(duration);

            Gdx.app.debug("Setup", String.format("[%s] Took %sms.", className, duration.toMillis()));
        }

        Gdx.app.debug("Setup", String.format("All setups took %sms.", totalDuration.toMillis()));
        REGISTRY.clear();
    }
}
