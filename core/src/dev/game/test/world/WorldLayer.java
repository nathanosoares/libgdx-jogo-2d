package dev.game.test.world;

import dev.game.test.world.block.BlockData;
import lombok.Getter;

@Getter
public class WorldLayer {

    protected BlockData[][] blocks;

    public BlockData getBlock(int x, int y) {
        return blocks[x][y];
    }

    public void setBlock(int x, int y, BlockData blockData) {
        for (int additionalX = 0; additionalX < blockData.getWidth(); additionalX++) {
            for (int additionalY = 0; additionalY < blockData.getHeight(); additionalY++) {

                this.blocks[x + additionalX][y + additionalY] = blockData;
            }
        }
    }
}
