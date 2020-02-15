package dev.game.test.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

public class Player extends Entity {

    private Texture texture;

    @Setter
    @Getter
    private Vector2 location = new Vector2(0, 0);

    public Player() {
        this.texture = new Texture(
                Gdx.files.internal("rpg-pack/chars/gabe/gabe-idle-run.png")
        );
    }

    public void draw(Batch batch) {
        batch.draw(
                new TextureRegion(this.texture, 0, 0, 24, 24),
                location.x, location.y, 24 / 10f, 24 / 10f
        );
    }
}
