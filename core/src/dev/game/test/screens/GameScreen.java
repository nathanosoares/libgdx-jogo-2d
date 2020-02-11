package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.inputs.GameInputAdapter;
import dev.game.test.net.client.ClientGameNet;
import dev.game.test.world.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class GameScreen extends ScreenAdapter {

    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;

    private final GameApplication application;

    @Getter
    private OrthographicCamera camera;

    private BitmapFont font;

    private Player player;

    private TiledMap tiledMap;

    private TiledMapTileLayer groundLayer;

    private BatchTiledMapRenderer tiledMapRenderer;

    public GameScreen(GameApplication application) {
        this.application = application;

        this.font = new BitmapFont();
        ;
        this.player = new Player();

        this.tiledMap = new TiledMap();
        Texture textureSprite = new Texture(Gdx.files.internal("tile.png"));
        Texture clickedTextureSprite = new Texture(Gdx.files.internal("tile_clicked.png"));

        MapLayers layers = tiledMap.getLayers();
        this.groundLayer = new TiledMapTileLayer(10, 10, 33, 17);

        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {

                Cell cell = new Cell();
                if (x == 3 && y == 0) {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(clickedTextureSprite)));
                } else {
                    cell.setTile(new StaticTiledMapTile(new TextureRegion(textureSprite)));
                }
                groundLayer.setCell(x, y, cell);
            }
        }

        layers.add(groundLayer);

        this.tiledMapRenderer = new IsometricTiledMapRenderer(tiledMap, 1);
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera(WIDTH, HEIGHT);

        Gdx.input.setInputProcessor(new GameInputAdapter(this.camera) {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                super.touchUp(screenX, screenY, pointer, button);

                Vector3 worldCoordinates = GameScreen.this.camera.unproject(new Vector3(screenX, screenY, 0));

                Vector2 vec = GameUtils.cartesianToIsometric(new Vector2(worldCoordinates.x, worldCoordinates.y));

                System.out.println(String.format(
                        "%s, %s -> %s, %s",
                        vec.x / 33f,
                        vec.y / 22f,
                        screenX,
                        screenY
                ));

                return true;
            }
        });
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
        this.tiledMapRenderer.render();

        this.tiledMapRenderer.getBatch().begin();
        this.player.draw(this.tiledMapRenderer.getBatch());

        /*String s = ((ClientGameNet) application.getNet()).serverConnection.getTestString();
        if(s != null) {
            this.font.draw(this.tiledMapRenderer.getBatch(), s, 10.0f, 10.0f);
        }*/

        this.tiledMapRenderer.getBatch().end();
    }

    @Override
    public void dispose() {

    }
}
