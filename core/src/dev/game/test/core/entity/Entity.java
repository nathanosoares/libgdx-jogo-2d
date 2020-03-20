package dev.game.test.core.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.google.common.base.Throwables;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.EntityDestroyServerPacket;
import dev.game.test.api.net.packet.server.EntitySpawnServerPacket;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.*;

import java.util.UUID;

public abstract class Entity extends com.badlogic.ashley.core.Entity implements IEntity {

    public Entity(UUID id) {
        this.add(new IdentifiableComponent(id));
        this.add(new PositionComponent(0, 0, null));
        this.add(new EntityComponent(false));
        this.add(new VelocityComponent());
        this.add(new DirectionComponent());

        this.setupDefaultComponents();
    }

    protected void setupDefaultComponents() {

    }

    @Override
    public double getDirection() {
        return DirectionComponent.MAPPER.get(this).degrees;
    }

    @Override
    public void setDirection(double degrees) {
        DirectionComponent.MAPPER.get(this).degrees = degrees;
    }

    @Override
    public Vector2 getPosition() {
        PositionComponent position = PositionComponent.MAPPER.get(this);

        return new Vector2(position.x, position.y);
    }

    @Override
    public void setPosition(Vector2 vec) {
        PositionComponent position = PositionComponent.MAPPER.get(this);

        position.x = vec.x;
        position.y = vec.y;
    }

    @Override
    public void setPosition(IWorld world, Vector2 vec) {
        if (!world.equals(this.getWorld())) {
            world.spawnEntity(this, vec);
            PositionComponent.MAPPER.get(this).world = world;
        } else {
            this.setPosition(vec);
        }
    }

    @Override
    public void setVelocity(Vector2 vec) {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(this);

        velocityComponent.x = vec.x;
        velocityComponent.y = vec.y;
    }

    @Override
    public Vector2 getVelocity() {
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(this);

        return new Vector2(velocityComponent.x, velocityComponent.y);
    }

    @Override
    public boolean isSpawned() {
        return EntityComponent.MAPPER.get(this).spawned;
    }

    @Override
    public void onSpawn(IWorld world) {
        System.out.println(Throwables.getStackTraceAsString(new Throwable()));

        EntityComponent.MAPPER.get(this).spawned = true;
        PositionComponent.MAPPER.get(this).world = world;

        BodyComponent bodyComponent = BodyComponent.MAPPER.get(this);
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep = false;

        // Set our body's starting position in the world
        Vector2 position = getPosition();
        bodyDef.position.set(position.x + this.getWidth() / 2, position.y + this.getHeight() / 2);

        // Create our body in the world using our body definition
        bodyComponent.body = world.getBox2dWorld().createBody(bodyDef);

        // Create Shape
        Shape shape = this.createShape();

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = -1;

        // Create our fixture and attach it to the body
        bodyComponent.body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        shape.dispose();

        if (Game.getInstance() instanceof IServerGame) {
            ((IServerGame) Game.getInstance()).getConnectionHandler().broadcastPacket(
                    new EntitySpawnServerPacket(this.getId(), this.getType(), getPosition(), getDirection()), world
            );
        }
    }

    @Override
    public void onDestroy(IWorld world) {
        BodyComponent bodyComponent = BodyComponent.MAPPER.get(this);
        world.getBox2dWorld().destroyBody(bodyComponent.body);

        EntityComponent.MAPPER.get(this).spawned = false;
        PositionComponent.MAPPER.get(this).world = null;
        bodyComponent.body = null;

        if (Game.getInstance() instanceof IServerGame) {
            ((IServerGame) Game.getInstance()).getConnectionHandler().broadcastPacket(
                    new EntityDestroyServerPacket(this.getId()), world
            );
        }
    }

    /*

     */

    @Override
    public IWorld getWorld() {
        return PositionComponent.MAPPER.get(this).world;
    }

    @Override
    public UUID getId() {
        return IdentifiableComponent.MAPPER.get(this).uuid;
    }
}
