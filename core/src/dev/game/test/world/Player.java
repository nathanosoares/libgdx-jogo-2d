package dev.game.test.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.GameUtils;
import lombok.Getter;
import lombok.Setter;

public class Player {

    private Texture texture;

    private float elapsedTime = 0f;

    @Getter
    @Setter
    private Vector2 location = new Vector2(0, 0);

    public Player() {
        this.texture = new Texture(
                Gdx.files.internal("man/man.png")
        );

    }

    public void draw(Batch batch) {
        Vector2 newLocation = GameUtils.cartesianToIsometric(location);

        elapsedTime += Gdx.graphics.getDeltaTime();

        batch.draw(
                new TextureRegion(this.texture, 0, 0, 32, 48),
                newLocation.x, newLocation.y, 32 * 2, 48 * 2
        );
    }
}
