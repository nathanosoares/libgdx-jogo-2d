package dev.game.test.world.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.screens.GameScreen;
import dev.game.test.world.World;
import dev.game.test.world.WorldLayer;
import dev.game.test.world.block.BlockData;
import dev.game.test.world.entity.Entity;
import lombok.Setter;

public class WorldRender implements MapRenderer {

    public static final float TILE_WIDTH = 16.0f;
    public static final float UNIT_PER_PIXEL = 1.0f / 16.0f;

    //
    protected final Batch batch;

    protected final World world;

    //
    private Rectangle viewBounds;

    @Setter
    private Viewport viewport;

    public WorldRender(Batch batch, World world) {
        this.batch = batch;
        this.world = world;

        this.viewBounds = new Rectangle();
    }

    @Override
    public void setView(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        float width = camera.viewportWidth * camera.zoom;
        float height = camera.viewportHeight * camera.zoom;
        float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
        float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);
        viewBounds.set(camera.position.x - w / 2, camera.position.y - h / 2, w, h);
    }

    @Override
    public void setView(Matrix4 projection, float x, float y, float width, float height) {
        batch.setProjectionMatrix(projection);
        viewBounds.set(x, y, width, height);
    }

    @Override
    public void render() {
        beginRender();

        for (int layerId = 0; layerId < world.getLayers().length; layerId++) {
            WorldLayer layer = world.getLayers()[layerId];
            renderMapLayer(layer);
        }

        endRender();
    }

    @Override
    public void render(int[] layers) {
        beginRender();

        for (int layerIdx : layers) {
            WorldLayer layer = world.getLayers()[layerIdx];
            renderMapLayer(layer);
        }

        endRender();
    }

    private void renderMapLayer(WorldLayer layer) {
        Vector2 mouseScreenPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 mouseWorldPosition = viewport.unproject(mouseScreenPosition);

        float layerTileWidth = TILE_WIDTH * UNIT_PER_PIXEL;
        float layerTileHeight = TILE_WIDTH * UNIT_PER_PIXEL;

        int col1 = Math.max(0, (int) ((viewBounds.x) / layerTileWidth));
        int col2 = (int) Math.min(world.getBounds().getWidth(), (int) ((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth));

        int row1 = Math.max(0, (int) ((viewBounds.y) / layerTileHeight));
        int row2 = (int) Math.min(world.getBounds().getHeight(), (int) ((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight));

        float y = row2 * layerTileHeight;
        float xStart = col1 * layerTileWidth;

        for (int row = row2; row >= row1; row--) {
            float x = xStart;

            for (int col = col1; col < col2; col++) {
                if (!layer.isOrigin(col, row)) {
                    x += layerTileWidth;
                    continue;
                }

                BlockData blockData = layer.getBlock(col, row);
                TextureRegion region = blockData.getBlock().getTexture(blockData);

                if(x == (int) mouseWorldPosition.x && y == (int) mouseScreenPosition.y) {
                    float fade = (float) ((Math.sin(2 * Math.PI * .8f * System.currentTimeMillis() / 1000) + 1.0f) / 2.0f);
                    batch.setColor(0.9f, 0.7f, 1.0f, 0.7f + 0.25f * fade);
                }

                batch.draw(region, x, y, region.getRegionWidth() * UNIT_PER_PIXEL, region.getRegionHeight() * UNIT_PER_PIXEL);
                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

                x += layerTileWidth;
            }

            y -= layerTileHeight;
        }
    }

    public void renderEntities() {
        beginRender();

        for (Entity entity : this.world.getEntities()) {
            entity.draw(this.batch);
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
