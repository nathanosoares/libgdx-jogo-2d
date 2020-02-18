package dev.game.test.client.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.core.entity.components.CollidableComponent;
import dev.game.test.core.entity.components.RigidBodyComponent;
import dev.game.test.core.entity.components.TransformComponent;

public class CollidableDebugSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<CollidableComponent> collidableMapper;
    private ComponentMapper<RigidBodyComponent> rigidBodyMapper;

    private final Batch batch;
    private final ShapeRenderer shapeRenderer;

    public CollidableDebugSystem(Batch batch) {
        super(Aspect.all(TransformComponent.class, CollidableComponent.class));
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void begin() {
        this.shapeRenderer.setProjectionMatrix(this.batch.getProjectionMatrix());
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    }

    @Override
    protected void process(int entityId) {
        TransformComponent transformComponent = transformMapper.get(entityId);
        CollidableComponent collidableComponent = collidableMapper.get(entityId);

        Vector2 size = collidableComponent.box.getSize(new Vector2());
        Vector2 min = collidableComponent.box.getPosition(new Vector2());
        Vector2 max = size.cpy().add(min);

        this.shapeRenderer.setColor(Color.GREEN);
        this.shapeRenderer.rect(min.x, min.y, size.x, size.y);

        if (rigidBodyMapper.has(entityId)) {
            RigidBodyComponent rigidBodyComponent = rigidBodyMapper.get(entityId);

            this.shapeRenderer.setColor(Color.BLUE);

            if (transformComponent.originCenter) {
                this.shapeRenderer.line(
                        transformComponent.position.x,
                        transformComponent.position.y,
                        transformComponent.position.x + rigidBodyComponent.velocity.x,
                        transformComponent.position.y + rigidBodyComponent.velocity.y
                );
            } else {
                this.shapeRenderer.line(
                        transformComponent.position.x + transformComponent.width / 2,
                        transformComponent.position.y + transformComponent.height / 2,
                        transformComponent.position.x + rigidBodyComponent.velocity.x + transformComponent.width / 2,
                        transformComponent.position.y + rigidBodyComponent.velocity.y + transformComponent.height / 2
                );
            }
        }
    }

    @Override
    protected void end() {
        this.shapeRenderer.end();
    }
}
