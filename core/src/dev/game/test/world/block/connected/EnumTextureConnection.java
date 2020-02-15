package dev.game.test.world.block.connected;

import dev.game.test.world.block.BlockData;
import dev.game.test.world.block.EnumFacing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumTextureConnection {

    NORTH(1 << 7) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData north = block.getLayer().getFacingBlock(block, EnumFacing.NORTH);

            return north == null || !block.getBlock().equals(north.getBlock());
        }
    },
    SOUTH(1 << 6) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData south = block.getLayer().getFacingBlock(block, EnumFacing.SOUTH);

            return south == null || !block.getBlock().equals(south.getBlock());
        }
    },
    WEST(1 << 5) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData west = block.getLayer().getFacingBlock(block, EnumFacing.WEST);

            return west == null || !block.getBlock().equals(west.getBlock());
        }
    },
    EAST(1 << 4) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData east = block.getLayer().getFacingBlock(block, EnumFacing.EAST);

            return east == null || !block.getBlock().equals(east.getBlock());
        }
    },
    NORTHWEST(1 << 3) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData west = block.getLayer().getFacingBlock(block, EnumFacing.WEST);
            BlockData north = block.getLayer().getFacingBlock(block, EnumFacing.NORTH);
            BlockData northwest = block.getLayer().getFacingBlock(block, EnumFacing.NORTH, EnumFacing.WEST);

            return (west != null && block.getBlock().equals(west.getBlock())) &&
                    (north != null && block.getBlock().equals(north.getBlock())) &&
                    (northwest == null || !block.getBlock().equals(northwest.getBlock()));
        }
    },
    NORTHEAST(1 << 2) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData east = block.getLayer().getFacingBlock(block, EnumFacing.EAST);
            BlockData north = block.getLayer().getFacingBlock(block, EnumFacing.NORTH);
            BlockData northeast = block.getLayer().getFacingBlock(block, EnumFacing.NORTH, EnumFacing.EAST);

            return (east != null && block.getBlock().equals(east.getBlock())) &&
                    (north != null && block.getBlock().equals(north.getBlock())) &&
                    (northeast == null || !block.getBlock().equals(northeast.getBlock()));
        }
    },
    SOUTHWEST(1 << 1) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData west = block.getLayer().getFacingBlock(block, EnumFacing.WEST);
            BlockData south = block.getLayer().getFacingBlock(block, EnumFacing.SOUTH);
            BlockData southwest = block.getLayer().getFacingBlock(block, EnumFacing.SOUTH, EnumFacing.WEST);

            return (west != null && block.getBlock().equals(west.getBlock())) &&
                    (south != null && block.getBlock().equals(south.getBlock())) &&
                    (southwest == null || !block.getBlock().equals(southwest.getBlock()));
        }
    },
    SOUTHEAST(1 << 0) {
        @Override
        public boolean shouldRender(BlockData block) {
            BlockData east = block.getLayer().getFacingBlock(block, EnumFacing.EAST);
            BlockData south = block.getLayer().getFacingBlock(block, EnumFacing.SOUTH);
            BlockData southeast = block.getLayer().getFacingBlock(block, EnumFacing.SOUTH, EnumFacing.EAST);

            return (east != null && block.getBlock().equals(east.getBlock())) &&
                    (south != null && block.getBlock().equals(south.getBlock())) &&
                    (southeast == null || !block.getBlock().equals(southeast.getBlock()));
        }
    };

    @Getter
    private final int value;

    public abstract boolean shouldRender(BlockData block);

}
