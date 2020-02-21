package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import dev.game.test.core.entity.components.StateComponent;

public class PlayerStateSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);

    public PlayerStateSystem() {
        super(Family.all(
                StateComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        stateMapper.get(entity).state.update();

    }
}
