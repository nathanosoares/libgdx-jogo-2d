package dev.game.test.core.setup;

import com.badlogic.gdx.Gdx;
import com.google.common.collect.Maps;
import dev.game.test.core.GameApplication;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
public class SetupPipeline {

    private final GameApplication application;

    private final Map<Class<? extends Setup>, Setup> registry = Maps.newLinkedHashMap();

    public SetupPipeline registerSetup(Setup setup) {
        if (registry.containsKey(setup.getClass())) {
            Gdx.app.error("Setup", String.format("Duplicated setup: %s", setup.getClass().getName()));
            return this;
        }

        registry.put(setup.getClass(), setup);

        return this;
    }

    public void runAll() {
        Duration totalDuration = Duration.ZERO;

        for (Map.Entry<Class<? extends Setup>, Setup> entry : registry.entrySet()) {
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
        registry.clear();
    }
}
