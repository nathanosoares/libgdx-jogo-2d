package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class GravityComponent implements Component {

    public static final ComponentMapper<GravityComponent> MAPPER = ComponentMapper.getFor(GravityComponent.class);

    @NonNull
    public float gravity;
    public Body body;

}
