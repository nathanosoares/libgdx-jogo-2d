package dev.game.test.core.world;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.BlockState;
import lombok.Getter;

public class WorldLayer implements IWorldLayer {

    @Getter
    protected BlockState[][] states;

    public WorldLayer(World world) {
        this.states = new BlockState[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];
    }

    public BlockState getBlockState(float x, float y) {
        return getBlockState((int) x, (int) y);
    }

    public BlockState getBlockState(int x, int y) {
        if (x < 0 || y < 0 || this.states.length <= x || this.states[x].length <= y) {
            return null;
        }

        return states[x][y];
    }

    public BlockState getFacingBlock(IBlockState blockState, EnumFacing... facings) {
        Vector2 position = blockState.getPosition();
        int x = (int) position.x;
        int y = (int) position.y;

        for (EnumFacing facing1 : facings) {
            x += facing1.getOffset().x;
            y += facing1.getOffset().y;
        }

        return getBlockState(x, y);
    }

    public void setBlockState(BlockState blockState) {
        for (int additionalX = 0; additionalX < blockState.getBlock().getWidth(); additionalX++) {
            for (int additionalY = 0; additionalY < blockState.getBlock().getHeight(); additionalY++) {

                int x = (int) blockState.getPosition().x + additionalX;
                int y = (int) blockState.getPosition().y + additionalY;
                this.states[x][y] = blockState;
            }
        }
    }

    public boolean isOrigin(int x, int y) {
        BlockState block = getBlockState(x, y);

        return block != null && (block.getPosition().x == x && block.getPosition().y == y);
    }
}
