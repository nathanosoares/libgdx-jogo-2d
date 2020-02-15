package dev.game.test.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import dev.game.test.world.block.BlockState;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.entity.Entity;
import lombok.Getter;

import java.util.List;

@Getter
public class World {

    private final String name;

    private final Rectangle bounds;

    private WorldLayer[] layers;

    private final List<Entity> entities = Lists.newArrayList();

    public World(String name, int width, int height) {
        this.name = name;

        this.bounds = new Rectangle(0, 0, width, height);

        this.layers = new WorldLayer[1];

        WorldLayer ground = new WorldLayer(this);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                ground.setBlock(x, y, new BlockState(
                        Blocks.DIRT,
                        1, 1, this, ground, new Vector2(x, y)
                ));

            }
        }

        for (int x = 4; x < 9; x++) {
            ground.getBlockState(x, 5).setBlock(Blocks.DIRT);
            ground.getBlockState(x, 6).setBlock(Blocks.DIRT);

            if (x == 4 || x == 9) {
                continue;
            }

            ground.getBlockState(x, 4).setBlock(Blocks.DIRT);
            ground.getBlockState(x, 7).setBlock(Blocks.DIRT);
        }

        for (int x = 9; x < 12; x++) {
            ground.getBlockState(x, 11).setBlock(Blocks.WATER);
            ground.getBlockState(x, 12).setBlock(Blocks.WATER);

            if (x == 9 || x == 12) {
                continue;
            }

            ground.getBlockState(x, 9).setBlock(Blocks.WATER);
            ground.getBlockState(x, 10).setBlock(Blocks.WATER);
        }

        ground.getBlockState(4, 10).setBlock(Blocks.WATER);

        ground.getBlockState(4, 14).setBlock(Blocks.STONE);

        for (int x = 0; x < this.getBounds().getWidth(); x++) {
            for (int y = 0; y < this.getBounds().getHeight(); y++) {
                BlockState blockState = ground.getBlockState(x, y);
                blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
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
