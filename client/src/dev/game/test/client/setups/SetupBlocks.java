package dev.game.test.client.setups;

import dev.game.test.api.IGame;
import dev.game.test.client.registry.BlockRegistry;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.setup.Setup;
import lombok.RequiredArgsConstructor;

@Deprecated
@RequiredArgsConstructor
public class SetupBlocks implements Setup {

    private final IGame game;

    @Override
    public void setup() {
        BlockRegistry blockRegistry = game.getRegistryManager().getRegistry(Block.class);

        blockRegistry.registerBlock(0, Blocks.AIR);
        blockRegistry.registerBlock(1, Blocks.DIRT);
        blockRegistry.registerBlock(2, Blocks.STONE);
        blockRegistry.registerBlock(3, Blocks.WATER);
        blockRegistry.registerBlock(4, Blocks.GRASS);
        blockRegistry.registerBlock(5, Blocks.REINFORCED_DIRT);
        blockRegistry.registerBlock(6, Blocks.GRASS_PLANT);
    }
}
