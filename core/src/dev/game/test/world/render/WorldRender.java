package dev.game.test.world.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;
import dev.game.test.world.block.BlockData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorldRender implements Disposable {

    protected final Batch batch;
    protected final World world;

    public void render() {
        beginRender();

        for (int layerId = 0; layerId < world.getLayers().length; layerId++) {
            WorldLayer layer = world.getLayers()[layerId];

            for (int x = 0; x < layer.getBlocks().length; x++) {
                for (int y = 0; y < layer.getBlocks()[x].length; y++) {

                    if (!layer.isOrigin(x, y)) {
                        continue;
                    }

                    BlockData blockData = layer.getBlock(x, y);
                    Vector2 position = blockData.getPosition();

                    TextureRegion textureRegion = blockData.getBlock().getTexture(blockData);
                    this.batch.draw(textureRegion, position.x, position.y);
                }
            }
        }

        endRender();
    }

    protected void beginRender() {
        batch.begin();
    }

    protected void endRender() {
        batch.end();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
    }
}
