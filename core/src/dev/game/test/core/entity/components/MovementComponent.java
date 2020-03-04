package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MovementComponent implements Component {

    public static final ComponentMapper<MovementComponent> MAPPER = ComponentMapper.getFor(MovementComponent.class);

    public float deltaX;
    public float deltaY;
}
