package dev.game.test.world.block;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Block {

    private static TextureRegion MISSING_TEXTURE;

    static {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        pixmap.drawRectangle(0, 0, 16, 16);
        pixmap.setColor(1.0f, 0.0f, 1.0f, 1.0f);
        pixmap.drawRectangle(0, 0, 8, 8);
        pixmap.drawRectangle(8, 8, 8, 8);

        MISSING_TEXTURE = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
    }

    /*

     */

    public TextureRegion getTexture(int x, int y) {
        return MISSING_TEXTURE;
    }

}
