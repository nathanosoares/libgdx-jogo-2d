package dev.game.test.core.entity;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.components.EntityComponent;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.PositionComponent;

import java.util.UUID;

public abstract class Entity extends com.badlogic.ashley.core.Entity implements IEntity {

    public Entity(UUID id) {
        this.add(new IdentifiableComponent(id));
        this.add(new PositionComponent(0, 0, null));
        this.add(new EntityComponent(false));

        this.setupDefaultComponents();
    }

    protected void setupDefaultComponents() {

    }

    @Override
    public com.badlogic.ashley.core.Entity getHandle() {
        return this;
    }

    @Override
    public Vector2 getPosition() {
        PositionComponent position = PositionComponent.MAPPER.get(this);
        return new Vector2(position.x, position.y);
    }

    @Override
    public Vector2 setPosition(Vector2 vec) {
        PositionComponent position = PositionComponent.MAPPER.get(this);

        position.x = vec.x;
        position.y = vec.y;

        return new Vector2(position.x, position.y);
    }

    @Override
    public void setVelocity(Vector2 vec) {
        MovementComponent movementComponent = MovementComponent.MAPPER.get(this);

        movementComponent.velocityX = vec.x;
        movementComponent.velocityY = vec.y;
    }

    @Override
    public void setWorld(IWorld world) {
        PositionComponent.MAPPER.get(this).world = world;
    }

    @Override
    public boolean isSpawned() {
        return EntityComponent.MAPPER.get(this).spawned;
    }

    @Override
    public void onSpawn(IWorld world) {
        EntityComponent.MAPPER.get(this).spawned = true;
        PositionComponent.MAPPER.get(this).world = world;
    }

    @Override
    public void onDestroy(IWorld world) {
        EntityComponent.MAPPER.get(this).spawned = false;
        PositionComponent.MAPPER.get(this).world = null;
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
