package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import dev.game.test.api.IClientGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.entity.ILivingEntity;
import dev.game.test.client.entity.components.HitVisualComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.DirectionComponent;
import dev.game.test.core.entity.components.HealthComponent;
import dev.game.test.core.entity.components.PositionComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisualRenderSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private final IClientGame game;
    private final Batch batch;

    private final Sprite spriteSword;

    public VisualRenderSystem(IClientGame game, Batch batch) {
        this.game = game;
        this.batch = batch;

        spriteSword = new Sprite(new Texture(Gdx.files.internal("sword.png")));
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
            IEntity iEntity = (IEntity) entity;

            position = PositionComponent.MAPPER.get(entity);
            visual = VisualComponent.MAPPER.get(entity);

            if (visual.region != null) {

                if (DirectionComponent.MAPPER.has(entity)) {
                    double degrees = DirectionComponent.MAPPER.get(entity).degrees;

                    if (!visual.region.isFlipX()) {
                        if (degrees >= 0) {
                            visual.region.flip(true, visual.region.isFlipY());
                        }
                    } else {
                        visual.region.flip(degrees < 0, visual.region.isFlipY());
                    }
                }

                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

                this.batch.draw(
                        visual.region,
                        position.x - iEntity.getWidth() / 2,
                        position.y - iEntity.getHeight() / 2,
                        0,
                        0,
                        visual.region.getRegionWidth(),
                        visual.region.getRegionHeight(),
                        1 / 16f,
                        1 / 16f,
                        0
                );

                if (HitVisualComponent.MAPPER.has(entity)) {

                    HitVisualComponent hitVisualComponent = HitVisualComponent.MAPPER.get(entity);

                    DirectionComponent directionComponent = DirectionComponent.MAPPER.get(entity);

                    float attackOffset = 0;

                    double degrees = directionComponent.degrees + 90;
                    float rotation = 20;

                    if (hitVisualComponent.handler.hitting) {
                        float hitProgress = hitVisualComponent.handler.time / hitVisualComponent.handler.delay * 100f / 100f;

                        attackOffset = 180f * hitProgress;

                        if (hitVisualComponent.handler.onRight) {
                            attackOffset -= 180f;
                        }

                        rotation += 360f * hitProgress * (hitVisualComponent.handler.onRight ? -1 : 1);

                    } else if (!hitVisualComponent.handler.onRight) {
                        attackOffset = 180;
                    }

                    degrees += attackOffset * (hitVisualComponent.handler.onRight ? 1 : -1);

                    float originX = spriteSword.getWidth() / 2f;
                    float originY = spriteSword.getHeight() / 2f;

                    float radius = 0.5f;
                    double radians = Math.toRadians(degrees);

                    double x = radius * Math.sin(radians) + position.x - originX;
                    double y = radius * Math.cos(radians) + position.y - originY;

                    spriteSword.setFlip(radians < Math.toRadians(directionComponent.degrees), true);
                    spriteSword.setOrigin(4, 15f);
                    spriteSword.setRotation((float) -directionComponent.degrees + rotation);

                    Group group = new Group();

                    group.addActor(new Actor() {
                        @Override
                        public void draw(Batch batch, float parentAlpha) {
                            spriteSword.draw(batch);
                        }
                    });
                    group.setOrigin(originX, originY);
                    group.setPosition((float) x, (float) y - (spriteSword.getHeight() - 8f) / 2);
                    group.draw(this.batch, 1f);

                    if (hitVisualComponent.handler.hitting) {
                        TextureRegion region = hitVisualComponent.animation.getKeyFrame(hitVisualComponent.handler.time);

                        originX = region.getRegionWidth() / 2f;
                        originY = region.getRegionHeight() / 2f;

                        radius = 1.5f;
                        radians = Math.toRadians(hitVisualComponent.handler.degrees);

                        x = radius * Math.sin(radians) + position.x - originX;
                        y = radius * Math.cos(radians) + position.y - originY;

                        this.batch.draw(
                                region,
                                (float) x,
                                (float) y,
                                originX,
                                originY,
                                region.getRegionWidth(),
                                region.getRegionHeight(),
                                1f / 16f,
                                1f / 16f,
                                (float) -hitVisualComponent.handler.degrees
                        );
                    }
                }

                if (entity instanceof ILivingEntity) {
                    ILivingEntity livingEntity = (ILivingEntity) entity;

                    BitmapFont font = new BitmapFont();
                    font.getData().setScale(1 / 32f);
                    font.setUseIntegerPositions(false);

                    final GlyphLayout layout = new GlyphLayout(font, String.format(
                            "%s/%s",
                            livingEntity.getHealth(), livingEntity.getMaxHealth()
                    ));

                    float fontX = position.x - layout.width / 2;
                    float fontY = 0.8f + position.y + layout.height;

                    font.draw(this.batch, layout, fontX, fontY);
                }
            }
        }

        this.batch.end();
    }
}
