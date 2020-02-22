package dev.game.test.core.block.connected;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface ConnectedTexture {

    TextureRegion getTexture(IBlockState blockState);

    boolean shouldRender(IBlockState blockState, TextureConnection facing);

    void updateNeighbours(IBlockState blockState);

    default int computeTextures(IBlockState blockState) {
        int old = blockState.getConnectedData();

        int data = 0;

        for (TextureConnection connection : CONNECTIONS) {
            if (shouldRender(blockState, connection)) {
                data += connection.value;
            }
        }

        blockState.setConnectedData(data);

        if (data != old) {
            updateNeighbours(blockState);
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
