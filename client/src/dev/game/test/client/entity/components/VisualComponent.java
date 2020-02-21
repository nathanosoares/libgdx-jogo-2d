package dev.game.test.client.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.core.entity.components.StateComponent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class VisualComponent implements Component {

    public static final ComponentMapper<VisualComponent> MAPPER = ComponentMapper.getFor(VisualComponent.class);

    public TextureRegion region;
}