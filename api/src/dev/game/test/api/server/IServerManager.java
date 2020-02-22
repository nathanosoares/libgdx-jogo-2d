package dev.game.test.api.server;

import dev.game.test.api.world.IWorld;

public interface IServerManager {

    void loadWorlds();

    IWorld getWorld(String name);

    IWorld getDefaultWorld();

}
