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
import com.google.common.collect.Maps;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.world.World;
import dev.game.test.world.block.BlockState;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.entity.Player;
import dev.game.test.world.render.WorldRender;
import lombok.Getter;

import java.util.Map;

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

    private WorldRender worldRender;

    private Player player;

    private Map<String, World> worlds = Maps.newHashMap();

    @Getter
    private Vector2 hover = new Vector2();

    public GameScreen(Player player, GameApplication application) {
        this.application = application;
        this.player = player;
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

        {
            World world = new World("world", mapWidth, mapHeight);
            registerWorld(world);

            world.addEntity(this.player, (int) (mapWidth / 2f), (int) (mapHeight / 2f));

            setCurrentWorld(world);
        }

        {
            World world = new World("test", mapWidth, mapHeight);
            registerWorld(world);
        }

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
                if (worldRender == null) {
                    return false;
                }

                Vector2 mouseScreenPosition = new Vector2(screenX, screenY);
                Vector2 mouseWorldPosition = viewport.unproject(mouseScreenPosition);

                if (worldRender.getWorld().getBounds().contains(mouseWorldPosition)) {
                    BlockState blockState = worldRender.getWorld().getLayers()[0].getBlockState(mouseWorldPosition.x, mouseWorldPosition.y);

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

        if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
            String currentWorldName = this.worldRender.getWorld().getName();
            if (currentWorldName.equals("test")) {
                setCurrentWorld(worlds.get("world"));
            } else {
                setCurrentWorld(worlds.get("test"));
            }

            this.worldRender.getWorld().addEntity(this.player, this.player.getBody().getPosition());
        }

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

        if (this.worldRender != null) {
            this.camera.position.set(this.player.getBody().getPosition().x, this.player.getBody().getPosition().y, 0);

            float visibleW = viewport.getWorldWidth() / 2.0f + (float) viewport.getScreenX() / (float) viewport.getScreenWidth() * viewport.getWorldWidth();//half of world visible
            float visibleH = viewport.getWorldHeight() / 2.0f + (float) viewport.getScreenY() / (float) viewport.getScreenHeight() * viewport.getWorldHeight();

            this.camera.position.x = MathUtils.clamp(this.camera.position.x, visibleW, this.worldRender.getWorld().getBounds().getWidth() - visibleW);
            this.camera.position.y = MathUtils.clamp(this.camera.position.y, visibleH, this.worldRender.getWorld().getBounds().getHeight() - visibleH);
            this.camera.update();

            this.worldRender.setView(this.camera);
            this.worldRender.render();
            this.worldRender.renderEntities();
        }

//        this.spriteBatch.begin();
//        this.player.draw(this.spriteBatch);
//        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
        this.worldRender.dispose();
    }

    private void setCurrentWorld(World world) {
        this.worldRender = new WorldRender(spriteBatch, world);
        this.worldRender.setView(this.camera);
        this.worldRender.setViewport(this.viewport);
    }

    private World registerWorld(World world) {
        if (this.worlds.containsKey(world.getName())) {
            throw new RuntimeException("Nome de mundo em uso");
        }

        worlds.put(world.getName(), world);

        return world;
    }
}