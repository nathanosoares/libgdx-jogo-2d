package dev.game.test.client.world.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.game.test.api.IClientGame;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.core.block.BlockState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorldRenderSystem extends EntitySystem {

    public static final float TILE_WIDTH = 16.0f;
    public static final float UNIT_PER_PIXEL = 1.0f / 16.0f;

    private final IClientGame clientGame;
    protected final OrthographicCamera camera;
    protected final Batch batch;
    private final Viewport viewport;

    private final Rectangle viewBounds = new Rectangle();

    @Override
    public void update(float deltaTime) {
        this.batch.begin();

        this.setView(this.camera);

        if (clientGame.getClientManager().getCurrentWorld() != null) {
            IWorld world = clientGame.getClientManager().getCurrentWorld();

            for (int layerIndex = 0; layerIndex < world.getLayers().length; layerIndex++) {
                renderMapLayer(world, world.getLayers()[layerIndex]);
            }
        }

        this.batch.end();
    }

    private void setView(OrthographicCamera camera) {
        this.batch.setProjectionMatrix(camera.combined);
        float width = camera.viewportWidth * camera.zoom;
        float height = camera.viewportHeight * camera.zoom;
        float w = width * Math.abs(camera.up.y) + height * Math.abs(camera.up.x);
        float h = height * Math.abs(camera.up.y) + width * Math.abs(camera.up.x);
        this.viewBounds.set(camera.position.x - w / 2, camera.position.y - h / 2, w, h);
    }

    private void renderMapLayer(IWorld world, IWorldLayer layer) {
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

                BlockState blockState = (BlockState) layer.getBlockState(col, row);
                TextureRegion region = blockState.getBlock().getTexture(blockState);

                if (x == (int) mouseWorldPosition.x && y == (int) mouseScreenPosition.y) {
                    float fade = (float) ((Math.sin(2 * Math.PI * .8f * System.currentTimeMillis() / 1000) + 1.0f) / 2.0f);
                    batch.setColor(0.9f, 0.7f, 1.0f, 0.7f + 0.25f * fade);
                }

//                if (MovementSystem.debug.containsEntry(col, row)) {
//                    float fade = (float) ((Math.sin(2 * Math.PI * .8f * System.currentTimeMillis() / 1000) + 1.0f) / 2.0f);
//                    batch.setColor(0.9f, 0.7f, 1.0f, 0.7f + 0.25f * fade);
//                }

                if (region != null) {
                    batch.draw(region, x, y, region.getRegionWidth() * UNIT_PER_PIXEL, region.getRegionHeight() * UNIT_PER_PIXEL);
                    batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                }

                x += layerTileWidth;
            }

            y -= layerTileHeight;
        }
    }
}
