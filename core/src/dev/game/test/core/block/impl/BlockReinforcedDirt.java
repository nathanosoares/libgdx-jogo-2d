package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.connected.ConnectedTextureSimple;

public class BlockReinforcedDirt extends Block {

    private ConnectedTextureSimple texture;

    public BlockReinforcedDirt() {
        super(5);
    }

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTextureSimple(this, Gdx.files.internal("map/reinforced_dirt.png"));
    }

    @Override
    public void onBlockNeighbourUpdate(IBlockState blockState, EnumFacing neighbourFacing) {
        super.onBlockNeighbourUpdate(blockState, neighbourFacing);
        this.texture.computeTextures(blockState);
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture.getTexture(blockState);
    }
}
