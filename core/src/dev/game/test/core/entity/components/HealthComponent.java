package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class HealthComponent implements Component {

    public static final ComponentMapper<HealthComponent> MAPPER = ComponentMapper.getFor(HealthComponent.class);

    public float health = 20;
    public float maxHealth = 20;
}
