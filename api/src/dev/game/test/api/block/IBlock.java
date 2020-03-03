package dev.game.test.api.block;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.util.EnumFacing;

public interface IBlock {

    int getId();

    int getWidth();

    int getHeight();

    /*
        Textures
     */
    void loadTextures();

    TextureRegion getTexture(IBlockState state);

    /*
        Updates
     */

    void onBlockNeighbourUpdate(IBlockState blockState, EnumFacing neighbourFacing);
}
