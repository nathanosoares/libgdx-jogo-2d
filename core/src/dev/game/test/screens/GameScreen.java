package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.world.World;
import dev.game.test.world.render.GameMapRenderer;
import dev.game.test.world.entity.Player;
import dev.game.test.world.render.WorldRender;

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

//    private SpriteBatch spriteBatch;

//    private TiledMap map;
//    private GameMapRenderer mapRenderer;

    private WorldRender worldRender;
    private Player player;

    public GameScreen(GameApplication application) {
        this.application = application;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
//        this.camera.zoom = 1.5f;

        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        float ratio = (float) displayMode.width / (float) displayMode.height;
        System.out.println(displayMode);
        System.out.println(ratio);

        this.viewport = new FillViewport(VIEWPORT_SIZE, VIEWPORT_SIZE / ratio, this.camera);

        int mapWidth = 20;
        int mapHeight = 20;

        World world = new World("world", mapWidth, mapHeight);
        this.worldRender = new WorldRender(world);

//        this.spriteBatch = new SpriteBatch();
//
//        this.map = new TmxMapLoader().load("map/test.tmx");
//        this.mapRenderer = new GameMapRenderer(this.map, UNIT_PER_PIXEL);

//        MapProperties prop = this.map.getProperties();
//
//        int mapWidth = prop.get("width", Integer.class);
//        int mapHeight = prop.get("height", Integer.class);
//        int tilePixelWidth = prop.get("tilewidth", Integer.class);
//        int tilePixelHeight = prop.get("tileheight", Integer.class);

        this.player = new Player();
        this.player.setLocation(new Vector2(mapWidth / 2f, mapHeight / 2f));
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(0, 50, 0, 100);

        if (Gdx.input.isKeyPressed(Keys.W)) {
            this.player.getLocation().y = Math.min(this.player.getLocation().y + delta * 5.0f, WORLD_SIZE - 24 / 10f);
        }

        if (Gdx.input.isKeyPressed(Keys.S)) {
            this.player.getLocation().y = Math.max(this.player.getLocation().y - delta * 5.0f, 0);
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            this.player.getLocation().x = Math.min(this.player.getLocation().x + delta * 5.0f, WORLD_SIZE - 24 / 10f);
        }

        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.player.getLocation().x = Math.max(this.player.getLocation().x - delta * 5.0f, 0);
        }


        this.camera.position.set(this.player.getLocation().x, this.player.getLocation().y, 0);

        float visibleW = viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();//half of world visible
        float visibleH = viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

        this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, WORLD_SIZE - visibleW);
        this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, WORLD_SIZE - visibleH);

        this.camera.update();

//        this.mapRenderer.setView(this.camera);
//
//        this.mapRenderer.render(new int[]{0});
//        this.mapRenderer.renderEntity(this.player);
//        this.mapRenderer.renderDecorations(this.player);
    }

    @Override
    public void dispose() {
        this.worldRender.dispose();
    }
}
