package dev.game.test.client.block;

import com.google.common.collect.Maps;

import java.util.Map;

public class BlocksRegistry {

    private final Map<Integer, BlockClient> MAP = Maps.newHashMap();

    public void registerBlock(int id, BlockClient block) {
        block.setId(id);
        block.loadTextures();

        MAP.put(id, block);
    }
}
