package dev.game.test.world.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.screens.GameScreen;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public abstract class Entity {

    protected final Texture texture;

    @Setter
    protected Vector2 position;

    public abstract void draw(Batch batch);

    public abstract boolean hasCollision();

    public void move(Vector2 vector2) {
        vector2.y = Math.max(Math.min(this.position.y + vector2.y, GameScreen.getInstance().getWorld().getHeight() - 24 / 10f), 0);
        vector2.x = Math.max(Math.min(this.position.x + vector2.x, GameScreen.getInstance().getWorld().getWidth() - 24 / 10f), 0);

        this.position = vector2;
    }
}
