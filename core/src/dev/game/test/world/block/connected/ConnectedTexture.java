package dev.game.test.world.block.connected;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.block.EnumFacing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface ConnectedTexture {

    TextureRegion getTexture(BlockData blockData);

    boolean shouldRender(BlockData blockData, TextureConnection facing);

    void updateNeighbours(BlockData blockData);

    default int computeTextures(BlockData blockData) {
        int old = blockData.connectedData;

        int data = 0;

        for (TextureConnection connection : CONNECTIONS) {
            if (shouldRender(blockData, connection)) {
                data += connection.value;
            }
        }

        blockData.connectedData = data;

        if (data != old) {
            updateNeighbours(blockData);
        }

        return data;
    }

    /*

     */

    static TextureConnection[] CONNECTIONS = new TextureConnection[]{
            new SideTextureConnection(1 << 7, new Rectangle(0, 0, 16, 8), new Vector2(0, 16), EnumFacing.NORTH),
            new SideTextureConnection(1 << 6, new Rectangle(0, 8, 16, 8), new Vector2(0, 16), EnumFacing.SOUTH),
            new SideTextureConnection(1 << 5, new Rectangle(0, 0, 8, 16), new Vector2(16, 0), EnumFacing.WEST),
            new SideTextureConnection(1 << 4, new Rectangle(8, 0, 8, 16), new Vector2(16, 0), EnumFacing.EAST),
            //
            new CornerTextureConnection(1 << 3, new Rectangle(0, 0, 8, 8), new Vector2(16, 16), EnumFacing.NORTH, EnumFacing.WEST),
            new CornerTextureConnection(1 << 2, new Rectangle(8, 0, 8, 8), new Vector2(16, 16), EnumFacing.NORTH, EnumFacing.EAST),
            new CornerTextureConnection(1 << 1, new Rectangle(0, 8, 8, 8), new Vector2(16, 16), EnumFacing.SOUTH, EnumFacing.WEST),
            new CornerTextureConnection(1 << 0, new Rectangle(8, 8, 8, 8), new Vector2(16, 16), EnumFacing.SOUTH, EnumFacing.EAST),
    };

    @RequiredArgsConstructor
    class TextureConnection {

        @Getter
        private final int value;

        @Getter
        private final Rectangle drawBounds;

        @Getter
        private final Vector2 uvOffset;

    }

    class SideTextureConnection extends TextureConnection {

        @Getter
        private EnumFacing side;

        public SideTextureConnection(int value, Rectangle drawBounds, Vector2 uvOffset, EnumFacing side) {
            super(value, drawBounds, uvOffset);

            this.side = side;
        }
    }

    class CornerTextureConnection extends TextureConnection {

        @Getter
        private EnumFacing[] sides;

        public CornerTextureConnection(int value, Rectangle drawBounds, Vector2 uvOffset, EnumFacing... sides) {
            super(value, drawBounds, uvOffset);

            this.sides = sides;
        }
    }

}
