package dev.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameUtils {

    public static void clearScreen() {
        clearScreen(255, 255, 255, 255);
    }

    public static void clearScreen(int red, int green, int blue, int alpha) {
        Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}