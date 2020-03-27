package dev.game.test.server;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.server.IServerManager;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.world.World;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class ServerManager implements IServerManager {

    private Map<UUID, IEntity> entities = Maps.newHashMap();

    private final ServerGame game;
    private final Engine engine;

    private Map<String, IWorld> worlds = Maps.newLinkedHashMap();

    public void loadWorlds() {
        {
            World world = new World("world", ServerConstants.MAP_SIZE, ServerConstants.MAP_SIZE);

            world.fillLayers(Blocks.GRASS, Blocks.DIRT); // TODO temp

            addWorld(world);
        }

        {
            World world = new World("test", ServerConstants.MAP_SIZE, ServerConstants.MAP_SIZE);

            world.fillLayers(Blocks.DIRT, Blocks.GRASS); // TODO temp

            addWorld(world);
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
        world.setLoaded(true);
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
