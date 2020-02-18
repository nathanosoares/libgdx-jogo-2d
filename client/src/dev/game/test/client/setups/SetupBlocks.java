package dev.game.test.client.setups;

import dev.game.test.client.GameApplication;
import dev.game.test.client.block.Blocks;
import dev.game.test.client.block.BlocksRegistry;

public class SetupBlocks extends Setup {

    @Override
    void setup(GameApplication application) {
        application.setBlocksRegistry(new BlocksRegistry());

        application.getBlocksRegistry().registerBlock(0, Blocks.AIR);
        application.getBlocksRegistry().registerBlock(1, Blocks.DIRT);
        application.getBlocksRegistry().registerBlock(2, Blocks.STONE);
        application.getBlocksRegistry().registerBlock(3, Blocks.WATER);
        application.getBlocksRegistry().registerBlock(4, Blocks.GRASS);
    }
}
