package dev.game.test.api.block.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.util.EnumFacing;

public interface ITextureInstance {

    void onBlockNeighbourUpdate(IBlockState blockState, EnumFacing neighbourFacing);

    TextureRegion getTexture(IBlockState blockState);

}
