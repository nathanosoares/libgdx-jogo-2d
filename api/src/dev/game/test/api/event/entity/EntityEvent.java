package dev.game.test.api.event.entity;

import dev.game.test.api.entity.IEntity;
import dev.game.test.api.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class EntityEvent implements Event {

    @Getter
    private final IEntity entity;
}
