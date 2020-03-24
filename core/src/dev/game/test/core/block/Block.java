package dev.game.test.core.block;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.states.BlockState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(of = {"id"})
@RequiredArgsConstructor
public abstract class Block implements IBlock {

    @Getter
    protected final int id;

    @Getter
    protected float width = 1f;

    @Getter
    protected float height = 1f;

    @Override
    public IBlockState createState(IWorld world, IWorldLayer layer, int x, int y) {
        return new BlockState(this, world, layer, new GridPoint2(x, y));
    }

    public void loadTextures() {
    }

    public TextureRegion getTexture(IBlockState state) {
        return null;
    }

    public void neighbourUpdate(IBlockState blockState) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockState targetData = (BlockState) blockState.getLayer().getFacingBlock(blockState, facing);

            if (targetData != null) {
                targetData.getBlock().onBlockNeighbourUpdate(targetData, facing.getOpposite());
            }
        }
    }

    public void onBlockNeighbourUpdate(IBlockState blockState, EnumFacing neighbourFacing) {

    }


}
