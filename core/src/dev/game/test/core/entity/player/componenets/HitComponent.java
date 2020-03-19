package dev.game.test.core.entity.player.componenets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class HitComponent implements Component {

    public static final ComponentMapper<HitComponent> MAPPER = ComponentMapper.getFor(HitComponent.class);

    public boolean pending = false;
    public float delay = 100f;
    public float time = 0;
    public boolean hitting = false;
    public boolean onRight = true;
    public double degrees = 0;

}
