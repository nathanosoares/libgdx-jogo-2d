package dev.game.test.core.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.common.collect.Maps;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.block.IPhysicBlockState;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.ILivingEntity;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.entity.Hit;
import lombok.Getter;

import java.lang.annotation.Target;
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
                Object target = null;

                if (fixtureA.getUserData() instanceof Hit) {

                    hitFixture = fixtureA;
                    hit = (Hit) fixtureA.getUserData();
                    target = fixtureB.getUserData();

                } else if (fixtureB.getUserData() instanceof Hit) {

                    hitFixture = fixtureB;
                    hit = (Hit) fixtureB.getUserData();
                    target = fixtureA.getUserData();

                }

                if (hitFixture != null && !hit.getDamaged().contains(target)) {

                    if (target instanceof ILivingEntity) {
                        ILivingEntity livingEntity = (ILivingEntity) target;
                        if (hit.getSource() != target) {

                            // TODO event

                            livingEntity.damage(hit.getSource(), hit.getDamage());
                        }

                    } else if (target instanceof IPhysicBlockState) {
                        IPhysicBlockState physicBlockState = (IPhysicBlockState) target;

                        physicBlockState.onHit(hit.getSource());
                    }

                    hit.getDamaged().add(target);
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
                ground.setBlockState(primary.createState(this, ground, x, y));
            }
        }

        for (int x = 4; x < 9; x++) {
            ground.setBlockState(secondary.createState(this, ground, x, 5));
            ground.setBlockState(secondary.createState(this, ground, x, 6));

            if (x == 4) {
                continue;
            }

            ground.setBlockState(secondary.createState(this, ground, x, 4));
            ground.setBlockState(secondary.createState(this, ground, x, 7));
        }

        for (int x = 9; x < 12; x++) {
            ground.setBlockState(Blocks.WATER.createState(this, ground, x, 11));
            ground.setBlockState(Blocks.WATER.createState(this, ground, x, 12));

            if (x == 9) {
                continue;
            }

            ground.setBlockState(Blocks.WATER.createState(this, ground, x, 9));
            ground.setBlockState(Blocks.WATER.createState(this, ground, x, 10));
        }

        for (int x = 0; x < this.getBounds().getWidth(); x++) {
            for (int y = 0; y < this.getBounds().getHeight(); y++) {
                IBlockState blockState = ground.getBlockState(x, y);
//                blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
            }
        }

        this.layers[0] = ground;

        WorldLayer decorationLayer = new WorldLayer(this);

        decorationLayer.setBlockState(Blocks.GRASS_PLANT.createState(this, decorationLayer, 8, 8));
        decorationLayer.setBlockState(Blocks.STONE.createState(this, decorationLayer, 4, 4));

        this.layers[1] = decorationLayer;
    }

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
