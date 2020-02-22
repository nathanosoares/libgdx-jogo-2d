package dev.game.test.api.block;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface IBlock {

    int getId();

    void setId(int id);

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

}
