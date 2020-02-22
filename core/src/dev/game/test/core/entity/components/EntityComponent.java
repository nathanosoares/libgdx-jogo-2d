package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.api.util.EnumFacing;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityComponent implements Component {

    public static final ComponentMapper<EntityComponent> MAPPER = ComponentMapper.getFor(EntityComponent.class);

    public boolean spawned;
}
