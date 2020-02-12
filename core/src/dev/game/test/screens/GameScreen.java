package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import lombok.Getter;

public class GameScreen extends ScreenAdapter {

    public static final int WORLD_SIZE = 500;

    //

    private final GameApplication application;

    //

    private Viewport viewport;

    private OrthographicCamera camera;

    //

    private SpriteBatch spriteBatch;

    private Texture texture;

    public GameScreen(GameApplication application) {
        this.application = application;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_SIZE * 2, WORLD_SIZE * 2, this.camera);

        this.spriteBatch = new SpriteBatch();
        this.texture = new Texture(Gdx.files.internal("tile.png"));
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(0, 50, 0, 100);

        this.camera.update();

        this.spriteBatch.begin();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);

        this.spriteBatch.draw(this.texture, -WORLD_SIZE / 2, - WORLD_SIZE / 2, WORLD_SIZE, WORLD_SIZE);

        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
    }
}
