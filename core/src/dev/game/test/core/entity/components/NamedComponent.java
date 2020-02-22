package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NamedComponent implements Component {

    public static final ComponentMapper<NamedComponent> MAPPER = ComponentMapper.getFor(NamedComponent.class);

    public String name;
}
