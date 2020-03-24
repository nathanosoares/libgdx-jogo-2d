package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.connected.ConnectedTextureSimple;
import dev.game.test.core.block.states.SolidBlockState;

public class BlockWater extends Block {

    private ConnectedTextureSimple texture;

    public BlockWater() {
        super(6);
    }

//    @Override
//    public IBlockState createState(IWorld world, IWorldLayer layer, int x, int y) {
//        return new SolidBlockState(this, world, layer, new Vector2(x, y)) {
//            @Override
//            public void createPhysics(World box2dWorld) {
//                BodyDef bodyDef = new BodyDef();
//                bodyDef.type = BodyDef.BodyType.StaticBody;
//                bodyDef.position.set(position.x + block.getWidth() / 2f, position.y + block.getHeight() / 2f);
//                bodyDef.fixedRotation = false;
//
//                body = box2dWorld.createBody(bodyDef);
//
//                PolygonShape shape = new PolygonShape();
//                shape.setAsBox(block.getWidth() / 2f, block.getHeight() / 2f);
//
//                FixtureDef fixtureDef = new FixtureDef();
//                fixtureDef.shape = shape;
//                fixtureDef.density = 0;
//                fixtureDef.restitution = .5f;
//                fixtureDef.friction = 0.9f;
//
//                Fixture fixture = body.createFixture(fixtureDef);
//                fixture.setUserData(this);
//
//                shape.dispose();
//            }
//        };
//    }


    @Override
    public void loadTextures() {
        this.texture = new ConnectedTextureSimple(this, Gdx.files.internal("map/water.png"));
    }

    @Override
    public void onBlockNeighbourUpdate(IBlockState blockState, EnumFacing neighbourFacing) {
        super.onBlockNeighbourUpdate(blockState, neighbourFacing);
        this.texture.computeTextures(blockState);
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture.getTexture(blockState);
    }
}
