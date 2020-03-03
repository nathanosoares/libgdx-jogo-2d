package dev.game.test.client.registry;

import com.google.common.collect.Maps;
import dev.game.test.api.registry.IRegistry;
import dev.game.test.core.block.Block;

import java.util.Map;

@Deprecated
public class RegistryBlocks implements IRegistry<Block> {

    private final Map<Integer, Block> blockId = Maps.newHashMap();

    public Block getBlock(int id) {
        return this.blockId.get(id);
    }

    public void registerBlock(int id, Block block) {
        block.loadTextures();

        blockId.put(block.getId(), block);
    }
}
