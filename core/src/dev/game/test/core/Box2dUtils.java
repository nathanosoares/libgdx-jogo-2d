package dev.game.test.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

public class Box2dUtils {


    public static ChainShape createEllipse(float width, float height, int steps) {
        ChainShape ellipse = new ChainShape();
        Vector2[] verts = new Vector2[steps];

        for (int i = 0; i < steps; i++) {
            float t = (float) (i * 2 * Math.PI) / steps;


            verts[i] = new Vector2(width * (float) Math.cos(t), height * (float) Math.sin(t));
        }

        ellipse.createLoop(verts);
        return ellipse;
    }
}
