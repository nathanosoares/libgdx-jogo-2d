package dev.game.test.core.block.states;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import lombok.Getter;

@Getter
public class SolidBlockState extends PhysicBlockState {

    public SolidBlockState(IBlock block, IWorld world, IWorldLayer layer, GridPoint2 position) {
        super(block, world, layer, position);
    }

    @Override
    public void createPhysics(World box2dWorld) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(
                position.x + block.getWidth() / 2f + bodyOffset.x,
                position.y + block.getHeight() / 2f + bodyOffset.y
        );

        bodyDef.fixedRotation = false;

        body = box2dWorld.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(block.getWidth() / 2f, block.getHeight() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        shape.dispose();
    }

    @Override
    public void onHit(IEntity entity) {

    }
}
