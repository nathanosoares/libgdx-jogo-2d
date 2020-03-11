package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class DirectionComponent implements Component {

    public static final ComponentMapper<DirectionComponent> MAPPER = ComponentMapper.getFor(DirectionComponent.class);

    public double degrees;
}
