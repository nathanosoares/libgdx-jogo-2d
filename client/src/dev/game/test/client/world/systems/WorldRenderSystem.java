package dev.game.test.client.world.systems;

import aurelienribon.tweenengine.Tween;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IPlayer;
import dev.game.test.api.world.IWorld;
import dev.game.test.api.world.IWorldLayer;
import dev.game.test.client.ClientApplication;
import dev.game.test.client.world.animations.OpacityAccessor;
import dev.game.test.core.block.BlockState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class WorldRenderSystem extends EntitySystem {

    public static final float TILE_WIDTH = 16.0f;
    public static final float UNIT_PER_PIXEL = 1.0f / 16.0f;

    private final IClientGame game;
    protected final OrthographicCamera camera;
    protected final Batch batch;
    private final Viewport viewport;

    private final Rectangle blockArea = new Rectangle();
    private final Rectangle viewBounds = new Rectangle();

    private final Vector2 mouseScreenPosition = new Vector2();

    private final Vector2 mouseWorldPosition = new Vector2();

    private final Map<BlockState, AtomicReference<Float>> opacity = Maps.newHashMap();

    TextureRegion portalFrame = new TextureRegion(new Texture(Gdx.files.internal("map/portal-frame.png")));
    TextureRegion portalGround = new TextureRegion(new Texture(Gdx.files.internal("map/portal-ground.png")));

    LinkedList<PortalFrame> portalFrames = Lists.newLinkedList();
    float maxPortalFrames = 3;
    float portalFrameX = 5;
    float portalFrameY = 5;
    float maxPortalFramesOffSet = 0.9f;

    {
        for (int i = 0; i < maxPortalFrames; i++) {
            PortalFrame frame = new PortalFrame();
            frame.y += i * maxPortalFramesOffSet / maxPortalFrames;
            portalFrames.add(frame);
        }
    }

    private class PortalFrame {
        long portalTime = 0;
        float time = 0;
        float y = portalFrameY;
    }

    public Vector2 getMouseWorldPosition(Vector2 vector2) {
        return vector2.set(mouseWorldPosition);
    }


    @Override
    public void update(float deltaTime) {

        this.batch.begin();
        this.setView(this.camera);

        mouseScreenPosition.set(Gdx.input.getX(), Gdx.input.getY());
        mouseWorldPosition.set(viewport.unproject(mouseScreenPosition.cpy()));

        if (game.getClientManager().getCurrentWorld() != null) {
            IWorld world = game.getClientManager().getCurrentWorld();

            for (int layerIndex = 0; layerIndex < world.getLayers().length; layerIndex++) {
                renderMapLayer(world, layerIndex);
            }
        }

        batch.setColor(1.0f, 1.0f, 1.0f, 1);
        batch.draw(portalGround, portalFrameX, portalFrameY, 1, 1);

        while (portalFrames.size() < maxPortalFrames) {
            PortalFrame frame = new PortalFrame();
            frame.y += (portalFrames.size() + 1 - maxPortalFrames) * maxPortalFramesOffSet / maxPortalFrames;
            portalFrames.add(frame);
        }

        Iterator<PortalFrame> iterator = portalFrames.iterator();

        while (iterator.hasNext()) {
            PortalFrame portalFrame = iterator.next();

            portalFrame.portalTime += deltaTime;

            portalFrame.time += deltaTime;
            portalFrame.y += 1 * deltaTime;

            if (portalFrame.y > portalFrameY + maxPortalFramesOffSet / 2) {
                batch.setColor(1.0f, 1.0f, 1.0f, .1f + (portalFrameY + maxPortalFramesOffSet - portalFrame.y));
            } else {
                batch.setColor(1.0f, 1.0f, 1.0f, 1);
            }

            batch.draw(this.portalFrame, portalFrameX, portalFrame.y, 1, 1);

            if (portalFrame.y > portalFrameY + maxPortalFramesOffSet) {
                iterator.remove();
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

    private void renderMapLayer(IWorld world, int layerIndex) {

        IWorldLayer layer = world.getLayers()[layerIndex];

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

                BlockState state = (BlockState) layer.getBlockState(col, row);
                TextureRegion region = state.getBlock().getTexture(state);

                batch.setColor(1.0f, 1.0f, 1.0f, 1);

                if (x == (int) mouseWorldPosition.x && y == (int) mouseWorldPosition.y) {
                    float fade = (float) ((Math.sin(2 * Math.PI * .8f * System.currentTimeMillis() / 1000) + 1.0f) / 2.0f);
                    batch.setColor(0.9f, 0.7f, 1.0f, 0.7f + 0.25f * fade);
                }

//                if (MovementSystem.debug.containsEntry(col, row)) {
//                    float fade = (float) ((Math.sin(2 * Math.PI * .8f * System.currentTimeMillis() / 1000) + 1.0f) / 2.0f);
//                    batch.setColor(0.9f, 0.7f, 1.0f, 0.7f + 0.25f * fade);
//                }

                if (region != null) {

                    float alpha = batch.getColor().a;

                    IPlayer player = this.game.getClientManager().getPlayer();

                    if (player != null && layerIndex > 0) {
                        blockArea.set(x, y, state.getBlock().getWidth(), state.getBlock().getHeight());

                        if (blockArea.overlaps(new Rectangle(player.getPosition().x, player.getPosition().y, 1.5f, 1.5f))) {

                            AtomicReference<Float> reference;

                            if ((reference = opacity.get(state)) == null) {
                                opacity.put(state, new AtomicReference<>(0.5f));
                            } else {
                                Tween.set(reference, OpacityAccessor.ALPHA).kill();
                                reference.set(0.5f);
                            }

                            alpha = 0.5f;

                        } else {
                            AtomicReference<Float> reference;

                            if ((reference = opacity.get(state)) != null) {
                                alpha = reference.get();

                                if (alpha == 0.5f) {
                                    Tween.to(reference, OpacityAccessor.ALPHA, 1f)
                                            .target(1f)
                                            .start(ClientApplication.getInstance().getTweenManager());
                                }

                                if (alpha >= 1) {
                                    Tween.set(reference, OpacityAccessor.ALPHA).kill();
                                    opacity.remove(state);
                                }
                            }
                        }
                    }

                    batch.setColor(
                            batch.getColor().r,
                            batch.getColor().g,
                            batch.getColor().b,
                            alpha
                    );


                    batch.draw(region, x, y, region.getRegionWidth() * UNIT_PER_PIXEL, region.getRegionHeight() * UNIT_PER_PIXEL);
                }

                x += layerTileWidth;
            }

            y -= layerTileHeight;
        }
    }
}
