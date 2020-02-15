package dev.game.test.world.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.world.block.connected.ConnectedTextureSimple;

public class BlockWater extends Block {

    private ConnectedTextureSimple texture;

    public BlockWater() {

    }

    @Override
    public void loadTextures() {
        this.texture = new ConnectedTextureSimple(this, Gdx.files.internal("map/water.png"));
    }

    @Override
    public void onBlockNeighbourUpdate(BlockData blockData, EnumFacing neighbourFacing) {
        super.onBlockNeighbourUpdate(blockData, neighbourFacing);
        this.texture.computeTextures(blockData);
    }

    @Override
    public TextureRegion getTexture(BlockData blockData) {
        return texture.getTexture(blockData);
    }
}
