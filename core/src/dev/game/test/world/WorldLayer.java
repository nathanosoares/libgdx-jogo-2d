package dev.game.test.world;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.block.impl.BlockAir;
import dev.game.test.world.block.BlockState;
import dev.game.test.world.block.EnumFacing;

public class WorldLayer {

    private final BlockState airBlock;

    //

    protected BlockState[][] blocks;

    public WorldLayer(World world) {
        this.blocks = new BlockState[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];

        this.airBlock = new BlockState(new BlockAir(), 1, 1, world, this, new Vector2(0.0f, 0.0f));
    }

    public BlockState getBlockState(float x, float y) {
        return getBlockState((int) x, (int) y);
    }

    public BlockState getBlockState(int x, int y) {
        if (x < 0 || y < 0 || this.blocks.length <= x || this.blocks[x].length <= y) {
            airBlock.getPosition().set(x, y);
            return airBlock;
        }

        return blocks[x][y];
    }

    public BlockState getFacingBlock(BlockState blockState, EnumFacing... facings) {
        Vector2 position = blockState.getPosition();
        int x = (int) position.x;
        int y = (int) position.y;

        for (EnumFacing facing1 : facings) {
            x += facing1.getOffset().x;
            y += facing1.getOffset().y;
        }

        return getBlockState(x, y);
    }

    public void setBlock(int x, int y, BlockState blockState) {
        for (int additionalX = 0; additionalX < blockState.getWidth(); additionalX++) {
            for (int additionalY = 0; additionalY < blockState.getHeight(); additionalY++) {

                this.blocks[x + additionalX][y + additionalY] = blockState;
            }
        }
    }

    public boolean isOrigin(int x, int y) {
        BlockState block = getBlockState(x, y);

        return block != null && (block.getPosition().x == x && block.getPosition().y == y);
    }
}
