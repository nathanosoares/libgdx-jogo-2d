package dev.game.test.core.entity;

import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.entity.components.IdentifiableComponent;
import dev.game.test.core.entity.components.WorldComponent;

import java.util.UUID;

public class Entity extends com.badlogic.ashley.core.Entity implements IEntity {

    public Entity(UUID id, IWorld world) {
        this.add(new IdentifiableComponent(id));
        this.add(new WorldComponent(world));

        this.setupDefaultComponents();
    }

    protected void setupDefaultComponents() {

    }

    @Override
    public com.badlogic.ashley.core.Entity getHandle() {
        return this;
    }

    /*

     */

    @Override
    public IWorld getWorld() {
        return WorldComponent.MAPPER.get(this).world;
    }

    @Override
    public void setWorld(IWorld world) {
        WorldComponent.MAPPER.get(this).world = world;
    }

    @Override
    public UUID getId() {
        return IdentifiableComponent.MAPPER.get(this).uuid;
    }
}
