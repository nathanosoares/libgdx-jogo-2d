package dev.game.test.core.entity;

import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;

import java.util.UUID;

public class Entity extends com.badlogic.ashley.core.Entity implements IEntity {

    public Entity() {

        this.setupDefaultComponents();
    }


    protected void setupDefaultComponents() {

    }

    @Override
    public IWorld getWorld() {
        return null;
    }

    @Override
    public void setWorld(IWorld world) {

    }

    @Override
    public UUID getId() {
        return null;
    }
}
