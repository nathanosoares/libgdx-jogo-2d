package dev.game.test.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.game.test.api.client.IClientManager;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class ClientManager implements IClientManager {

    private Map<String, IWorld> worlds = Maps.newHashMap();
    private Map<UUID, IEntity> entities = Maps.newHashMap();

    @Getter
    private final ClientGame game;

    @Getter
    @Setter
    private IPlayer player;

    @Setter
    @Getter
    private IWorld currentWorld;

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

    @Override
    public IEntity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    @Override
    public void addEntity(IEntity entity) {
        entities.put(entity.getId(), entity);
    }

    @Override
    public void removeEntity(IEntity entity) {
        entities.remove(entity.getId());
    }
}
