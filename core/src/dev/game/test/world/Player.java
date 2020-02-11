package dev.game.test.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import lombok.Setter;

public class Player extends Sprite {

    private TextureAtlas idleAtlas;
    private Animation<TextureAtlas.AtlasRegion> idleAnimation;

    private float elapsedTime = 0f;

    @Getter
    @Setter
    private Vector3 location = new Vector3(0, 0, 0);

    public Player() {
        this.idleAtlas = new TextureAtlas(Gdx.files.internal("spritessheets/flatboy/idle/idle.atlas"));
        this.idleAnimation = new Animation<>(1f / 30f, this.idleAtlas.getRegions());
    }

    @Override
    public void draw(Batch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();

        batch.draw(idleAnimation.getKeyFrame(elapsedTime, true), location.x, location.z);
    }
}
