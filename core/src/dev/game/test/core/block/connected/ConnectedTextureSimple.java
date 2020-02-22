package dev.game.test.core.block.connected;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;

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
        Pixmap pixmapOut = new Pixmap(16, 16 * 0xFF, Pixmap.Format.RGBA8888);

        for (int i = 0; i < 0xFF; i++) {
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
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

                    pixmapOut.drawPixel(x, y + i * 16);
                }
            }
        }

        this.atlasRegion = new TextureRegion(this.atlasTexture = new Texture(pixmapOut));
    }

    /*
        Public
     */

    public TextureRegion getTexture(BlockState blockState) {
        int code = blockState.getConnectedData();
        atlasRegion.setRegion(0, code * 16, 16, 16);
        return atlasRegion;
    }

    /*
        Connected Texture
     */

    @Override
    public boolean shouldRender(BlockState blockState, TextureConnection facing) {
        if (facing instanceof SideTextureConnection) {
            SideTextureConnection textureConnection = (SideTextureConnection) facing;
            BlockState sideBlock = blockState.getLayer().getFacingBlock(blockState, textureConnection.getSide());
            return sideBlock == null || !blockState.getBlock().equals(sideBlock.getBlock());
        }

        if (facing instanceof CornerTextureConnection) {
            CornerTextureConnection textureConnection = (CornerTextureConnection) facing;

            for (EnumFacing facing1 : textureConnection.getSides()) {
                BlockState sideBlock = blockState.getLayer().getFacingBlock(blockState, facing1);

                if (sideBlock == null || !blockState.getBlock().equals(sideBlock.getBlock())) {
                    return false;
                }
            }

            BlockState sideBlock = blockState.getLayer().getFacingBlock(blockState, textureConnection.getSides());
            return sideBlock == null || !blockState.getBlock().equals(sideBlock.getBlock());
        }

        return false;
    }

    @Override
    public void updateNeighbours(BlockState blockState) {
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
