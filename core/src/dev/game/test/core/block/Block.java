package dev.game.test.core.block;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.block.IBlock;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(of = {"id"})
public class Block implements IBlock {

    @Getter
    @Setter
    protected int id;

    @Getter
    protected int width = 1;

    @Getter
    protected int height = 1;

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
