package dev.game.test.client.block;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;

public class BlockClient extends Block {
    private static TextureRegion MISSING_TEXTURE;

    static {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        pixmap.fillRectangle(0, 0, 16, 16);
        pixmap.setColor(1.0f, 0.0f, 1.0f, 1.0f);
        pixmap.fillRectangle(1, 1, 14, 14);

        MISSING_TEXTURE = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
    }

    public void loadTextures() {

    }

    public TextureRegion getTexture(BlockState blockState) {
        return MISSING_TEXTURE;
    }
}
