package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.world.World;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.entity.Player;
import dev.game.test.world.render.WorldRender;
import lombok.Getter;

@Getter
public class GameScreen extends ScreenAdapter {

    public static final int VIEWPORT_SIZE = 20;

    @Getter
    private static GameScreen instance;

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

    @Getter
    private Vector2 hover = new Vector2();

    public GameScreen(GameApplication application) {
        this.application = application;
        instance = this;
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
        this.worldRender = new WorldRender(spriteBatch, world);
        this.worldRender.setView(this.camera);

        this.player = new Player();
        this.player.setPosition(new Vector2(mapWidth / 2f, mapHeight / 2f));

        this.world.addEntity(this.player);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(0, 50, 0, 100);

        Vector2 moveTo = new Vector2();

        this.player.setRunning(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT));

        if (Gdx.input.isKeyPressed(Keys.W)) {
            moveTo.y += delta * this.player.getSpeed();
        }

        if (Gdx.input.isKeyPressed(Keys.S)) {
            moveTo.y -= delta * this.player.getSpeed();
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            moveTo.x += delta * this.player.getSpeed();
        }

        if (Gdx.input.isKeyPressed(Keys.A)) {
            moveTo.x -= delta * this.player.getSpeed();
        }

        this.player.move(moveTo);

        this.camera.position.set(this.player.getPosition().x, this.player.getPosition().y, 0);

        float visibleW = viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();//half of world visible
        float visibleH = viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

        this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, this.world.getWidth() - visibleW);
        this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, this.world.getHeight() - visibleH);

        this.camera.update();

        Vector3 mouseInWorld3D = new Vector3();

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;


        if (Gdx.input.isTouched()) {
            this.world.getLayers()[0].getBlock(this.hover.x, this.hover.y).setBlock(World.DIRT);
        }

        this.camera.unproject(mouseInWorld3D);

        this.hover.x = (float) Math.floor(mouseInWorld3D.x);
        this.hover.y = (float) Math.floor(mouseInWorld3D.y);


        this.worldRender.setView(this.camera);
        this.worldRender.render();
        this.worldRender.renderEntities();

//        this.spriteBatch.begin();
//        this.player.draw(this.spriteBatch);
//        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
    }
}
