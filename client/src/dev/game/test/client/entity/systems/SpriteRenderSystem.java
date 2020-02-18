package dev.game.test.client.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import dev.game.test.client.entity.components.SpriteComponent;
import dev.game.test.core.entity.components.TransformComponent;

public class SpriteRenderSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<SpriteComponent> spriteMapper;

    private final Batch batch;

    public SpriteRenderSystem(Batch batch) {
        super(Aspect.all(TransformComponent.class, SpriteComponent.class));
        this.batch = batch;
    }

    @Override
    protected void begin() {
        this.batch.begin();
    }

    @Override
    protected void process(int entityId) {
        TransformComponent transformComponent = transformMapper.get(entityId);
        SpriteComponent spriteComponent = spriteMapper.get(entityId);

        Sprite sprite = spriteComponent.sprite;
        if (transformComponent.originCenter) {
            sprite.setOriginCenter();
        } else {
            sprite.setOrigin(transformComponent.origin.x, transformComponent.origin.y);
        }

        sprite.setScale(transformComponent.scaleX, transformComponent.scaleY);
        sprite.setRotation(transformComponent.rotation);
        sprite.setPosition(transformComponent.position.x, transformComponent.position.y);

        this.batch.draw(
                sprite.getTexture(),
                sprite.getX() - sprite.getOriginX(),
                sprite.getY() - sprite.getOriginY(),
                sprite.getOriginX(),
                sprite.getOriginY(),
                sprite.getWidth(),
                sprite.getHeight(),
                sprite.getScaleX(),
                sprite.getScaleY(),
                sprite.getRotation(),
                sprite.getRegionX(),
                sprite.getRegionY(),
                sprite.getRegionWidth(),
                sprite.getRegionHeight(),
                false, false
        );
    }

    @Override
    protected void end() {
        this.batch.end();
    }

}
