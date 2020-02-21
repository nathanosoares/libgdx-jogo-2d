package dev.game.test.core.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class CollidableComponent implements Component {

    public final Rectangle box = new Rectangle();

    public CollidableComponent(float width, float height) {
        this.box.setWidth(width);
        this.box.setHeight(height);
    }
}
