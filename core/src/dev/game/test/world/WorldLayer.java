package dev.game.test.world;

import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.block.BlockAir;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.block.EnumFacing;

public class WorldLayer {

    private final BlockData airBlock;

    protected BlockData[][] blocks;

    public WorldLayer(World world) {
        this.blocks = new BlockData[world.getWidth()][world.getHeight()];

        this.airBlock = new BlockData(new BlockAir(), world, this, new Vector2(0.0f, 0.0f), 1, 1);
    }

    public BlockData getBlock(int x, int y) {
        if(x < 0 || y < 0) {
            return null;
        }

        if(this.blocks.length <= x || this.blocks[x].length <= y) {
            return airBlock;
        }

        return blocks[x][y];
    }

    public BlockData getFacingBlock(BlockData blockData, EnumFacing facing, EnumFacing... facings) {
        BlockData data = getBlock((int) (blockData.getPosition().x + facing.getOffset().x), (int) (blockData.getPosition().y + facing.getOffset().y));

        for(EnumFacing facing1 : facings) {
            data = getFacingBlock(data, facing1);
        }

        return data;
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
