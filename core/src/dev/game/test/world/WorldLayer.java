package dev.game.test.world;

import dev.game.test.world.block.BlockData;

public class WorldLayer {

    private BlockData[][] blocks;

    public void setBlock(int x, int y, BlockData blockData) {
        for (int additionalX = 0; additionalX < blockData.getWidth(); additionalX++) {
            for (int additionalY = 0; additionalY < blockData.getHeight(); additionalY++) {

                this.blocks[x + additionalX][y + additionalY] = blockData;

            }
        }
    }
}
