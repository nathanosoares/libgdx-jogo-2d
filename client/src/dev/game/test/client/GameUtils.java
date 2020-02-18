package dev.game.test.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameUtils {

    public static void clearScreen(int red, int green, int blue, int alpha) {
        Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
