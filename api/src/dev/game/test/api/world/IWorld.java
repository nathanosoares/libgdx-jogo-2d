package dev.game.test.api.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;

import java.util.Collection;
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

    IEntity createEntity(EnumEntityType type);

    IEntity createEntity(UUID uuid, EnumEntityType type);

    World getBox2dWorld();
}
