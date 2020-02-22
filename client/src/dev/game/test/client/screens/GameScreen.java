package dev.game.test.client.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Maps;
import dev.game.test.api.IClientGame;
import dev.game.test.client.GameUtils;
import dev.game.test.client.entity.components.AnimateStateComponent;
import dev.game.test.client.entity.components.FacingVisualFlipComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.client.entity.systems.AnimateStateSystem;
import dev.game.test.client.entity.systems.CollisiveDebugSystem;
import dev.game.test.client.entity.systems.LocalEntityControllerSystem;
import dev.game.test.client.entity.systems.VisualRenderSystem;
import dev.game.test.client.world.WorldClient;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.state.PlayerState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Random;

@Getter
@RequiredArgsConstructor
public class GameScreen extends ScreenAdapter {

    public static final int VIEWPORT_SIZE = 20;

    //

    private final IClientGame clientGame;

    //

    private Viewport viewport;

    private OrthographicCamera camera;

    private SpriteBatch spriteBatch;

    private Map<String, WorldClient> worlds = Maps.newHashMap();

    @Getter
    private Vector2 hover = new Vector2();

    @Getter
    private Entity localEntity;

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

        Engine engine = clientGame.getEngine();

        engine.addSystem(new WorldRenderSystem(world, this.camera, this.spriteBatch, this.viewport));
        engine.addSystem(new VisualRenderSystem(this.spriteBatch));
        engine.addSystem(new LocalEntityControllerSystem(this));
        engine.addSystem(new CollisiveDebugSystem(this.spriteBatch));
        engine.addSystem(new AnimateStateSystem());

        this.localEntity = buildLocalPlayer(world);
        this.localEntity.getComponent(PositionComponent.class).x = mapHeight / 2f;
        this.localEntity.getComponent(PositionComponent.class).y = mapHeight / 2f;

        engine.addEntity(this.localEntity);
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

            WorldRenderSystem renderSystem = clientGame.getEngine().getSystem(WorldRenderSystem.class);
            WorldClient worldClient = renderSystem.getWorldClient();

            this.camera.position.x = MathUtils
                    .clamp(this.camera.position.x, visibleW, worldClient.getBounds().getWidth() - visibleW);

            this.camera.position.y = MathUtils
                    .clamp(this.camera.position.y, visibleH, worldClient.getBounds().getHeight() - visibleH);

            this.camera.update();
        }

        clientGame.getEngine().update(delta);
    }

    @Override
    public void dispose() {
    }

    private Player buildLocalPlayer(WorldClient world) {

        Texture texture = new Texture("rpg-pack/chars/gabe/gabe-idle-run.png");
        String username;

        if (System.getProperty("username") != null) {
            username = System.getProperty("username");
        } else {
            username = String.format("Player%d", new Random().nextInt(1000));
        }

        Player player = new Player(username, world);

        player.add(new VisualComponent());
        player.add(new FacingVisualFlipComponent());

        // Animate
        {
            TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 7, texture.getHeight());

            TextureRegion[] idleFrames = new TextureRegion[1];
            TextureRegion[] walkFrames = new TextureRegion[6];

            int index = 0;
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    idleFrames[0] = tmp[0][i];
                    continue;
                }
                walkFrames[index++] = tmp[0][i];
            }

            Animation<TextureRegion> idleAnimation = new Animation<>(0.135f, idleFrames);
            Animation<TextureRegion> walkAnimation = new Animation<>(0.135f, walkFrames);
            Animation<TextureRegion> runningAnimation = new Animation<>(0.105f, walkFrames);


            AnimateStateComponent animateStateComponent = new AnimateStateComponent();

            animateStateComponent.animations.put(PlayerState.IDLE, idleAnimation);
            animateStateComponent.animations.put(PlayerState.WALK, walkAnimation);
            animateStateComponent.animations.put(PlayerState.RUNNING, runningAnimation);

            player.add(animateStateComponent);
        }

        return player;
    }
}
