package dev.game.test.world.block.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.world.block.Block;
import dev.game.test.world.block.BlockState;

public class BlockGrass extends Block {

    private TextureRegion texture;

    public BlockGrass() {

    }

    @Override
    public void loadTextures() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.valueOf("6df7b1"));
        pixmap.fillRectangle(0,0 , 16, 16);
        this.texture = new TextureRegion(new Texture(pixmap));
    }

    @Override
    public TextureRegion getTexture(BlockState blockState) {
        return texture;
    }
}