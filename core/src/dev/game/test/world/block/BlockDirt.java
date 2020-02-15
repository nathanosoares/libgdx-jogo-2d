package dev.game.test.world.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.world.World;

public class BlockDirt extends Block {

    private Texture dirtAtlas;

    public BlockDirt() {

    }

    @Override
    public void loadTextures() {
        this.dirtAtlas = new Texture(Gdx.files.internal("map/dirt.png"));
    }

    @Override
    public TextureRegion getTexture(World world, Vector2 position) {
    }
}
