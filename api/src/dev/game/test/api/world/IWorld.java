package dev.game.test.api.world;

import com.badlogic.gdx.math.Rectangle;
import dev.game.test.api.entity.IEntity;

public interface IWorld {

    IWorldLayer[] getLayers();

    Rectangle getBounds();

    void spawnEntity(IEntity entity, float x, float y);

    void destroyEntity(IEntity entity);
}
