package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.block.states.PhysicBlockState;

public class BlockGrassPlant extends Block {

    private TextureRegion texture;

    public BlockGrassPlant() {
        super(7);
    }


    @Override
    public void loadTextures() {
        this.texture = new TextureRegion(new Texture(Gdx.files.internal("grass.png")));
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture;
    }

    @Override
    public IBlockState createState(IWorld world, IWorldLayer layer, int x, int y) {
        return new PhysicBlockState(this, world, layer, new GridPoint2(x, y)) {

            {
                bodyOffset.set(getWidth() / 2, getHeight() / 2);
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
//                fixtureDef.isSensor = true;

                Fixture fixture = body.createFixture(fixtureDef);
                fixture.setUserData(this);

                shape.dispose();
            }

            @Override
            public void onHit(IEntity entity) {
                this.layer.setBlockState(Blocks.AIR.createState(world, layer, position.x, position.y));
                System.out.println("hit grass");
            }
        };
    }

    @Override
    public float getHeight() {
        return .5f;
    }

    @Override
    public float getWidth() {
        return .5f;
    }
}