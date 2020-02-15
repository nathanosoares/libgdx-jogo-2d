package dev.game.test.world;

import com.google.common.collect.Lists;
import dev.game.test.world.entities.Entity;

import java.util.List;

public class World {

    private WorldLayer[] layers;

    private final List<Entity> entities = Lists.newArrayList();

    private World() {
        this.layers = new WorldLayer[1];


    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.add(entity);
    }
}
