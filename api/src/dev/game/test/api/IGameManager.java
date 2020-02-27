package dev.game.test.api;

import dev.game.test.api.world.IWorld;

import java.util.List;

public interface IGameManager {

    List<IWorld> getWorlds();

    void addWorld(IWorld world);

    IWorld getWorld(String worldName);
}
