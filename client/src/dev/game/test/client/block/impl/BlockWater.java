package dev.game.test.client.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.client.block.BlockClient;
import dev.game.test.client.block.connected.ConnectedTextureSimple;
import dev.game.test.core.block.BlockState;

public class BlockWater extends BlockClient {

    private ConnectedTextureSimple texture;

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTextureSimple(this, Gdx.files.internal("map/water.png"));
    }

    @Override
    public void onBlockNeighbourUpdate(BlockState blockState, EnumFacing neighbourFacing) {
        super.onBlockNeighbourUpdate(blockState, neighbourFacing);
        this.texture.computeTextures(blockState);
    }

    @Override
    public TextureRegion getTexture(BlockState blockState) {
        return texture.getTexture(blockState);
    }
}
