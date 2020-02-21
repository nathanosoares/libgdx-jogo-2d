package dev.game.test.client.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Maps;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.GameUtils;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.client.entity.systems.CollidableDebugSystem;
import dev.game.test.client.entity.systems.LocalEntityControllerSystem;
import dev.game.test.client.entity.systems.VisualRenderSystem;
import dev.game.test.client.world.WorldClient;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.entity.components.CollidableComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.systems.MovementSystem;
import lombok.Getter;

import javax.inject.Inject;
import java.util.Map;

@Getter
public class GameScreen extends ScreenAdapter {

    public static final int VIEWPORT_SIZE = 20;

    @Getter
    private static GameScreen instance;

    private final ClientApplication application;

    private Viewport viewport;

    private OrthographicCamera camera;

    private SpriteBatch spriteBatch;

    private Map<String, WorldClient> worlds = Maps.newHashMap();

    @Getter
    private Vector2 hover = new Vector2();

    @Getter
    private Entity localEntity;

    @Inject
    public GameScreen(ClientApplication application) {
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

        WorldClient world = new WorldClient("world", mapWidth, mapHeight);

        this.application.getEngine().addSystem(new WorldRenderSystem(world, this.camera, this.spriteBatch, this.viewport));
        this.application.getEngine().addSystem(new VisualRenderSystem(this.spriteBatch));
        this.application.getEngine().addSystem(new LocalEntityControllerSystem(this));
        this.application.getEngine().addSystem(new CollidableDebugSystem(this.spriteBatch));

        this.localEntity = new Entity();
        this.localEntity.add(new PositionComponent(0, 0));
        this.localEntity.add(new CollidableComponent(16 / 16f, 22 / 16f));
        this.localEntity.add(new MovementComponent());
        this.localEntity.add(new VisualComponent(new TextureRegion(
                new Texture("rpg-pack/chars/gabe/gabe-idle-run.png"),
                5, 2, 16, 22
        )));

        this.application.getEngine().addEntity(this.localEntity);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {

        GameUtils.clearScreen(0, 50, 0, 100);

        PositionComponent localEntityPosition = this.localEntity.getComponent(PositionComponent.class);

        if (localEntityPosition != null) {
            this.camera.position.set(localEntityPosition.x, localEntityPosition.y, 0);

            float visibleW = viewport.getWorldWidth() / 2.0f +
                    (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();

            float visibleH = viewport.getWorldHeight() / 2.0f +
                    (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

            WorldRenderSystem renderSystem = this.application.getEngine().getSystem(WorldRenderSystem.class);
            WorldClient worldClient = renderSystem.getWorldClient();

            this.camera.position.x = MathUtils
                    .clamp(this.camera.position.x, visibleW, worldClient.getBounds().getWidth() - visibleW);

            this.camera.position.y = MathUtils
                    .clamp(this.camera.position.y, visibleH, worldClient.getBounds().getHeight() - visibleH);

            this.camera.update();
        }

        this.application.getEngine().update(delta);
    }

    @Override
    public void dispose() {
    }
}
