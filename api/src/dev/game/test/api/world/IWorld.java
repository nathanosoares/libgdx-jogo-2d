package dev.game.test.api.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IWorld {

    String getName();

    IWorldLayer[] getLayers();

    Rectangle getBounds();

    Collection<IEntity> getEntities();

    Collection<IPlayer> getPlayers();

    IEntity getEntity(UUID id);

    IPlayer getPlayer(UUID id);

    void spawnEntity(IEntity entity, float x, float y);

    default void spawnEntity(IEntity entity, Vector2 position) {
        spawnEntity(entity, position.x, position.y);
    }

    void destroyEntity(IEntity entity);
}
