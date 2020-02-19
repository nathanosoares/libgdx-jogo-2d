package dev.game.test.client.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.client.world.WorldClient;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public abstract class Entity implements IEntity {

    @Getter
    private final UUID id;

    protected final Texture texture;

    @Setter
    protected IWorld world;

    public final Vector2 position = new Vector2(0, 0);

//    public Body body;

    protected float walkSpeed = 5;

    public abstract void draw(Batch batch);

    public abstract boolean hasCollision();

    public abstract boolean canLeaveMap();

    public void move(Vector2 vector2) {

//        if (!this.canLeaveMap()) {
//            vector2.y = Math.max(Math.min(vector2.y, this.world.getBounds().getHeight() - 24 / 10f), 0);
//            vector2.x = Math.max(Math.min(vector2.x, this.world.getBounds().getWidth() - 24 / 10f), 0);
//        }

//        this.body.setLinearVelocity(vector2);
        this.position.add(vector2);
//        this.body.setTransform(this.position, this.body.getAngle());
    }

    public float getSpeed() {
        if (this instanceof EntityRunner && ((EntityRunner) this).isRunning()) {
            return ((EntityRunner) this).getRunningSpeed();
        }

        return this.walkSpeed;
    }

    //

    public void onWorldRemove(WorldClient worldClient) {
//        if (this.body != null) {
//            worldClient.getBox2dWorld().destroyBody(this.body);
//        }
    }

    public void onWorldAdd(WorldClient worldClient, float x, float y) {
//        BodyDef def = new BodyDef();
//        def.type = BodyDef.BodyType.KinematicBody;
//        def.position.set(x, y);
//
//        this.body = worldClient.getBox2dWorld().createBody(def);
//
//        PolygonShape groundBox = new PolygonShape();
//
//        Vector2 center = new Vector2(24 / 16f / 2f, 24 / 16f / 2f);
//        groundBox.setAsBox(24 / 16f / 2.0f, 24 / 16f / 2.0f, center, 0.0f);
//
//        this.body.createFixture(groundBox, 0f);
//
//        groundBox.dispose();
//        worldClient.getBox2dWorld().setContactListener(new ListenerClass());
    }

}
