package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.block.IBlockState;
import dev.game.test.core.block.Block;

public class BlockStone extends Block {

    private TextureRegion texture;

    public BlockStone() {
        super(1);
    }

    @Override
    public void loadTextures() {
        this.texture = new TextureRegion(new Texture(Gdx.files.internal("map/stone.png")));
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture;
    }
}
