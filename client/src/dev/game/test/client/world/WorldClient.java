package dev.game.test.client.world;

import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import dev.game.test.client.block.Blocks;
import dev.game.test.client.entity.Entity;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.world.World;
import dev.game.test.core.world.WorldLayer;
import lombok.Getter;

import java.util.List;

@Getter
public class WorldClient extends World {

    public static Block CLIPBOARD;


    private final List<Entity> entities = Lists.newArrayList();

    private final com.badlogic.gdx.physics.box2d.World box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), false);

    public WorldClient(String name, int width, int height) {
        super(name, width, height);

        CLIPBOARD = Blocks.DIRT;

        this.layers = new WorldLayer[2];

        WorldLayer ground = new WorldLayer(this);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                ground.setBlockState(new BlockState(
                        Blocks.GRASS, this, ground, new Vector2(x, y)
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
        ground.getBlockState(4, 11).setBlock(Blocks.REINFORCED_DIRT);


        for (int x = 0; x < this.getBounds().getWidth(); x++) {
            for (int y = 0; y < this.getBounds().getHeight(); y++) {
                BlockState blockState = ground.getBlockState(x, y);
                blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
            }
        }

        this.layers[0] = ground;

        WorldLayer decoration = new WorldLayer(this);

        decoration.setBlockState(new BlockState(Blocks.STONE, this, decoration, new Vector2(4, 13)));

        this.layers[1] = decoration;
    }

    public void addEntity(Entity entity, float x, float y) {
        if (entity.getWorld() != null) {
            entity.getWorld().destroyEntity(entity);
        }

        entity.setWorld(this);

        this.entities.add(entity);


        entity.onWorldAdd(this, x, y);
    }
}
