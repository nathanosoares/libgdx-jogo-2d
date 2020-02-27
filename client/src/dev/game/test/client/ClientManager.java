package dev.game.test.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.game.test.api.client.IClientManager;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.world.World;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class ClientManager implements IClientManager {

    private Map<String, IWorld> worlds = Maps.newHashMap();

    @Getter
    private final IPlayer player;

    @Setter
    @Getter
    private IWorld currentWorld;

    public ClientManager(IPlayer player) {
        this.player = player;

    }

    @Override
    public List<IWorld> getWorlds() {
        return ImmutableList.copyOf(worlds.values());
    }

    @Override
    public void addWorld(IWorld world) {
        worlds.put(world.getName(), world);
    }

    @Override
    public IWorld getWorld(String worldName) {
        return worlds.get(worldName);
    }
}
