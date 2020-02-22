package dev.game.test.core.world;

import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.Maps;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class World implements IWorld {

    private final String name;

    protected final Rectangle bounds;

    protected WorldLayer[] layers;

    //

    private final Map<UUID, IEntity> entities = Maps.newHashMap();

    private final Map<UUID, IPlayer> players = Maps.newHashMap();

    public World(String name, int width, int height) {
        this.name = name;
        this.bounds = new Rectangle(0, 0, width, height);
    }

    //


    @Override
    public Collection<IPlayer> getPlayers() {
        return players.values();
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return players.get(id);
    }


    @Override
    public Collection<IEntity> getEntities() {
        return entities.values();
    }

    @Override
    public IEntity getEntity(UUID id) {
        return entities.get(id);
    }

    //

    @Override
    public void spawnEntity(IEntity entity, float x, float y) {
        entity.onSpawn(this);

        this.entities.put(entity.getId(), entity);

        if(entity instanceof IPlayer) {
            players.put(entity.getId(), (IPlayer) entity);
        }
    }

    @Override
    public void destroyEntity(IEntity entity) {
        entity.onDestroy(this);

        this.entities.remove(entity.getId());

        if(entity instanceof IPlayer) {
            players.remove((IPlayer) entity);
        }
    }
}
