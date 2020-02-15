package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.world.World;
import dev.game.test.world.block.BlockState;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.entity.Player;
import dev.game.test.world.render.WorldRender;
import lombok.Getter;

import java.util.UUID;

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
        this.worldRender.setViewport(this.viewport);

        this.player = new Player(UUID.randomUUID());
        this.player.setPosition(new Vector2(mapWidth / 2f, mapHeight / 2f));

        this.world.addEntity(this.player);

        Gdx.input.setInputProcessor(new InputAdapter() {

            private int button;

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return action(screenX, screenY, button);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                this.button = button;

                return action(screenX, screenY, button);
            }

            private boolean action(int screenX, int screenY, int button) {
                Vector2 mouseScreenPosition = new Vector2(screenX, screenY);
                Vector2 mouseWorldPosition = viewport.unproject(mouseScreenPosition);

                if (world.getBounds().contains(mouseWorldPosition)) {
                    BlockState blockState = world.getLayers()[0].getBlockState(mouseWorldPosition.x, mouseWorldPosition.y);

                    if (button == Input.Buttons.RIGHT) {
                        if (blockState.getBlock() != Blocks.GRASS) {
                            blockState.setBlock(Blocks.GRASS);
                            blockState.getBlock().neighbourUpdate(blockState);
                        }
                    } else if (button == Input.Buttons.MIDDLE) {
                        World.CLIPBOARD = blockState.getBlock();
                    } else {
                        if (blockState.getBlock() != World.CLIPBOARD) {
                            blockState.setBlock(World.CLIPBOARD);
                            blockState.getBlock().neighbourUpdate(blockState);
                            blockState.getBlock().onBlockNeighbourUpdate(blockState, null);
                        }
                    }

                    return true;
                }

                return false;
            }
        });
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

        this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, this.world.getBounds().getWidth() - visibleW);
        this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, this.world.getBounds().getHeight() - visibleH);

        this.camera.update();

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
