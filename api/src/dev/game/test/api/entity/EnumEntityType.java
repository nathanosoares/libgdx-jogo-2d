package dev.game.test.api.entity;

import com.badlogic.ashley.core.Entity;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public enum EnumEntityType {

    PLAYER(IPlayer.class),
    HIT_PROJECTILE(IHitProjectile.class),
    ;

    private final Class<? extends IEntity> clazz;
    private final List<Consumer<IEntity>> createHandlers = Lists.newArrayList();

    public Class<? extends IEntity> getEntityClass() {
        return clazz;
    }

    public void addPostCreateListener(Consumer<IEntity> consumer) {
        createHandlers.add(consumer);
    }

    public void onPostCreate(IEntity entity) {
        createHandlers.forEach(c -> c.accept(entity));
    }
}
