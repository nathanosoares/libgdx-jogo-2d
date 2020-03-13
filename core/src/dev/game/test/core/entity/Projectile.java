package dev.game.test.core.entity;

import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.IProjectile;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public abstract class Projectile extends Entity implements IProjectile {

    @Getter
    @Setter
    private IEntity source;

    public Projectile(UUID id) {
        super(id);
    }
}
