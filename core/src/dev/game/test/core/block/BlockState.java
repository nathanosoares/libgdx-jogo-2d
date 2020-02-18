package dev.game.test.core.block;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.game.test.api.block.IBlockState;
import dev.game.test.core.world.World;
import dev.game.test.core.world.WorldLayer;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BlockState implements IBlockState {

    @Setter
    private Block block;

    private World world;

    private WorldLayer layer;

    private Vector2 position;

    public int connectedData;

    public BlockState(Block block, World world, WorldLayer layer, Vector2 position) {
        this.block = block;
        this.world = world;
        this.layer = layer;
        this.position = position;
    }
}
