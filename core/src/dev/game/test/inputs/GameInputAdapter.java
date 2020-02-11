package dev.game.test.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameInputAdapter extends InputAdapter {

    private static final float MIN_ZOOM = 0.5f;
    private static final float MAX_ZOOM = 1.5f;
    private static final float ZOOM_STEEP = 0.02f;

    private final OrthographicCamera camera;

    private boolean pressed = false;
    private Vector2 cameraOffset = null;

    @Override
    public boolean scrolled(int amount) {
        if (this.camera != null) {

            this.camera.zoom = Math.min(
                    Math.max(this.camera.zoom + (ZOOM_STEEP * amount), MIN_ZOOM),
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
                    screenX + this.camera.position.x,
                    this.camera.position.y - screenY
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
        if (this.camera != null && this.pressed) {
            this.camera.position.x = this.cameraOffset.x - screenX;
            this.camera.position.y = this.cameraOffset.y + screenY;

            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            this.camera.translate(-32, 0);
        }

        if (keycode == Input.Keys.RIGHT) {
            this.camera.translate(32, 0);
        }

        if (keycode == Input.Keys.UP) {
            this.camera.translate(0, -32);
        }

        if (keycode == Input.Keys.DOWN) {
            this.camera.translate(0, 32);
        }

        return false;
    }
}
