package dev.game.test.world;

import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.block.BlockDirt;
import dev.game.test.world.entity.Entity;
import lombok.Getter;

import java.util.List;

@Getter
public class World {

    private final String name;

    private final int width;
    private final int height;

    private WorldLayer[] layers;

    private final List<Entity> entities = Lists.newArrayList();

    public World(String name, int width, int height) {
        this.name = name;

        this.width = width;
        this.height = height;

        this.layers = new WorldLayer[1];

        WorldLayer ground = new WorldLayer(this);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                ground.setBlock(x, y, new BlockData(
                        new BlockDirt(), this, new Vector2(x, y), 1, 1
                ));

            }
        }

        this.layers[0] = ground;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.add(entity);
    }
}
