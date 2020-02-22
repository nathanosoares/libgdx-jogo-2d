package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.client.block.BlockClient;
import dev.game.test.core.block.BlockState;
import dev.game.test.core.block.connected.ConnectedTextureSimple;

public class BlockReinforcedDirt extends BlockClient {

    private ConnectedTextureSimple texture;

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTextureSimple(this, Gdx.files.internal("map/reinforced_dirt.png"));
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
