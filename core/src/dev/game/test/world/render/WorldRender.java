package dev.game.test.world.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;

public class WorldRender implements Disposable {

    protected Batch batch;
    protected World world;

    private WorldRender(World world) {

    }

    public void render() {
        beginRender();

        for (WorldLayer layer : world.getLayers()) {
            layer.
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
