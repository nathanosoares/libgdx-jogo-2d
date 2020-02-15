package dev.game.test.world;

import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.block.BlockDirt;
import dev.game.test.world.block.BlockGrass;
import dev.game.test.world.block.BlockWater;
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

    public static final BlockDirt DIRT = new BlockDirt();

    static {
        DIRT.loadTextures();
    }

    public World(String name, int width, int height) {
        this.name = name;

        this.width = width;
        this.height = height;

        this.layers = new WorldLayer[1];

        WorldLayer ground = new WorldLayer(this);


        BlockWater water = new BlockWater();
        water.loadTextures();

        BlockGrass grass = new BlockGrass();
        grass.loadTextures();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                ground.setBlock(x, y, new BlockData(
                        grass, 1, 1, this, ground, new Vector2(x, y)
                ));

            }
        }

        for (int x = 4; x < 9; x++) {
            ground.getBlock(x, 5).setBlock(DIRT);
            ground.getBlock(x, 6).setBlock(DIRT);

            if (x == 4 || x == 9) {
                continue;
            }

            ground.getBlock(x, 4).setBlock(DIRT);
            ground.getBlock(x, 7).setBlock(DIRT);
        }

        for (int x = 9; x < 12; x++) {
            ground.getBlock(x, 11).setBlock(water);
            ground.getBlock(x, 12).setBlock(water);

            if (x == 9 || x == 12) {
                continue;
            }

            ground.getBlock(x, 9).setBlock(water);
            ground.getBlock(x, 10).setBlock(water);
        }

        ground.getBlock(4, 10).setBlock(water);

        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                BlockData blockData = ground.getBlock(x, y);
                blockData.getBlock().onBlockNeighbourUpdate(blockData, null);
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
