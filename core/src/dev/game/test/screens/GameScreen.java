package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.game.test.GameUtils;
import dev.game.test.inputs.GameInputAdapter;
import dev.game.test.world.Player;
import lombok.Getter;

public class GameScreen extends ScreenAdapter {

    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;

    @Getter
    private OrthographicCamera camera;

    private Player player;

    private TextureMapObject playerObject;

    private TiledMap tiledMap;

    private BatchTiledMapRenderer tiledMapRenderer;

    private TiledMapTileLayer floor;

    public GameScreen() {
        this.player = new Player();

        this.tiledMap = new TiledMap();
        Texture textureSprite = new Texture(Gdx.files.internal("tile.png"));

        MapLayers layers = tiledMap.getLayers();

        this.floor = new TiledMapTileLayer(5, 5, 33, 17);
        this.floor.setName("floor");

        for (int x = 0; x < this.floor.getWidth(); x++) {
            for (int y = 0; y < this.floor.getHeight(); y++) {

                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(new TextureRegion(textureSprite)));

                this.floor.setCell(x, y, cell);
            }
        }

        layers.add(this.floor);

        MapLayer playerLayer = new MapLayer();
        this.playerObject = new TextureMapObject(this.player);
        this.playerObject.setX(0.0f);
        this.playerObject.setY(0.0f);

        playerLayer.getObjects().add(this.playerObject);
        layers.add(playerLayer);

        this.tiledMapRenderer = new IsometricTiledMapRenderer(tiledMap, 4.0f);

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera(WIDTH, HEIGHT);

        Gdx.input.setInputProcessor(new GameInputAdapter(this.camera));
    }

    @Override
    public void render(float delta) {
        Vector2 newLocation = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newLocation.y += 2.5;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newLocation.y -= 2.5;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newLocation.x += 2.5;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newLocation.x -= 2.5;
        }

        this.player.getLocation().add(newLocation);

        GameUtils.clearScreen(255, 255, 255, 100);
        this.camera.update();

        this.tiledMapRenderer.setView(camera);
        this.tiledMapRenderer.render();

        this.tiledMapRenderer.getBatch().begin();
        this.player.draw(this.tiledMapRenderer.getBatch());
        this.tiledMapRenderer.getBatch().end();
    }

    @Override
    public void dispose() {

    }
}
