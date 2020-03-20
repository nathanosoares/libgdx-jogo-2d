package dev.game.test.core.block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import lombok.Getter;

@Getter
public class BlockSolidState extends BlockState implements ISolidState {

    private Body body;

    public BlockSolidState(IBlock block, IWorld world, IWorldLayer layer, Vector2 position) {
        super(block, world, layer, position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x + block.getWidth() / 2f, position.y + block.getHeight() / 2f);
        bodyDef.fixedRotation = false;

        body = world.getBox2dWorld().createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(block.getWidth() / 2f, block.getHeight() / 2f);

        body.createFixture(polygonShape, 0.0f);

        polygonShape.dispose();
    }

}
