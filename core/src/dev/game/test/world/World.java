package dev.game.test.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import dev.game.test.world.block.*;
import dev.game.test.world.entity.Entity;
import lombok.Getter;

import javax.swing.*;
import java.util.List;

@Getter
public class World {

    private final String name;

    private final Rectangle bounds;

    private WorldLayer[] layers;

    private final List<Entity> entities = Lists.newArrayList();

    public static final BlockDirt DIRT = new BlockDirt();
    public static final BlockGrass GRASS = new BlockGrass();
    public static final BlockWater WATER = new BlockWater();

    public static Block CLIPBOARD = DIRT;

    static {
        DIRT.loadTextures();
        GRASS.loadTextures();
        WATER.loadTextures();
    }

    public World(String name, int width, int height) {
        this.name = name;

        this.bounds = new Rectangle(0, 0, width, height);

        this.layers = new WorldLayer[1];

        WorldLayer ground = new WorldLayer(this);


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                ground.setBlock(x, y, new BlockData(
                        GRASS, 1, 1, this, ground, new Vector2(x, y)
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
            ground.getBlock(x, 11).setBlock(WATER);
            ground.getBlock(x, 12).setBlock(WATER);

            if (x == 9 || x == 12) {
                continue;
            }

            ground.getBlock(x, 9).setBlock(WATER);
            ground.getBlock(x, 10).setBlock(WATER);
        }

        ground.getBlock(4, 10).setBlock(WATER);


        for (int x = 0; x < this.getBounds().getWidth(); x++) {
            for (int y = 0; y < this.getBounds().getHeight(); y++) {
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
