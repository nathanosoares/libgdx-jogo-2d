package dev.game.test.client;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.game.test.api.client.IClientManager;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientManager implements IClientManager {

    private Map<String, IWorld> worlds = Maps.newHashMap();
    private Map<UUID, IEntity> entities = Maps.newHashMap();

    @Getter
    private final ClientGame game;

    private final Engine engine;

    @Getter
    @Setter
    private IPlayer player;

    @Setter
    @Getter
    private IWorld currentWorld;

    public ClientManager(ClientGame game, Engine engine) {
        this.game = game;
        this.engine = engine;
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

    @Override
    public IEntity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    @Override
    public void addEntity(IEntity entity) {
        entities.put(entity.getId(), entity);
        engine.addEntity((Entity) entity);
    }

    @Override
    public void removeEntity(IEntity entity) {
        entities.remove(entity.getId());
        engine.removeEntity((Entity) entity);
    }
}
