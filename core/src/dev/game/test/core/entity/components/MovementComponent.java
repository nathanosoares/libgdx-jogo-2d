package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MovementComponent implements Component {
    public float velocityX;
    public float velocityY;
}
