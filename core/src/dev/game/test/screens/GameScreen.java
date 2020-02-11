package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dev.game.test.GameUtils;
import dev.game.test.inputs.GameInputAdapter;
import dev.game.test.world.Player;
import lombok.Getter;

public class GameScreen extends ScreenAdapter {

    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;

    @Getter
    private OrthographicCamera camera;

    private SpriteBatch batch;

    private Player player;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    public GameScreen() {
        this.batch = new SpriteBatch();

        this.tiledMap = new TmxMapLoader().load("map.tmx");

        this.tiledMap.getLayers().add(new MapLayer());

        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0f);

        Gdx.input.setInputProcessor(new GameInputAdapter(this.camera));

        this.tiledMap.getLayers().get(0).setVisible(false);

        this.player = new Player();
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.player.getLocation().y += 2.5;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.player.getLocation().y -= 2.5;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.player.getLocation().x += 2.5;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.player.getLocation().x -= 2.5;
        }

        GameUtils.clearScreen(255, 255, 255, 100);

        this.batch.setProjectionMatrix(this.camera.combined);
        this.camera.update();

        this.tiledMapRenderer.setView(camera);
        this.tiledMapRenderer.render();

        this.batch.begin();

        this.player.draw(this.batch);

        this.batch.end();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
    }
}
