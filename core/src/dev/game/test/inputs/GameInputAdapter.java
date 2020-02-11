package dev.game.test.inputs;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.screens.GameScreen;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameInputAdapter extends InputAdapter {

    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 1.5f;
    private static final float ZOOM_STEEP = 0.02f;

    private final GameScreen gameScreen;

    private boolean pressed = false;
    private Vector2 cameraOffset = null;

    @Override
    public boolean scrolled(int amount) {
        if (this.gameScreen.getCamera() != null) {

            this.gameScreen.getCamera().zoom = Math.min(
                    Math.max(this.gameScreen.getCamera().zoom + (ZOOM_STEEP * amount), MIN_ZOOM),
                    MAX_ZOOM
            );

            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            this.pressed = true;
            this.cameraOffset = new Vector2(
                    screenX + this.gameScreen.getCamera().position.x,
                    this.gameScreen.getCamera().position.y - screenY
            );

            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            this.pressed = false;
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.gameScreen.getCamera() != null && this.pressed) {
            this.gameScreen.getCamera().position.x = this.cameraOffset.x - screenX;
            this.gameScreen.getCamera().position.y = this.cameraOffset.y + screenY;

            return true;
        }

        return false;
    }
}
