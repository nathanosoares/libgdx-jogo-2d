package dev.game.test.core.block.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.block.IBlockState;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.BlockState;
import lombok.Getter;

public class BlockGrass extends Block {

    private TextureRegion texture;

    public BlockGrass() {
        super(3);
    }

    @Override
    public void loadTextures() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.valueOf("6df7b1"));
        pixmap.fillRectangle(0, 0, 16, 16);
        this.texture = new TextureRegion(new Texture(pixmap));
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture;
    }
}
