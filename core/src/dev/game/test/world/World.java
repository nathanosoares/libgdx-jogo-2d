package dev.game.test.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.game.test.world.block.Block;
import dev.game.test.world.block.BlockState;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.entity.Entity;
import dev.game.test.world.entity.Player;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class World {

    public static Block CLIPBOARD;

    private final String name;

    private final Rectangle bounds;

    private WorldLayer[] layers;

    //

    private final Map<UUID, Player> players = Maps.newHashMap();

    private final List<Entity> entities = Lists.newArrayList();

    private final com.badlogic.gdx.physics.box2d.World box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);

    public World(String name, int width, int height) {
        CLIPBOARD = Blocks.DIRT;


        this.name = name;

        this.bounds = new Rectangle(0, 0, width, height);

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


        for (int x = 0; x < this.getBounds().getWidth(); x++) {
            for (int y = 0; y < this.getBounds().getHeight(); y++) {
                BlockState blockState = ground.getBlockState(x, y);
                blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
            }
        }

        this.layers[0] = ground;

        WorldLayer decoration = new WorldLayer(this);

        decoration.setBlockState(new BlockState(Blocks.STONE, this, decoration, new Vector2(2, 4)));

        this.layers[1] = decoration;
    }

    public Player getPlayer(UUID uid) {
        return this.players.get(uid);
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);

        if(entity instanceof Player) {
            this.players.put(entity.getId(), (Player) entity);
        }
    }

    public void removeEntity(Entity entity) {
        this.entities.add(entity);

        if(entity instanceof Player) {
            this.players.remove(entity.getId());
        }
    }
}
