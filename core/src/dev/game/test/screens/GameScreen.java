package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;

public class GameScreen extends ScreenAdapter {

    public static final float UNIT_PER_PIXEL = 1.0f / 16.0f;

    public static final int VIEWPORT_SIZE = 15;

    public static final int WORLD_SIZE = 20;

    //

    private final GameApplication application;

    //

    private Viewport viewport;

    private OrthographicCamera camera;

    //

    private SpriteBatch spriteBatch;

    private TiledMap map;
    private BatchTiledMapRenderer mapRenderer;

    private Texture playerTexture;
    private TextureRegion playerTextureRegion;

    //

    private Vector2 playerLocation = new Vector2();

    public GameScreen(GameApplication application) {
        this.application = application;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();

        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        float ratio = (float) displayMode.width / (float) displayMode.height;
        System.out.println(displayMode);
        System.out.println(ratio);

        this.viewport = new FillViewport(VIEWPORT_SIZE, VIEWPORT_SIZE / ratio, this.camera);

        this.spriteBatch = new SpriteBatch();

        this.map = new TmxMapLoader().load("map/test.tmx");
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, UNIT_PER_PIXEL);

        this.playerTexture = new Texture(Gdx.files.internal("rpg-pack/chars/gabe/gabe-idle-run.png"));
        this.playerTextureRegion = new TextureRegion(this.playerTexture, 0, 0, 24, 24);

        this.playerLocation.x = this.map.getProperties().get("width", Integer.class) / 2;
        this.playerLocation.y = this.map.getProperties().get("height", Integer.class) / 2;
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(0, 50, 0, 100);

        if(Gdx.input.isKeyPressed(Keys.W)) {
            this.playerLocation.y = Math.min(this.playerLocation.y + delta * 5.0f, WORLD_SIZE - 1.8f);
        }

        if(Gdx.input.isKeyPressed(Keys.S)) {
            this.playerLocation.y = Math.max(this.playerLocation.y - delta * 5.0f, 0);
        }

        if(Gdx.input.isKeyPressed(Keys.D)) {
            this.playerLocation.x = Math.min(this.playerLocation.x + delta * 5.0f, WORLD_SIZE - 1.8f);
        }

        if(Gdx.input.isKeyPressed(Keys.A)) {
            this.playerLocation.x = Math.max(this.playerLocation.x - delta * 5.0f, 0);
        }

        this.camera.position.set(this.playerLocation.x, this.playerLocation.y, 0);

        float visibleW =  viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();//half of world visible
        float visibleH = viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

        this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, WORLD_SIZE - visibleW);
        this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, WORLD_SIZE - visibleH);

        this.camera.update();

        this.mapRenderer.setView(this.camera);

        this.mapRenderer.render(new int[] { 0 });
        this.mapRenderer.render(new int[] { 1 });

        this.spriteBatch.begin();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);
        this.spriteBatch.draw(this.playerTextureRegion, this.playerLocation.x, this.playerLocation.y, 1.8f, 1.8f);
        this.spriteBatch.end();

        this.mapRenderer.render(new int[] { 2 });
    }

    @Override
    public void dispose() {
        this.map.dispose();
    }
}
