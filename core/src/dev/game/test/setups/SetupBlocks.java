package dev.game.test.setups;

import dev.game.test.GameApplication;
import dev.game.test.world.block.Blocks;
import dev.game.test.world.block.BlocksRegistry;

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
