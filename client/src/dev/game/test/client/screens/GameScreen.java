package dev.game.test.client.screens;

import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Maps;
import dev.game.test.client.GameApplication;
import dev.game.test.client.GameUtils;
import dev.game.test.client.entity.components.SpriteComponent;
import dev.game.test.client.entity.systems.PlayerControllerSystem;
import dev.game.test.client.entity.systems.SpriteRenderSystem;
import dev.game.test.client.world.WorldClient;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.entity.EntityFactory;
import dev.game.test.core.entity.components.TransformComponent;
import dev.game.test.core.entity.systems.MovementSystem;
import lombok.Getter;

import java.util.Map;

@Getter
public class GameScreen extends ScreenAdapter {

    public static final int VIEWPORT_SIZE = 20;

    @Getter
    private static GameScreen instance;

    private final GameApplication application;

    private Viewport viewport;

    private OrthographicCamera camera;

    private SpriteBatch spriteBatch;

    private Map<String, WorldClient> worlds = Maps.newHashMap();

    @Getter
    private Vector2 hover = new Vector2();

    private com.artemis.World artemis;

    private EntityFactory entityFactory = new EntityFactory();

    @Getter
    private int playerId;

    public GameScreen(GameApplication application) {
        this.application = application;
        instance = this;
    }

    @Override
    public void show() {
        EntityFactory.postCreate = (world, entityId) -> {
            world.edit(entityId)
                    .add(new SpriteComponent(new Sprite(
                            new Texture("rpg-pack/chars/gabe/gabe-idle-run.png"),
                            5, 0, 24, 24
                    )));
        };

        this.camera = new OrthographicCamera();

        DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        float ratio = (float) displayMode.width / (float) displayMode.height;

        this.viewport = new FillViewport(VIEWPORT_SIZE, VIEWPORT_SIZE / ratio, this.camera);

        this.spriteBatch = new SpriteBatch();

        int mapWidth = 20;
        int mapHeight = 20;

        WorldClient world = new WorldClient("world", mapWidth, mapHeight);

        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(new EntityLinkManager())
                .with(new MovementSystem())
                .with(new PlayerControllerSystem(this))
                .with(new WorldRenderSystem(world, this.camera, this.spriteBatch, this.viewport))
                .with(new SpriteRenderSystem(this.spriteBatch));

//        builder.with(new EntityTracker(new EntityTrackerMainWindow()));

        this.artemis = new com.artemis.World(builder.build());

        this.artemis.inject(entityFactory);

        this.playerId = entityFactory.createPlayer(this.artemis, 0, 0);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        GameUtils.clearScreen(0, 50, 0, 100);

        TransformComponent transformComponent = this.artemis.getEntity(playerId)
                .getComponent(TransformComponent.class);

        if (transformComponent != null) {
            this.camera.position.set(transformComponent.position.x, transformComponent.position.y, 0);

            float visibleW = viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();//half of world visible
            float visibleH = viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();


            WorldRenderSystem renderSystem = this.artemis.getSystem(WorldRenderSystem.class);

            this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, renderSystem.getWorldClient().getBounds().getWidth() - visibleW);
            this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, renderSystem.getWorldClient().getBounds().getHeight() - visibleH);
            this.camera.update();
        }

        this.artemis.setDelta(delta);
        this.artemis.process();
    }

    @Override
    public void dispose() {
    }
}
