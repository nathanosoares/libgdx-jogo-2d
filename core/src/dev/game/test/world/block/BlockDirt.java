package dev.game.test.world.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.world.block.connected.ConnectedTexture;
import dev.game.test.world.block.connected.EnumTextureConnection;

import java.awt.*;

public class BlockDirt extends Block {

    private ConnectedTexture texture;

    public BlockDirt() {

    }

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTexture(Gdx.files.internal("map/dirt.png"));
    }

    @Override
    public TextureRegion getTexture(BlockData blockData) {
        return texture.getTexture(blockData);
    }
}
