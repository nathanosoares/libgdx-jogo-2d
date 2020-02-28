package dev.game.test.server;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.net.packet.Packet;
import dev.game.test.api.server.IServerManager;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.components.NetworkComponent;
import dev.game.test.core.world.World;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        ImmutableArray<Entity> entities = this.game.getEngine().getEntitiesFor(Family.all(NetworkComponent.class).get());

        List<IPlayer> players = Lists.newArrayList(entities).stream()
                .map(entity -> (IPlayer) entity)
                .collect(Collectors.toList());

        for (IPlayer player : players) {
            player.sendPacket(packet);
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
