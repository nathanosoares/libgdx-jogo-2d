package dev.game.test.core.block.connected;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.CoreConstants;
import dev.game.test.core.block.Block;

public class ConnectedTextureSimple implements ConnectedTexture, Disposable {

    private final Block block;

    private FileHandle imageHandle;

    private Texture atlasTexture;

    private TextureRegion atlasRegion;

    public ConnectedTextureSimple(Block block, FileHandle imageHandle) {
        this.block = block;
        this.imageHandle = imageHandle;

        loadTextures();
    }

    private void loadTextures() {
        Pixmap pixmapRaw = new Pixmap(imageHandle);
        Pixmap pixmapOut = new Pixmap(CoreConstants.TILE_SIZE, CoreConstants.TILE_SIZE * 0xFF, Pixmap.Format.RGBA8888);

        for (int i = 0; i < 0xFF; i++) {
            for (int x = 0; x < CoreConstants.TILE_SIZE; x++) {
                for (int y = 0; y < CoreConstants.TILE_SIZE; y++) {
                    pixmapOut.setColor(pixmapRaw.getPixel(x, y));

                    for (TextureConnection textureConnection : CONNECTIONS) {
                        boolean shouldRender = (i & textureConnection.getValue()) != 0;

                        if (shouldRender && textureConnection.getDrawBounds().contains(x, y)) {
                            int color = pixmapRaw.getPixel((int) textureConnection.getUvOffset().x + x, (int) textureConnection.getUvOffset().y + y);

                            if (color > 0) {
                                pixmapOut.setColor(color);
                            }
                        }
                    }

                    pixmapOut.drawPixel(x, y + i * CoreConstants.TILE_SIZE);
                }
            }
        }

        this.atlasRegion = new TextureRegion(this.atlasTexture = new Texture(pixmapOut));
    }

    /*
        Public
     */

    public TextureRegion getTexture(IBlockState blockState) {
        int code = blockState.getConnectedData();
        atlasRegion.setRegion(0, code * CoreConstants.TILE_SIZE, CoreConstants.TILE_SIZE, CoreConstants.TILE_SIZE);
        return atlasRegion;
    }

    /*
        Connected Texture
     */

    @Override
    public boolean shouldRender(IBlockState blockState, TextureConnection facing) {
        if (facing instanceof SideTextureConnection) {
            SideTextureConnection textureConnection = (SideTextureConnection) facing;

            IBlockState sideBlock = blockState.getLayer().getFacingBlock(blockState, textureConnection.getSide());

            return sideBlock == null || !blockState.getBlock().equals(sideBlock.getBlock());
        }

        if (facing instanceof CornerTextureConnection) {
            CornerTextureConnection textureConnection = (CornerTextureConnection) facing;

            for (EnumFacing facing1 : textureConnection.getSides()) {
                IBlockState sideBlock = blockState.getLayer().getFacingBlock(blockState, facing1);

                if (sideBlock == null || !blockState.getBlock().equals(sideBlock.getBlock())) {
                    return false;
                }
            }

            IBlockState sideBlock = blockState.getLayer().getFacingBlock(blockState, textureConnection.getSides());

            return sideBlock == null || !blockState.getBlock().equals(sideBlock.getBlock());
        }

        return false;
    }

    @Override
    public void updateNeighbours(IBlockState blockState) {
        this.block.neighbourUpdate(blockState);
    }

    /*
        Disposable
     */

    @Override
    public void dispose() {
        this.atlasTexture.dispose();
    }
}
