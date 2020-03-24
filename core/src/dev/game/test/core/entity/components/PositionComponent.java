package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import dev.game.test.api.world.IWorld;
import dev.game.test.core.world.World;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class PositionComponent implements Component {

    public static final ComponentMapper<PositionComponent> MAPPER = ComponentMapper.getFor(PositionComponent.class);

    public float x, y;

    public IWorld world;
}
