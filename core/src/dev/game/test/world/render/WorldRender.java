package dev.game.test.world.render;

import com.badlogic.gdx.graphics.g2d.Batch;
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

        for (WorldLayer layer : world.getLayers()) {
            for (BlockData[] blocks : layer.getBlocks()) {
                for (BlockData block : blocks) {
                    this.batch.draw(block.getBlock().getTexture(block), block.getPosition().x, block.getPosition().y);
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
