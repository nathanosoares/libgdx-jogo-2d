package dev.game.test.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.server.IServerManager;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.world.World;
import dev.game.test.server.handler.PlayerConnectionManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ServerManager implements IServerManager {

    private final ServerGame game;

    private Map<String, IWorld> worlds = Maps.newLinkedHashMap();

    public void loadWorlds() {
        {
            World world = new World("world", ServerConstants.MAP_SIZE, ServerConstants.MAP_SIZE);

            world.fillLayers(); // TODO temp

            addWorld(world);
        }

        {
            World world = new World("test", ServerConstants.MAP_SIZE, ServerConstants.MAP_SIZE);

            world.fillLayers(); // TODO temp

            addWorld(world);
        }
    }

    @Override
    public void broadcastPacket(Packet packet) {
        for (PlayerConnectionManager connectionManager : this.game.getConnectionHandler().getConnections().values()) {
            connectionManager.sendPacket(packet);
        }
    }

    @Override
    public List<IWorld> getWorlds() {
        return ImmutableList.copyOf(worlds.values());
    }

    @Override
    public void addWorld(IWorld world) {
        if (this.worlds.containsKey(world.getName())) {
            throw new RuntimeException("Nome de mundo em uso");
        }

        worlds.put(world.getName(), world);
    }

    @Override
    public IWorld getWorld(String worldName) {
        return worlds.get(worldName);
    }

}
