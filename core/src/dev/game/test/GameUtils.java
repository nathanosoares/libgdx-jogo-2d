package dev.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameUtils {

    public static void clearScreen() {
        clearScreen(0, 0, 0, 0);
    }

    public static void clearScreen(int red, int green, int blue, int alpha) {
        Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling
            ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
    }
}
