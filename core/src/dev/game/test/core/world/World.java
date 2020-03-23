package dev.game.test.core.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.common.collect.Maps;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.ILivingEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockSolidState;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.entity.Hit;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class World implements IWorld {

    private final String name;

    protected final Rectangle bounds;

    protected WorldLayer[] layers;

    private final com.badlogic.gdx.physics.box2d.World box2dWorld;

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

        this.box2dWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);

        this.box2dWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                Hit hit = null;
                Fixture hitFixture = null;
                ILivingEntity livingEntity = null;

                if (fixtureA.getUserData() instanceof Hit && fixtureB.getUserData() instanceof ILivingEntity) {

                    hitFixture = fixtureA;
                    hit = (Hit) fixtureA.getUserData();
                    livingEntity = (ILivingEntity) fixtureB.getUserData();

                } else if (fixtureB.getUserData() instanceof Hit && fixtureA.getUserData() instanceof ILivingEntity) {

                    hitFixture = fixtureB;
                    hit = (Hit) fixtureB.getUserData();
                    livingEntity = (ILivingEntity) fixtureA.getUserData();

                }

                if (hitFixture != null) {
                    hit.getDamaged().add(livingEntity.getId());

                    if (!hit.getDamaged().contains(livingEntity.getId()) && hit.getSource() != livingEntity) {

                        // TODO event

                        livingEntity.damage(hit.getSource(), hit.getDamage());
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
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

            if (x == 4) {
                continue;
            }

            ground.getBlockState(x, 4).setBlock(secondary);
            ground.getBlockState(x, 7).setBlock(secondary);
        }

        for (int x = 9; x < 12; x++) {
            ground.getBlockState(x, 11).setBlock(Blocks.WATER);
            ground.getBlockState(x, 12).setBlock(Blocks.WATER);

            if (x == 9) {
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

        decoration.setBlockState(new BlockSolidState(Blocks.STONE, this, decoration, new Vector2(2, 4)));

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
        if (entity != null) {
            type.onPostCreate(entity);
        }

        return entity;
    }
}
