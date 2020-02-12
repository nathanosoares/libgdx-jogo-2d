package dev.game.test.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.game.test.GameApplication;
import dev.game.test.GameUtils;
import dev.game.test.net.GameServerConnection;
import dev.game.test.net.client.ClientGameNet;
import dev.game.test.world.Player;
import java.util.Optional;
import lombok.Getter;

public class GameScreen extends ScreenAdapter {

    //

    public static final int MODEL_LENGTH = 20;

    public static final int MAP_LENGTH = 10;

    //

    public static final int WIDTH = 320 * 4;
    public static final int HEIGHT = 180 * 4;

    public static final Vector3 CAMERA_OFFSET = new Vector3(-1.0f, 2.0f, -1.0f);

    private final GameApplication application;

    @Getter
    private PerspectiveCamera camera;

    private CameraInputController inputController;

    //

    private BitmapFont font;

    private SpriteBatch spriteBatch;

    private ModelBatch modelBatch;

    //

    private Player player;

    private Model playerModel;
    private ModelInstance playerModelInstance;

    //

    private Environment environment;
    private Model[] blockModels = new Model[MODEL_LENGTH];
    private ModelInstance[][] mapBlockInstances = new ModelInstance[MAP_LENGTH][MAP_LENGTH];

    //

    private Texture clickedTextureSprite = new Texture(Gdx.files.internal("tile_clicked.png"));
    private Texture textureSprite = new Texture(Gdx.files.internal("tile.png"));

    private Cell lastCellHover = null;

    private int fps;

    public GameScreen(GameApplication application) {
        this.application = application;

        this.font = new BitmapFont();
        this.spriteBatch = new SpriteBatch();
        this.modelBatch = new ModelBatch();
        this.player = new Player();
    }

    @Override
    public void show() {
        this.camera = new PerspectiveCamera(67.0f, WIDTH, HEIGHT);

        this.playerModel = new ModelBuilder().createCapsule(0.3f, 1.0f, 10, new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            Usage.Position | Usage.Normal);
        this.playerModelInstance = new ModelInstance(this.playerModel);

        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        this.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        for (int i = 0; i < MODEL_LENGTH; i++) {
            this.blockModels[i] = new ModelBuilder().createBox(1.0f, 1.0f, 1.0f,
                new Material(ColorAttribute.createDiffuse((float) Math.random(), (float) Math.random(), (float) Math.random(), 1.0f)),
                Usage.Position | Usage.Normal);
        }

        for (int x = 0; x < MAP_LENGTH; x++) {
            for (int y = 0; y < MAP_LENGTH; y++) {
                ModelInstance blockInstance = new ModelInstance(this.blockModels[(int) (Math.random() * MODEL_LENGTH - 1)]);
                blockInstance.transform.setToTranslation(x, 0, y);

                mapBlockInstances[x][y] = blockInstance;
            }
        }

        inputController = new CameraInputController(this.camera);
        inputController.forwardKey = inputController.backwardKey = inputController.rotateLeftKey = inputController.rotateRightKey = -1;
        //Gdx.input.setInputProcessor(inputController);
    }

    @Override
    public void render(float delta) {
        Vector3 to = new Vector3();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            to.x += 2.5f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            to.x -= 2.5f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            to.z += 2.5f * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            to.z -= 2.5f * delta;
        }

        this.player.move(to);
        this.playerModelInstance.transform.setToTranslation(this.player.getLocation());

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        GameUtils.clearScreen(0, 0, 0, 100);

        this.camera.position.set(new Vector3(this.player.getLocation()).add(CAMERA_OFFSET));
        this.camera.lookAt(new Vector3(this.player.getLocation()).add(1.0f, 0.0f, 1.0f));

        this.camera.update();
        this.inputController.update();

        this.modelBatch.begin(this.camera);

        this.modelBatch.render(this.playerModelInstance, this.environment);

        for (int x = 0; x < MAP_LENGTH; x++) {
            for (int y = 0; y < MAP_LENGTH; y++) {
                this.modelBatch.render(this.mapBlockInstances[x][y], this.environment);
            }
        }

        this.modelBatch.end();

        this.spriteBatch.begin();
        String s = Optional.ofNullable(((ClientGameNet) application.getNet()).serverConnection).map(GameServerConnection::getTestString)
            .orElse(null);
        if (s != null) {
            this.font.draw(this.spriteBatch, s, 10.0f, 20.0f);
        }

        this.font.draw(this.spriteBatch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()), 10.0f,  50.0f);

        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
        for(int i = 0; i < MODEL_LENGTH; i++) {
            this.blockModels[i].dispose();
        }
    }
}
