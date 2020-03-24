package dev.game.test.core.block.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import dev.game.test.api.block.IBlockState;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.Block;
import dev.game.test.core.block.states.BlockState;
import dev.game.test.core.block.states.SolidBlockState;

public class BlockGrassPlant extends Block {

    private TextureRegion texture;

    public BlockGrassPlant() {
        super(7);
    }


    @Override
    public void loadTextures() {
        this.texture = new TextureRegion(new Texture(Gdx.files.internal("grass.png")));
    }

    @Override
    public TextureRegion getTexture(IBlockState blockState) {
        return texture;
    }
}