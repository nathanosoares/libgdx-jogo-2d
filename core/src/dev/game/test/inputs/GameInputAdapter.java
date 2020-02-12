package dev.game.test.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameInputAdapter extends InputAdapter {

    private static final float MAX_ZOOM = 45.0f;
    private static final float MIN_ZOOM = 175.0f;
    private static final float ZOOM_STEEP = 0.5f;

    private final PerspectiveCamera camera;

    private boolean pressed = false;
    private Vector2 cameraOffset = null;

    @Override
    public boolean scrolled(int amount) {
        if (this.camera != null) {

            this.camera.fieldOfView = Math.min(
                    Math.max(this.camera.fieldOfView + (ZOOM_STEEP * amount), MAX_ZOOM),
                    MIN_ZOOM
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
            this.camera.translate(-32, 0, 0);
        }

        if (keycode == Input.Keys.RIGHT) {
            this.camera.translate(32, 0, 0);
        }

        if (keycode == Input.Keys.UP) {
            this.camera.translate(0, -32, 0);
        }

        if (keycode == Input.Keys.DOWN) {
            this.camera.translate(0, 32, 0);
        }

        return false;
    }
}
