package dev.game.test.client.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Lists;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.keybind.Keybind;
import dev.game.test.api.net.packet.client.PacketKeybindActivate;
import dev.game.test.api.net.packet.client.PacketKeybindDeactivate;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.api.world.IWorld;
import dev.game.test.client.entity.systems.AnimateStateSystem;
import dev.game.test.client.entity.systems.CollisiveDebugSystem;
import dev.game.test.client.entity.systems.VisualRenderSystem;
import dev.game.test.client.world.systems.WorldRenderSystem;
import dev.game.test.core.entity.components.KeybindComponent;
import dev.game.test.core.registry.impl.RegistryKeybinds;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GameScreen extends ScreenAdapter {

    public static final int VIEWPORT_SIZE = 20;

    private final IClientGame game;

    private Viewport viewport;

    private OrthographicCamera camera;

    private SpriteBatch spriteBatch;

    @Getter
    private Vector2 hover = new Vector2();

    private List<EntitySystem> systems = Lists.newArrayList();

    private EntitySystem registerSystem(EntitySystem system) {
        this.systems.add(system);
        this.game.getEngine().addSystem(system);

        return system;
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();

        DisplayMode displayMode = Gdx.graphics.getDisplayMode();

        float ratio = (float) displayMode.width / (float) displayMode.height;

        this.viewport = new FillViewport(VIEWPORT_SIZE, VIEWPORT_SIZE / ratio, this.camera);

        this.spriteBatch = new SpriteBatch();

        this.registerSystem(new WorldRenderSystem(this.game, this.camera, this.spriteBatch, this.viewport));
        this.registerSystem(new VisualRenderSystem(this.game, this.spriteBatch));
        this.registerSystem(new CollisiveDebugSystem(this.game, this.spriteBatch));
        this.registerSystem(new AnimateStateSystem(this.game));

        Gdx.input.setInputProcessor(new PlayerControllerInputAdapter());
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        if (this.game.getConnectionHandler().getConnectionManager() == null) {
            return;
        }
        if (this.game.getConnectionHandler().getConnectionManager().getState() != PacketConnectionState.State.INGAME) {
            return;
        }

        IWorld currentWorld = this.game.getClientManager().getCurrentWorld();
        IPlayer currentPlayer = this.game.getClientManager().getPlayer();

        if (currentWorld != null && currentPlayer != null) {
            Vector2 playerPosition = currentPlayer.getPosition();

            this.camera.position.set(playerPosition.x, playerPosition.y, 0);

            float visibleW = viewport.getWorldWidth() / 2.0f +
                    (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();

            float visibleH = viewport.getWorldHeight() / 2.0f +
                    (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

            this.camera.position.x = MathUtils
                    .clamp(this.camera.position.x, visibleW, currentWorld.getBounds().getWidth() - visibleW);

            this.camera.position.y = MathUtils
                    .clamp(this.camera.position.y, visibleH, currentWorld.getBounds().getHeight() - visibleH);

            this.camera.update();
        }
    }

    @Override
    public void dispose() {
        this.systems.forEach(this.game.getEngine()::removeSystem);
    }

    private class PlayerControllerInputAdapter extends InputAdapter {


        @Override
        public boolean keyDown(int keycode) {
            if (GameScreen.this.game.getClientManager().getPlayer() == null) {
                return super.keyDown(keycode);
            }

            KeybindComponent activatedKeybinds = KeybindComponent.MAPPER
                    .get((Entity) GameScreen.this.game.getClientManager().getPlayer());

            RegistryKeybinds keybindRegistry = GameScreen.this.game.getRegistryManager().getRegistry(Keybind.class);
            Keybind keybind = keybindRegistry.getKeybindFromKey(keycode);

            if (keybind != null) {
                activatedKeybinds.activeKeybinds.add(keybind);

                GameScreen.this.game.getConnectionHandler().queuePacket(new PacketKeybindActivate(keybind.getId()));
                return true;
            }

            return super.keyDown(keycode);
        }

        @Override
        public boolean keyUp(int keycode) {
            if (GameScreen.this.game.getClientManager().getPlayer() == null) {
                return super.keyUp(keycode);
            }

            KeybindComponent activatedKeybinds = KeybindComponent.MAPPER
                    .get((Entity) GameScreen.this.game.getClientManager().getPlayer());

            RegistryKeybinds keybindRegistry = GameScreen.this.game.getRegistryManager().getRegistry(Keybind.class);
            Keybind keybind = keybindRegistry.getKeybindFromKey(keycode);

            if (keybind != null) {
                activatedKeybinds.activeKeybinds.remove(keybind);

                GameScreen.this.game.getConnectionHandler().queuePacket(new PacketKeybindDeactivate(keybind.getId()));
                return true;
            }

            return super.keyUp(keycode);
        }
    }
}
