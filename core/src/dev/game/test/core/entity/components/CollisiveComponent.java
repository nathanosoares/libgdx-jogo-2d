package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Rectangle;

public class CollisiveComponent implements Component {

    public static final ComponentMapper<CollisiveComponent> MAPPER = ComponentMapper.getFor(CollisiveComponent.class);

    public final Rectangle box = new Rectangle();

    public CollisiveComponent(float width, float height) {
        this.box.setWidth(width);
        this.box.setHeight(height);
    }
}
