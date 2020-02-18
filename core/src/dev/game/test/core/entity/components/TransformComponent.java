package dev.game.test.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransformComponent extends Component {

    public Vector2 position = new Vector2();

    public Vector2 origin = new Vector2();

    public boolean originCenter = false;

    public float scaleX = 1;
    public float scaleY = 1;
}
