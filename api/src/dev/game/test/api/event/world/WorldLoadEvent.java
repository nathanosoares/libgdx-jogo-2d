package dev.game.test.api.event.world;

import dev.game.test.api.world.IWorld;

public class WorldLoadEvent extends WorldEvent {

    public WorldLoadEvent(IWorld world) {
        super(world);
    }
}
