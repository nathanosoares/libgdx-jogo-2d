package dev.game.test.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {

    public Player() {
        super(new Texture(Gdx.files.internal("rpg-pack/chars/gabe/gabe-idle-run.png")));
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(
                new TextureRegion(this.texture, 0, 0, 24, 24),
                position.x, position.y, 24 / 10f, 24 / 10f
        );
    }

    @Override
    public boolean hasCollision() {
        return true;
    }
}
