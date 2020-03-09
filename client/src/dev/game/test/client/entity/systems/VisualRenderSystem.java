package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.api.IClientGame;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.player.componenets.DirectionComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisualRenderSystem extends EntitySystem {

    private final BitmapFont font = new BitmapFont();

    private ImmutableArray<Entity> entities;

    private final IClientGame game;
    private final Batch batch;

    private final Sprite spriteSword = new Sprite(new Texture(Gdx.files.internal("sword.png")));

    {
        spriteSword.setScale(1 / 16f);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VisualComponent.class).get());
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

                if (DirectionComponent.MAPPER.has(entity)) {
                    double degrees = DirectionComponent.MAPPER.get(entity).degrees;

                    if (visual.region.isFlipX()) {
                        if (degrees >= 0) {
                            visual.region.flip(true, visual.region.isFlipY());
                        }
                    } else {
                        visual.region.flip(degrees < 0, visual.region.isFlipY());
                    }
                }

                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);


                Vector2 size = CollisiveComponent.MAPPER.get(entity).box.getSize(new Vector2());

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

                DirectionComponent directionComponent = DirectionComponent.MAPPER.get(entity);

                float originX = spriteSword.getWidth() / 2f;
                float originZ = spriteSword.getHeight() / 2f;

                double y = 0.8 * Math.cos(Math.toRadians(directionComponent.degrees))
                        + position.y
                        + (size.y / 2)
                        - originX;

                double x = 0.8 * Math.sin(Math.toRadians(directionComponent.degrees))
                        + position.x
                        + (size.x / 2)
                        - originZ;

                spriteSword.setOrigin(1 / 16f, 1/ 16f);
                spriteSword.setOriginCenter();
                spriteSword.setFlip(false, Math.signum(directionComponent.degrees) < 0);
                spriteSword.setRotation((float) (-directionComponent.degrees + 90));
                spriteSword.setPosition((float) x, (float) y);

                spriteSword.draw(this.batch);

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
