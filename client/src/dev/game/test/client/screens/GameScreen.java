package dev.game.test.client.screens;

import com.artemis.WorldConfiguration;
import com.artemis.link.EntityLinkManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
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
import com.sun.corba.se.spi.orbutil.fsm.State;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.ClientConstants;
import dev.game.test.client.GameUtils;
import dev.game.test.client.entity.components.SpriteComponent;
import dev.game.test.client.entity.systems.CollidableDebugSystem;
import dev.game.test.client.entity.systems.PlayerControllerSystem;
import dev.game.test.client.entity.systems.SpriteRenderSystem;
import dev.game.test.client.world.WorldClient;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.Injection;
import dev.game.test.core.entity.EntityFactory;
import dev.game.test.core.entity.components.TransformComponent;
import dev.game.test.core.entity.systems.MovementSystem;

import java.util.Map;

import dev.game.test.core.entity.systems.StateSystem;
import lombok.Getter;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

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

    private com.artemis.World artemis;

    private EntityFactory entityFactory = new EntityFactory();

    @Getter
    private int playerId;

    private EntityTrackerMainWindow entityTrackerMainWindow;

    public GameScreen(ClientApplication application) {
        this.application = application;
        instance = this;
    }

    @Override
    public void show() {
        EntityFactory.postCreate = (world, entityId) -> {
            world.edit(entityId)
                    .add(new SpriteComponent(new Sprite(
                            new Texture("rpg-pack/chars/gabe/gabe-idle-run.png"),
                            5, 2, 16, 22
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

        WorldConfiguration worldConfiguration = new WorldConfiguration();


        worldConfiguration.setSystem(new EntityLinkManager());
        worldConfiguration.setSystem(new MovementSystem());
        worldConfiguration.setSystem(new StateSystem());
        worldConfiguration.setSystem(new PlayerControllerSystem(this));
        worldConfiguration.setSystem(new WorldRenderSystem(world, this.camera, this.spriteBatch, this.viewport));
        worldConfiguration.setSystem(new SpriteRenderSystem(this.spriteBatch));

        worldConfiguration = Injection.injectSingletons(worldConfiguration);

        if (ClientConstants.DEBUG) {
            this.entityTrackerMainWindow = new EntityTrackerMainWindow(false, false);

            worldConfiguration.setSystem(new EntityTracker(entityTrackerMainWindow));
            worldConfiguration.setSystem(new CollidableDebugSystem(this.spriteBatch));
        }

        this.artemis = new com.artemis.World(worldConfiguration);

        this.artemis.inject(entityFactory);

        this.playerId = entityFactory.createPlayer(this.artemis, mapWidth / 2, mapHeight / 2);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {

        if (entityTrackerMainWindow != null) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
                entityTrackerMainWindow.setVisible(!entityTrackerMainWindow.isVisible());
            }
        }

        GameUtils.clearScreen(0, 50, 0, 100);

        TransformComponent transformComponent = this.artemis.getEntity(playerId)
                .getComponent(TransformComponent.class);

        if (transformComponent != null) {
            this.camera.position.set(transformComponent.position.x, transformComponent.position.y, 0);

            float visibleW = viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport
                    .getWorldWidth();//half of world visible
            float visibleH =
                    viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport
                            .getWorldHeight();

            WorldRenderSystem renderSystem = this.artemis.getSystem(WorldRenderSystem.class);

            this.camera.position.x = MathUtils
                    .clamp(this.camera.position.x, visibleW, renderSystem.getWorldClient().getBounds().getWidth() - visibleW);
            this.camera.position.y = MathUtils
                    .clamp(this.camera.position.y, visibleH, renderSystem.getWorldClient().getBounds().getHeight() - visibleH);
            this.camera.update();
        }

        this.artemis.setDelta(delta);
        this.artemis.process();
    }

    @Override
    public void dispose() {
    }
}
