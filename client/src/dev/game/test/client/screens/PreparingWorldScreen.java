package dev.game.test.client.screens;

import com.badlogic.gdx.ScreenAdapter;
import dev.game.test.client.GameUtils;

public class PreparingWorldScreen extends ScreenAdapter {

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(250, 0, 0, 0);
    }

}
