package dev.game.test.core.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Maps;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.entity.HitProjectile;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class World implements IWorld {

    private final String name;

    protected final Rectangle bounds;

    protected WorldLayer[] layers;

    //

    private final Map<UUID, IEntity> entities = Maps.newHashMap();

    private final Map<UUID, IPlayer> players = Maps.newHashMap();

    public World(String name, int width, int height) {
        this(name, 0, width, height);
    }

    public World(String name, int layersSize, int width, int height) {
        this.name = name;
        this.bounds = new Rectangle(0, 0, width, height);
        this.layers = new WorldLayer[layersSize];

        for (int i = 0; i < layersSize; i++) {
            this.layers[i] = new WorldLayer(this);
        }
    }

    public void fillLayers(Block primary, Block secondary) {
        this.layers = new WorldLayer[2];

        WorldLayer ground = new WorldLayer(this);

        for (int x = 0; x < getBounds().getWidth(); x++) {
            for (int y = 0; y < getBounds().getHeight(); y++) {

                ground.setBlockState(new BlockState(
                        primary, this, ground, new Vector2(x, y)
                ));

            }
        }

        for (int x = 4; x < 9; x++) {
            ground.getBlockState(x, 5).setBlock(secondary);
            ground.getBlockState(x, 6).setBlock(secondary);

            if (x == 4 || x == 9) {
                continue;
            }

            ground.getBlockState(x, 4).setBlock(secondary);
            ground.getBlockState(x, 7).setBlock(secondary);
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
                IBlockState blockState = ground.getBlockState(x, y);
//                blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
            }
        }

        this.layers[0] = ground;

        WorldLayer decoration = new WorldLayer(this);

        decoration.setBlockState(new BlockState(Blocks.STONE, this, decoration, new Vector2(2, 4)));

        this.layers[1] = decoration;
    }

    //


    @Override
    public Collection<IPlayer> getPlayers() {
        return players.values();
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return players.get(id);
    }


    @Override
    public Collection<IEntity> getEntities() {
        return entities.values();
    }

    @Override
    public IEntity getEntity(UUID id) {
        return entities.get(id);
    }

    @Override
    public void spawnEntity(IEntity entity, float x, float y) {

        if (entity.getWorld() != null) {
            entity.getWorld().destroyEntity(entity);
        }

        entity.setPosition(new Vector2(x, y));
        entity.onSpawn(this);

        this.entities.put(entity.getId(), entity);

        if (entity instanceof IPlayer) {
            players.put(entity.getId(), (IPlayer) entity);
        }

        Game.getInstance().getGameManager().addEntity(entity);
    }

    @Override
    public void destroyEntity(IEntity entity) {
        entity.onDestroy(this);

        this.entities.remove(entity.getId());

        if (entity instanceof IPlayer) {
            players.remove(entity.getId());
        }

        Game.getInstance().getGameManager().removeEntity(entity);
    }

    @Override
    public IEntity createEntity(EnumEntityType type) {
        UUID uuid = UUID.randomUUID();

        return createEntity(uuid, type);
    }


    @Override
    public IEntity createEntity(UUID uuid, EnumEntityType type) {

        IEntity entity = null;
        if (type == EnumEntityType.HIT_PROJECTILE) {
            entity = new HitProjectile(uuid);
        }

        if (entity != null) {
            type.onPostCreate(entity);
        }

        return entity;
    }
}
