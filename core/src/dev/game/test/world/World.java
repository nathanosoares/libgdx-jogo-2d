package dev.game.test.world;

import com.google.common.collect.Lists;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.entity.Entity;
import lombok.Getter;

import java.util.List;

@Getter
public class World {

    private int width;

    private int height;

    private WorldLayer[] layers;

    private final List<Entity> entities = Lists.newArrayList();

    private World(String world, int width, int height) {
        this.layers = new WorldLayer[1];

        WorldLayer ground = new WorldLayer();

        ground.setBlock(0, 0, new BlockData());

        this.layers[0] = ground;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.add(entity);
    }
}
