package dev.game.test.world.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.world.block.Block;
import dev.game.test.world.block.BlockState;
import dev.game.test.world.block.EnumFacing;
import dev.game.test.world.block.connected.ConnectedTextureSimple;

public class BlockDirt extends Block {

    private ConnectedTextureSimple texture;

    public BlockDirt() {

    }

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTextureSimple(this, Gdx.files.internal("map/dirt.png"));
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
