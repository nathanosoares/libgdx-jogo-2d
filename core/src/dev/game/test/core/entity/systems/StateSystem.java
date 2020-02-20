package dev.game.test.core.entity.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import dev.game.test.core.entity.components.StateComponent;

@All(StateComponent.class)
public class StateSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> stateMapper;

    @Override
    protected void process(int entityId) {
        stateMapper.get(entityId).state.update();
    }
}
