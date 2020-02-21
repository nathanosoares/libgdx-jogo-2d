package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.PositionComponent;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent position = positionMapper.get(entity);
        MovementComponent movement = movementMapper.get(entity);

        position.x += movement.velocityX * deltaTime;
        position.y += movement.velocityY * deltaTime;
    }
}
