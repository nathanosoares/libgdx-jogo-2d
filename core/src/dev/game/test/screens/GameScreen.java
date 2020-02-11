package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.inputs.GameInputAdapter;
import dev.game.test.net.GameServerConnection;
import dev.game.test.net.client.ClientGameNet;
import dev.game.test.world.Player;
import lombok.Getter;

import java.util.Optional;

public class GameScreen extends ScreenAdapter {

    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;

    private final GameApplication application;

    @Getter
    private OrthographicCamera camera;

    private BitmapFont font;

    private SpriteBatch batch;

    private Player player;

    private TiledMap tiledMap;

    private TiledMapTileLayer groundLayer;

    private BatchTiledMapRenderer tiledMapRenderer;


    private Texture clickedTextureSprite = new Texture(Gdx.files.internal("tile_clicked.png"));
    private Texture textureSprite = new Texture(Gdx.files.internal("tile.png"));

    private Cell lastCellHover = null;

    public GameScreen(GameApplication application) {
        this.application = application;

        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.player = new Player();

        this.tiledMap = new TiledMap();

        MapLayers layers = tiledMap.getLayers();
        this.groundLayer = new TiledMapTileLayer(10, 10, 33, 17);

        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {

                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(new TextureRegion(this.textureSprite)));

                groundLayer.setCell(x, y, cell);
            }
        }

        layers.add(groundLayer);

        this.tiledMapRenderer = new IsometricTiledMapRenderer(tiledMap, 1f);
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.camera.zoom = .5f;

        Gdx.input.setInputProcessor(new GameInputAdapter(this.camera));
    }

    @Override
    public void render(float delta) {
        Vector2 to = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            to.y += 2.5f / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            to.y -= 2.5f / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            to.x += 2.5f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            to.x -= 2.5f;
        }

        if (to.x == 0 && to.y != 0) {
            to.y *= 2;
        }

        this.player.move(to);

        GameUtils.clearScreen(255, 255, 255, 100);
        this.camera.update();

        this.tiledMapRenderer.setView(camera);

        markGroundHover();

        this.tiledMapRenderer.render();

        this.tiledMapRenderer.getBatch().begin();
        this.player.draw(this.tiledMapRenderer.getBatch());

        this.tiledMapRenderer.getBatch().end();

        this.batch.begin();
        String s = Optional.ofNullable(((ClientGameNet) application.getNet()).serverConnection).map(GameServerConnection::getTestString).orElse(null);
        if (s != null) {
            this.font.draw(this.batch, s, 10.0f, 20.0f);
        }

        this.batch.end();
    }

    private void markGroundHover() {
        if (lastCellHover != null) {
            lastCellHover.setTile(new StaticTiledMapTile(new TextureRegion(GameScreen.this.textureSprite)));
        }

        Vector3 point = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        GameScreen.this.camera.unproject(point);

        point.x /= 33;
        point.y = (point.y - 17 / 2f) / 17 + point.x;
        point.x -= point.y - point.x;

        int cellX = (int) Math.floor(point.x);
        int cellY = (int) Math.floor(point.y);

        Cell cell = groundLayer.getCell(cellX, cellY);

        if (cell != null) {
            cell.setTile(new StaticTiledMapTile(new TextureRegion(GameScreen.this.clickedTextureSprite)));
            lastCellHover = cell;
        }
    }

    @Override
    public void dispose() {

    }
}
