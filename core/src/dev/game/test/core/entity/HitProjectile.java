package dev.game.test.core.entity;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.EnumEntityType;
import dev.game.test.api.entity.IHitProjectile;

import java.util.UUID;

public class HitProjectile extends Projectile implements IHitProjectile {

    public HitProjectile(UUID id) {
        super(id);
    }

    @Override
    public EnumEntityType getType() {
        return EnumEntityType.HIT_PROJECTILE;
    }
}
