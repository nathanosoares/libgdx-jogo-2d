package dev.game.test.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.api.IClientGame;
import dev.game.test.api.world.IWorld;
import dev.game.test.client.GameUtils;
import dev.game.test.client.world.systems.WorldRenderSystem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameScreen extends ScreenAdapter {

    public static final int VIEWPORT_SIZE = 20;

    private final IClientGame clientGame;

    private Viewport viewport;

    private OrthographicCamera camera;

    private SpriteBatch spriteBatch;

    @Getter
    private Vector2 hover = new Vector2();

    @Override
    public void show() {
        this.camera = new OrthographicCamera();

        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        float ratio = (float) displayMode.width / (float) displayMode.height;

        this.viewport = new FillViewport(VIEWPORT_SIZE, VIEWPORT_SIZE / ratio, this.camera);

        this.spriteBatch = new SpriteBatch();

        // TODO dar setup da engine do IGame
//        engine.addSystem(new WorldRenderSystem(world, this.camera, this.spriteBatch, this.viewport));
//        engine.addSystem(new VisualRenderSystem(this.spriteBatch));
//        engine.addSystem(new LocalEntityControllerSystem(this));
//        engine.addSystem(new CollisiveDebugSystem(this.spriteBatch));
//        engine.addSystem(new AnimateStateSystem());
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {

        GameUtils.clearScreen(0, 0, 0, 100);

        if (this.clientGame.getClientManager().getPlayer() != null) {
            Vector2 playerPosition = this.clientGame.getClientManager().getPlayer().getPosition();

            this.camera.position.set(playerPosition.x, playerPosition.y, 0);

            float visibleW = viewport.getWorldWidth() / 2.0f +
                    (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();

            float visibleH = viewport.getWorldHeight() / 2.0f +
                    (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

            WorldRenderSystem renderSystem = clientGame.getEngine().getSystem(WorldRenderSystem.class);
            IWorld worldClient = renderSystem.getWorld();

            this.camera.position.x = MathUtils
                    .clamp(this.camera.position.x, visibleW, worldClient.getBounds().getWidth() - visibleW);

            this.camera.position.y = MathUtils
                    .clamp(this.camera.position.y, visibleH, worldClient.getBounds().getHeight() - visibleH);

            this.camera.update();
        }
    }

    @Override
    public void dispose() {
    }

}
