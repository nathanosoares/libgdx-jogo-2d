package dev.game.test.client.setups;

import com.badlogic.gdx.Gdx;
import dev.game.test.client.block.BlockClient;
import dev.game.test.client.block.Blocks;
import dev.game.test.client.registry.RegistryBlocks;
import dev.game.test.core.registry.RegistryManager;
import dev.game.test.core.setup.Setup;

import javax.inject.Inject;

public class SetupBlocks implements Setup {

    @Inject
    protected RegistryManager registryManager;

    @Override
    public void setup() {
        Gdx.app.debug("registryManager ", String.valueOf(registryManager));
        RegistryBlocks registryBlocks = registryManager.getRegistry(BlockClient.class);

        registryBlocks.registerBlock(0, Blocks.AIR);
        registryBlocks.registerBlock(1, Blocks.DIRT);
        registryBlocks.registerBlock(2, Blocks.STONE);
        registryBlocks.registerBlock(3, Blocks.WATER);
        registryBlocks.registerBlock(4, Blocks.GRASS);
        registryBlocks.registerBlock(5, Blocks.REINFORCED_DIRT);
    }
}
