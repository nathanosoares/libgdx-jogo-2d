package dev.game.test.core.block.states;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.world.World;
import dev.game.test.core.world.WorldLayer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(of ={"block", "position", "connectedData"})
public class BlockState implements IBlockState {

    @Setter
    protected IBlock block;

    protected IWorld world;

    protected IWorldLayer layer;

    protected Vector2 position;

    @Setter
    private int connectedData;

    public BlockState(IBlock block, IWorld world, IWorldLayer layer, Vector2 position) {
        this.block = block;
        this.world = world;
        this.layer = layer;
        this.position = position;
    }
}
