package dev.game.test.api.world;

import com.badlogic.gdx.math.Rectangle;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;

import java.util.List;

public interface IWorld {

    String getName();

    IWorldLayer[] getLayers();

    Rectangle getBounds();

    //

    List<IEntity> getEntities();

    List<IPlayer> getPlayers();

    void spawnEntity(IEntity entity, float x, float y);

    void destroyEntity(IEntity entity);
}
