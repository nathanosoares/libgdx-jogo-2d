package dev.game.test.client.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import dev.game.test.api.IClientGame;
import dev.game.test.core.CoreConstants;

public class GameHudSystem extends EntitySystem {

    private IClientGame game;
    private SpriteBatch batch;

    public ExtendViewport viewport;
    public Stage stage;

    public Table table;

    private float timeCount;

    public GameHudSystem(IClientGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;

        this.viewport = new ExtendViewport(
                CoreConstants.V_WIDTH / CoreConstants.PPM,
                CoreConstants.V_HEIGHT / CoreConstants.PPM,
                new OrthographicCamera()
        );

        this.stage = new Stage(this.viewport, batch);

        this.table = new Table();
        this.table.center();
        this.table.top();
        this.table.setFillParent(true);

        BitmapFont font = new BitmapFont();

        font.getData().setScale(1 / 32f);
        font.setUseIntegerPositions(false);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        this.table.add(new Label("SCORE:", labelStyle));

        this.stage.addActor(this.table);
    }

    @Override
    public void update(float deltaTime) {
        this.stage.draw();
    }

    public void resize(int width, int height) {
        this.stage.getViewport().setScreenSize(width, height);
    }
}
