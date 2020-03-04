package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.State;
import dev.game.test.core.entity.components.StateComponent;

public class PlayerStateSystem extends IteratingSystem {


    public PlayerStateSystem() {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StateComponent stateComponent = StateComponent.MAPPER.get(entity);

        State old = stateComponent.machine.getCurrentState();

        stateComponent.machine.update();


        if (old != stateComponent.machine.getCurrentState()) {
            stateComponent.changedAt = System.currentTimeMillis();
        }
    }
}
