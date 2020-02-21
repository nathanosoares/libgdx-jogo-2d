package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.FacingComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.PositionComponent;

public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent position = PositionComponent.MAPPER.get(entity);
        MovementComponent movement = MovementComponent.MAPPER.get(entity);

        position.x += movement.velocityX * deltaTime;
        position.y += movement.velocityY * deltaTime;

        if (CollisiveComponent.MAPPER.has(entity)) {
            Rectangle box = CollisiveComponent.MAPPER.get(entity).box;
            box.setPosition(position.x, position.y);
        }

        if (FacingComponent.MAPPER.has(entity)) {
            FacingComponent facing = FacingComponent.MAPPER.get(entity);

            if (movement.velocityX > 0) {
                facing.facing = EnumFacing.EAST;
            } else if (movement.velocityX < 0) {
                facing.facing = EnumFacing.WEST;
            }
        }
    }
}
