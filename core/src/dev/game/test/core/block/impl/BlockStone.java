package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.states.SolidBlockState;

public class BlockStone extends Block  {

    private TextureRegion texture;

    public BlockStone() {
        super(1);
    }

    @Override
    public IBlockState createState(IWorld world, IWorldLayer layer, int x, int y) {
        return new SolidBlockState(this, world, layer, new Vector2(x, y));
    }

    @Override
    public void loadTextures() {
        this.texture = new TextureRegion(new Texture(Gdx.files.internal("map/stone.png")));
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture;
    }
}
