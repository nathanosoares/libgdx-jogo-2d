package dev.game.test.world.block;

import com.google.common.collect.Maps;

import java.util.Map;

public class BlocksRegistry {

    private final Map<Integer, Block> MAP = Maps.newHashMap();

    public void registerBlock(int id, Block block) {
        block.setId(id);
        block.loadTextures();

        MAP.put(id, block);
    }
}
