package dev.game.test.client.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
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


        if (transformComponent.originCenter) {
            spriteComponent.sprite.setOriginCenter();
        } else {
            spriteComponent.sprite.setOrigin(transformComponent.origin.x, transformComponent.origin.y);
        }

        spriteComponent.sprite.setScale(transformComponent.scaleX, transformComponent.scaleY);

        spriteComponent.sprite.setPosition(transformComponent.position.x, transformComponent.position.y);

        spriteComponent.sprite.draw(this.batch);
    }

    @Override
    protected void end() {
        this.batch.end();
    }

}
