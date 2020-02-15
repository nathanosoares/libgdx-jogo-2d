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

    public static final int VIEWPORT_SIZE = 20;

    //

    private final GameApplication application;

    //

    private Viewport viewport;

    private OrthographicCamera camera;

    //

    private SpriteBatch spriteBatch;

    private World world;

    private WorldRender worldRender;

    private Player player;

    public GameScreen(GameApplication application) {
        this.application = application;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();

        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        float ratio = (float) displayMode.width / (float) displayMode.height;

        this.viewport = new FillViewport(VIEWPORT_SIZE, VIEWPORT_SIZE / ratio, this.camera);

        this.spriteBatch = new SpriteBatch();

        int mapWidth = 20;
        int mapHeight = 20;

        this.world = new World("world", mapWidth, mapHeight);
        this.worldRender = new WorldRender(spriteBatch, this.camera, world);

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
            this.player.getLocation().y = Math.min(this.player.getLocation().y + delta * 5.0f, this.world.getHeight() - 24 / 10f);
        }

        if (Gdx.input.isKeyPressed(Keys.S)) {
            this.player.getLocation().y = Math.max(this.player.getLocation().y - delta * 5.0f, 0);
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            this.player.getLocation().x = Math.min(this.player.getLocation().x + delta * 5.0f, this.world.getWidth() - 24 / 10f);
        }

        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.player.getLocation().x = Math.max(this.player.getLocation().x - delta * 5.0f, 0);
        }


        this.camera.position.set(this.player.getLocation().x, this.player.getLocation().y, 0);

        float visibleW = viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();//half of world visible
        float visibleH = viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

        this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, this.world.getWidth() - visibleW);
        this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, this.world.getHeight() - visibleH);

        this.camera.update();

        this.worldRender.render();
        this.spriteBatch.begin();
        this.player.draw(this.spriteBatch);
        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
    }
}
