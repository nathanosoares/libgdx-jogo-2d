package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import dev.game.test.client.entity.components.FacingVisualFlipComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.FacingComponent;
import dev.game.test.core.entity.components.NamedComponent;
import dev.game.test.core.entity.components.PositionComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisualRenderSystem extends EntitySystem {

    private final BitmapFont font = new BitmapFont();

    private ImmutableArray<Entity> entities;

    private final IClientGame game;
    private final Batch batch;

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(
                PositionComponent.class,
                VisualComponent.class
        ).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        VisualComponent visual;

        this.batch.begin();

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            position = PositionComponent.MAPPER.get(entity);
            visual = VisualComponent.MAPPER.get(entity);

            float nameOffsetY = 0;
            if (visual.region != null) {

                if (FacingComponent.MAPPER.has(entity)) {
                    if (FacingVisualFlipComponent.MAPPER.has(entity)) {
                        FacingComponent facing = FacingComponent.MAPPER.get(entity);
                        FacingVisualFlipComponent facingVisualFlip = FacingVisualFlipComponent.MAPPER.get(entity);

                        if (visual.region.isFlipX()) {
                            if (!facingVisualFlip.flipX.contains(facing.facing)) {
                                visual.region.flip(true, visual.region.isFlipY());
                            }
                        } else {
                            visual.region.flip(facingVisualFlip.flipX.contains(facing.facing), visual.region.isFlipY());
                        }
                    }
                }


                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                this.batch.draw(
                        visual.region,
                        position.x,
                        position.y,
                        0,
                        0,
                        visual.region.getRegionWidth(),
                        visual.region.getRegionHeight(),
                        1 / 16f,
                        1 / 16f,
                        0
                );

                nameOffsetY = visual.region.getRegionHeight() / 16f;
            }

//            if (NamedComponent.MAPPER.has(entity)) {
//                this.font.getData().setScale(1f / 32f);
//                this.font.draw(
//                        this.batch,
//                        namedMapper.get(entity).name,
//                        position.x, position.y + nameOffsetY + 1
//                );
//            }
        }

        this.batch.end();
    }
}
