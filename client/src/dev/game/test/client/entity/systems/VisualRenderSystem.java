package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import dev.game.test.api.IClientGame;
import dev.game.test.client.RenderInfo;
import dev.game.test.client.entity.components.ModifyVisualComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.Player;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.DirectionComponent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VisualRenderSystem extends EntitySystem {

    private final BitmapFont font = new BitmapFont();

    private ImmutableArray<Entity> entities;

    private final IClientGame game;
    private final Batch batch;

    private final RenderInfo renderInfo = new RenderInfo();

    private final Sprite spriteSword = new Sprite(new Texture(Gdx.files.internal("sword.png")));

    Animation<TextureRegion> animation;
    TextureRegion[] frames;
    private float attackTime = Float.MAX_VALUE;
    private boolean attacked = false;
    private boolean hitting = false;
    private float hittingDegrees = 0;
    private float animationTime = 0;

    {
        spriteSword.setScale(1f / 16f);

        Texture texture = new Texture(Gdx.files.internal("hit.png"));

        frames = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight())[0];

        animation = new Animation<>(150f / 1000f / 5f, frames);
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VisualComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        animationTime += deltaTime;

        PositionComponent position;
        VisualComponent visual;

        this.batch.begin();

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

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

                Vector2 size = CollisiveComponent.MAPPER.get(entity).box.getSize(new Vector2());

                renderInfo.region = visual.region;
                renderInfo.x = position.x;
                renderInfo.y = position.y;
                renderInfo.originX = 0;
                renderInfo.originY = 0;
                renderInfo.width = visual.region.getRegionWidth();
                renderInfo.height = visual.region.getRegionHeight();
                renderInfo.scaleX = 1 / 16f;
                renderInfo.scaleY = 1 / 16f;
                renderInfo.rotation = 0;

                if (ModifyVisualComponent.MAPPER.has(entity)) {
                    ModifyVisualComponent modifyVisualComponent = ModifyVisualComponent.MAPPER.get(entity);

                    if (modifyVisualComponent.consumer != null) {
                        modifyVisualComponent.consumer.accept(renderInfo);
                    }
                }

                this.batch.draw(
                        renderInfo.region,
                        renderInfo.x,
                        renderInfo.y,
                        renderInfo.originX,
                        renderInfo.originY,
                        renderInfo.width,
                        renderInfo.height,
                        renderInfo.scaleX,
                        renderInfo.scaleY,
                        renderInfo.rotation
                );

                if (entity instanceof Player) {
                    long maxHitTime = 150;

                    if (attackTime >= maxHitTime) {
                        hitting = false;
                    }

                    DirectionComponent directionComponent = DirectionComponent.MAPPER.get(entity);

                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && attackTime >= maxHitTime) {
                        attackTime = 0;
                        attacked = !attacked;
                        hitting = true;
                        hittingDegrees = (float) directionComponent.degrees;
                        animationTime = 0;
                    }

                    float attackOffset = 0;

                    double degrees = directionComponent.degrees + 90;
                    float rotation = 20;

                    if (attackTime >= 0 && attackTime < maxHitTime) {
                        attackOffset = 180f * (100f / maxHitTime * attackTime / 100f);

                        if (!attacked) {
                            attackOffset -= 180f;
                        }

                        rotation += (360f * (100f / maxHitTime * attackTime / 100f)) * (attacked ? 1 : -1);

                        attackTime += deltaTime * 1000;
                    } else if (attacked) {
                        attackOffset = 180;
                    }

                    degrees += attackOffset * (attacked ? -1 : 1);

                    float originX = spriteSword.getWidth() / 2f;
                    float originY = spriteSword.getHeight() / 2f;

                    float radius = 0.5f;

                    double x = radius * Math.sin(Math.toRadians(degrees))
                            + position.x
                            + (size.x / 2)
                            - originX;

                    double y = radius * Math.cos(Math.toRadians(degrees))
                            + position.y
                            + (size.y / 2)
                            - originY;

                    spriteSword.setFlip(
                            Math.toRadians(degrees) < Math.toRadians(directionComponent.degrees),
                            true
                    );

                    spriteSword.setOriginCenter();
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

                    if (hitting) {
                        TextureRegion region = animation.getKeyFrame(animationTime);

                        originX = region.getRegionWidth() / 2f;
                        originY = region.getRegionHeight() / 2f;

                        x = 1.5 * Math.sin(Math.toRadians(hittingDegrees))
                                + position.x
                                + (size.x / 2)
                                - originX;

                        y = 1.5 * Math.cos(Math.toRadians(hittingDegrees))
                                + position.y
                                + (size.y / 2)
                                - originY;

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
                                -hittingDegrees
                        );
                    }
                }
            }
        }

        this.batch.end();
    }
}
