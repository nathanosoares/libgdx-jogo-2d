package dev.game.test.core.block;

import com.google.common.collect.Maps;
import dev.game.test.api.block.IBlock;

import java.util.Map;

public class BlocksRegistry {

    private final Map<Integer, IBlock> blocks = Maps.newHashMap();

    public void registerBlock(int id, IBlock block) {
        block.setId(id);

        blocks.put(id, block);
    }
}
