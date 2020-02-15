package dev.game.test.world.block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;
import dev.game.test.world.block.impl.BlockStone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BlockState {

    private Block block;

    private World world;
    private WorldLayer layer;

    private Vector2 position;

    public int connectedData;

    public BlockState(Block block, World world, WorldLayer layer, Vector2 position) {
        this.world = world;
        this.layer = layer;
        this.position = position;

        this.setBlock(block);
    }

    Body body;

    public void setBlock(Block block) {
        this.block = block;

        if (block instanceof BlockStone) {
            if (this.body != null) {
                world.getBox2dWorld().destroyBody(this.body);
            }

            BodyDef def = new BodyDef();
            def.position.set(this.position);
            this.body = world.getBox2dWorld().createBody(def);

            PolygonShape groundBox = new PolygonShape();


            Vector2 center = new Vector2(this.block.getWidth() / 2f, this.block.getHeight() / 2f);
            groundBox.setAsBox(this.block.getWidth() / 2f, this.block.getHeight() / 2f, center, 0f);

            this.body.createFixture(groundBox, 0f);

            groundBox.dispose();
        }
    }
}
