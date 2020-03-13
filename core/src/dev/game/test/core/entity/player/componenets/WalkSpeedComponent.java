package dev.game.test.core.entity.player.componenets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WalkSpeedComponent implements Component {

    public static final ComponentMapper<WalkSpeedComponent> MAPPER = ComponentMapper.getFor(WalkSpeedComponent.class);


    public float speed;
}
