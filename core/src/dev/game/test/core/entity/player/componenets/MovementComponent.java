package dev.game.test.core.entity.player.componenets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class MovementComponent implements Component {

    public static final ComponentMapper<MovementComponent> MAPPER = ComponentMapper.getFor(MovementComponent.class);

    public long sequenceId;
    public long updatedAt;
    public float speed = 4;
    public float deltaX;
    public float deltaY;
}
