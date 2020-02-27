package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import dev.game.test.core.entity.components.StateComponent;

public class PlayerStateSystem extends IteratingSystem {


    public PlayerStateSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        StateComponent.MAPPER.get(entity).machine.update();

    }
}
