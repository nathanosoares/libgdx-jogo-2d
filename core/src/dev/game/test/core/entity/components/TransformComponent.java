package dev.game.test.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransformComponent extends Component {

    public final Vector2 position = new Vector2();

    public final Vector2 origin = new Vector2();

    public boolean originCenter = true;

    public float rotation = 0;

    public float scaleX = 1;
    public float scaleY = 1;

    public float width = 1;
    public float height = 1;
}
