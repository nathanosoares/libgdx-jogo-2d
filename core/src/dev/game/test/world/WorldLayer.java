package dev.game.test.world;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.block.BlockAir;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.block.EnumFacing;

public class WorldLayer {

    private final BlockData airBlock;

    //

    protected BlockData[][] blocks;

    public WorldLayer(World world) {
        this.blocks = new BlockData[(int) world.getBounds().getWidth()][(int) world.getBounds().getHeight()];

        this.airBlock = new BlockData(new BlockAir(), 1, 1, world, this, new Vector2(0.0f, 0.0f));
    }

    public BlockData getBlock(float x, float y) {
        return getBlock((int) x, (int) y);
    }

    public BlockData getBlock(int x, int y) {
        if (x < 0 || y < 0 || this.blocks.length <= x || this.blocks[x].length <= y) {
            airBlock.getPosition().set(x, y);
            return airBlock;
        }

        return blocks[x][y];
    }

    public BlockData getFacingBlock(BlockData blockData, EnumFacing... facings) {
        Vector2 position = blockData.getPosition();
        int x = (int) position.x;
        int y = (int) position.y;

        for (EnumFacing facing1 : facings) {
            x += facing1.getOffset().x;
            y += facing1.getOffset().y;
        }

        return getBlock(x, y);
    }

    public void setBlock(int x, int y, BlockData blockData) {
        for (int additionalX = 0; additionalX < blockData.getWidth(); additionalX++) {
            for (int additionalY = 0; additionalY < blockData.getHeight(); additionalY++) {

                this.blocks[x + additionalX][y + additionalY] = blockData;
            }
        }
    }

    public boolean isOrigin(int x, int y) {
        BlockData block = getBlock(x, y);

        return block != null && (block.getPosition().x == x && block.getPosition().y == y);
    }
}
