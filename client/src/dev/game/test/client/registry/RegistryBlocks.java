package dev.game.test.client.registry;

import com.google.common.collect.Maps;
import dev.game.test.client.block.BlockClient;
import dev.game.test.core.block.Block;
import dev.game.test.core.registry.Registry;
import java.util.Map;

public class RegistryBlocks implements Registry<BlockClient> {

    private final Map<Integer, Block> blockId = Maps.newHashMap();

    public Block getBlock(int id) {
        return this.blockId.get(id);
    }

    public void registerBlock(int id, BlockClient block) {
        block.setId(id);
        block.loadTextures();

        blockId.put(id, block);
    }
}
