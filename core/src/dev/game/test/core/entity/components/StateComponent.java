package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import dev.game.test.core.entity.Entity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StateComponent<T extends State<Entity>> implements Component {

    public final StateMachine<Entity, T> state;
    public float time = 0.0f;
    public boolean isLooping = false;
}
