package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import dev.game.test.core.entity.Entity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StateComponent<T extends State<Entity>> implements Component {

    public static final ComponentMapper<StateComponent> MAPPER = ComponentMapper.getFor(StateComponent.class);

    public final StateMachine<Entity, T> machine;
    public float time = 0.0f;
    public boolean isLooping = true;
}
