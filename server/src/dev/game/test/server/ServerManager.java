package dev.game.test.server;

import com.google.common.collect.Maps;
import dev.game.test.api.server.IServerManager;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.world.World;

import java.util.Map;

public class ServerManager implements IServerManager {

    private Map<String, IWorld> worlds = Maps.newHashMap();

    //

    public void loadWorlds() {
        World world = new World("world", ServerConstants.MAP_SIZE, ServerConstants.MAP_SIZE);
        registerWorld(world);

        world = new World("test", ServerConstants.MAP_SIZE, ServerConstants.MAP_SIZE);
        registerWorld(world);
    }

    private World registerWorld(World world) {
        if (this.worlds.containsKey(world.getName())) {
            throw new RuntimeException("Nome de mundo em uso");
        }

        worlds.put(world.getName(), world);
        return world;
    }

    @Override
    public IWorld getWorld(String name) {
        return worlds.get(name);
    }

    @Override
    public IWorld getDefaultWorld() {
        return getWorld("world");
    }
}
