package dev.game.test.world.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.world.block.connected.ConnectedTexture;

public class BlockWater extends Block {

    private ConnectedTexture texture;

    public BlockWater() {

    }

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTexture(Gdx.files.internal("map/water.png"));
    }

    @Override
    public TextureRegion getTexture(BlockData blockData) {
        return texture.getTexture(blockData);
    }
}
