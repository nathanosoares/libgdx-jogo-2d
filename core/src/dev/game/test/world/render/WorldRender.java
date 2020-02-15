package dev.game.test.world.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;
import dev.game.test.world.block.BlockData;

public class WorldRender {

    public static final float TILE_WIDTH = 16.0f;
    public static final float UNIT_PER_PIXEL = 1.0f / 16.0f;

    //

    protected final Batch batch;

    protected final OrthographicCamera camera;

    protected final World world;

    //

    private Rectangle viewBounds;

    public WorldRender(Batch batch, OrthographicCamera camera, World world) {
        this.batch = batch;
        this.camera = camera;
        this.world = world;

        this.viewBounds = new Rectangle();
    }

    public void render() {
        beginRender();

        this.batch.setProjectionMatrix(this.camera.combined);

        float width = camera.viewportWidth * camera.zoom;
        float height = camera.viewportHeight * camera.zoom;

        float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
        float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);

        viewBounds.set(camera.position.x - w / 2, camera.position.y - h / 2, w, h);

        float layerTileWidth = TILE_WIDTH * UNIT_PER_PIXEL;
        float layerTileHeight = TILE_WIDTH * UNIT_PER_PIXEL;

        int col1 = Math.max(0, (int) ((viewBounds.x) / layerTileWidth));
        int col2 = Math.min(world.getWidth(), (int) ((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth));

        int row1 = Math.max(0, (int) ((viewBounds.y) / layerTileHeight));
        int row2 = Math.min(world.getHeight(), (int) ((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight));

        float y = row2 * layerTileHeight;
        float xStart = col1 * layerTileWidth;

        for (int layerId = 0; layerId < world.getLayers().length; layerId++) {
            WorldLayer layer = world.getLayers()[layerId];
            for (int row = row2; row >= row1; row--) {
                float x = xStart;

                for (int col = col1; col < col2; col++) {
                    if (!layer.isOrigin(col, row)) {
                        x += layerTileWidth;
                        continue;
                    }

                    BlockData blockData = layer.getBlock(col, row);
                    TextureRegion region = blockData.getBlock().getTexture(blockData);

                    batch.draw(region, x, y, region.getRegionWidth() * UNIT_PER_PIXEL, region.getRegionHeight() * UNIT_PER_PIXEL);

                    x += layerTileWidth;
                }

                y -= layerTileHeight;
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

}
