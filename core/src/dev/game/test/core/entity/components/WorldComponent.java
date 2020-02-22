package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.api.world.IWorld;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WorldComponent implements Component {

    public static final ComponentMapper<WorldComponent> MAPPER = ComponentMapper.getFor(WorldComponent.class);

    //

    public IWorld world;

}
