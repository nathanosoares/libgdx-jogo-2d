package dev.game.test.core.block;

import dev.game.test.api.block.IBlock;
import dev.game.test.api.util.EnumFacing;
import lombok.Getter;
import lombok.Setter;

public class Block implements IBlock {

    @Getter
    @Setter
    protected int id;

    @Getter
    protected int width = 1;

    @Getter
    protected int height = 1;

    public void neighbourUpdate(BlockState blockState) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockState targetData = blockState.getLayer().getFacingBlock(blockState, facing);

            if (targetData != null) {
                targetData.getBlock().onBlockNeighbourUpdate(targetData, facing.getOpposite());
            }
        }
    }

    public void onBlockNeighbourUpdate(BlockState blockState, EnumFacing neighbourFacing) {

    }
}
