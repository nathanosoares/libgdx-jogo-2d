package dev.game.test.world.block.connected;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.world.block.BlockData;

public class ConnectedTexture {

    private TextureRegion textureRegion;

    public ConnectedTexture(FileHandle imageHandle) {
        Pixmap pixmapRaw = new Pixmap(imageHandle);

        Pixmap pixmapOut = new Pixmap(16, 16 * 0xFF, Pixmap.Format.RGBA8888);

        for (int i = 0; i < 0xFF; i++) {
            boolean north = (i & EnumTextureConnection.NORTH.getValue()) != 0;
            boolean south = (i & EnumTextureConnection.SOUTH.getValue()) != 0;
            boolean west = (i & EnumTextureConnection.WEST.getValue()) != 0;
            boolean east = (i & EnumTextureConnection.EAST.getValue()) != 0;
            boolean northwest = (i & EnumTextureConnection.NORTHWEST.getValue()) != 0;
            boolean northeast = (i & EnumTextureConnection.NORTHEAST.getValue()) != 0;
            boolean southwest = (i & EnumTextureConnection.SOUTHWEST.getValue()) != 0;
            boolean southeast = (i & EnumTextureConnection.SOUTHEAST.getValue()) != 0;

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    int color = pixmapRaw.getPixel(x, y);

                    if (northwest && y < 8 && x < 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x + 16, y + 16));
                    } else if (northeast && y < 8 && x > 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x + 16, y + 16));
                    } else if (southwest && y > 8 && x < 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x + 16, y + 16));
                    } else if (southeast && y > 8 && x > 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x + 16, y + 16));
                    }

                    if (north && y < 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x, y + 16));
                    } else if (south && y > 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x, y + 16));
                    }

                    if (west && x < 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x + 16, y));
                    } else if (east && x > 8) {
                        color = blendColor(color, pixmapRaw.getPixel(x + 16, y));
                    }

                    pixmapOut.setColor(color);

                    pixmapOut.drawPixel(x, y + i * 16);
                }
            }
        }

        this.textureRegion = new TextureRegion(new Texture(pixmapOut));
    }

    private int blendColor(int original, int color) {
        return color > 0 ? color : original;
    }

    public TextureRegion getTexture(BlockData blockData) {
        int code = 0;
        for (EnumTextureConnection connection : EnumTextureConnection.values()) {
            if (connection.shouldRender(blockData)) {
                code += connection.getValue();
            }
        }

        textureRegion.setRegion(0, code * 16, 16, 16);
        return textureRegion;
    }

}
