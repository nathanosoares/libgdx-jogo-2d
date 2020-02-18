package dev.game.test.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class RigidBodyComponent extends Component {

    public final Vector2 velocity = new Vector2();

    public final float walkSpeed = 1;
}
