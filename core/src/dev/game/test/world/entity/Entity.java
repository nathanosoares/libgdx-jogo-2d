package dev.game.test.world.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.game.test.screens.GameScreen;
import dev.game.test.world.World;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class Entity {

    @Getter
    private final UUID id;

    protected final Texture texture;

    @Setter
    protected World world;

    public final Vector2 position = new Vector2(0, 0);

    protected float walkSpeed = 5;

    public abstract void draw(Batch batch);

    public abstract boolean hasCollision();

    public abstract boolean canLeaveMap();

    public Vector2 move(Vector2 vector2) {

        if (!this.canLeaveMap()) {
            vector2.y = Math.max(Math.min(this.position.y + vector2.y, this.world.getBounds().getHeight() - 24 / 10f), 0);
            vector2.x = Math.max(Math.min(this.position.x + vector2.x, this.world.getBounds().getWidth() - 24 / 10f), 0);
        } else {
            vector2.y = this.position.y + vector2.y;
            vector2.x = this.position.x + vector2.x;
        }

        this.position.set(vector2);
        this.body.setTransform(this.position, this.body.getAngle());

        return this.position.cpy();
    }

    public float getSpeed() {
        if (this instanceof EntityRunner && ((EntityRunner) this).isRunning()) {
            return ((EntityRunner) this).getRunningSpeed();
        }

        return this.walkSpeed;
    }

    //

    Body body;

    public void onWorldRemove(World world) {
        if (this.body != null) {
            world.getBox2dWorld().destroyBody(this.body);
        }
    }

    public void onWorldAdd(World world) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(this.position);
        this.body = world.getBox2dWorld().createBody(def);

        PolygonShape groundBox = new PolygonShape();

        Vector2 center = new Vector2(24 / 16f / 2f, 24 / 16f / 2f);
        groundBox.setAsBox(24 / 16f / 2.0f, 24 / 16f / 2.0f, center, 0.0f);

        this.body.createFixture(groundBox, 0f);

        groundBox.dispose();
    }
}
