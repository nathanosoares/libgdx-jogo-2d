package dev.game.test.core.block.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.block.IBlockState;
import dev.game.test.core.CoreConstants;
import dev.game.test.core.block.Block;

public class BlockGrass extends Block {

    private TextureRegion texture;

    public BlockGrass() {
        super(3);
    }

    @Override
    public void loadTextures() {
        Pixmap pixmap = new Pixmap(CoreConstants.TILE_SIZE, CoreConstants.TILE_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.valueOf("6df7b1"));
        pixmap.fillRectangle(0, 0, CoreConstants.TILE_SIZE, CoreConstants.TILE_SIZE);
        this.texture = new TextureRegion(new Texture(pixmap));
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture;
    }
}
