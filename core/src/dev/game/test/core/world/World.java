package dev.game.test.core.world;

import com.badlogic.gdx.math.Rectangle;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import lombok.Getter;

@Getter
public class World implements IWorld {


    private final String name;

    protected final Rectangle bounds;


    protected WorldLayer[] layers;

    public World(String name, int width, int height) {
        this.name = name;
        this.bounds = new Rectangle(0, 0, width, height);
    }

    @Override
    public void spawnEntity(IEntity entity, float x, float y) {

    }

    @Override
    public void destroyEntity(IEntity entity) {

    }
}
