package dev.game.test.client.setups;

import dev.game.test.api.IGame;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.Blocks;
import dev.game.test.core.setup.Setup;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetupBlocks implements Setup {

    private final IGame game;

    @Override
    public void setup() {
        RegistryBlocks registryBlocks = game.getRegistryManager().getRegistry(Block.class);

        registryBlocks.registerBlock(0, Blocks.AIR);
        registryBlocks.registerBlock(1, Blocks.DIRT);
        registryBlocks.registerBlock(2, Blocks.STONE);
        registryBlocks.registerBlock(3, Blocks.WATER);
        registryBlocks.registerBlock(4, Blocks.GRASS);
        registryBlocks.registerBlock(5, Blocks.REINFORCED_DIRT);
    }
}
