package dev.game.test.api.event.entity;

import dev.game.test.api.entity.IEntity;

public class EntitySpawnEvent extends EntityEvent {

    public EntitySpawnEvent(IEntity entity) {
        super(entity);
    }
}
