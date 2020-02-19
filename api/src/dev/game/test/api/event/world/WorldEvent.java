package dev.game.test.api.event.world;

import dev.game.test.api.event.Event;
import dev.game.test.api.world.IWorld;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class WorldEvent implements Event {

    @Getter
    private final IWorld world;
}
