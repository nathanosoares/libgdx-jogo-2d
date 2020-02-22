package dev.game.test.core.world;

import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.Lists;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import lombok.Getter;

import java.util.List;

@Getter
public class World implements IWorld {

    private final String name;

    protected final Rectangle bounds;

    protected WorldLayer[] layers;

    //

    @Getter
    protected final List<IEntity> entities = Lists.newArrayList();

    @Getter
    private final List<IPlayer> players = Lists.newArrayList();

    public World(String name, int width, int height) {
        this.name = name;
        this.bounds = new Rectangle(0, 0, width, height);
    }

    //

    @Override
    public void spawnEntity(IEntity entity, float x, float y) {
        this.entities.add(entity);

        if(entity instanceof IPlayer) {
            players.add((IPlayer) entity);
        }
    }

    @Override
    public void destroyEntity(IEntity entity) {
        this.entities.remove(entity);

        if(entity instanceof IPlayer) {
            players.remove((IPlayer) entity);
        }
    }
}
