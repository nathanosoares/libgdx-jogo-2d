package dev.game.test.api.block;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.util.EnumFacing;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;

public interface IBlock {

    int getId();

    float getWidth();

    float getHeight();

    IBlockState createState(IWorld world, IWorldLayer layer, int x, int z);

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
