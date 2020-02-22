package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.core.entity.components.CollisiveComponent;
import dev.game.test.core.entity.components.MovementComponent;
import dev.game.test.core.entity.components.PositionComponent;

public class CollisiveDebugSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;


    private final Batch batch;
    private final ShapeRenderer shapeRenderer;

    public CollisiveDebugSystem(Batch batch) {
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(
                CollisiveComponent.class,
                PositionComponent.class
        ).get());
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent position;
        CollisiveComponent collisive;

        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setProjectionMatrix(this.batch.getProjectionMatrix());

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            position = PositionComponent.MAPPER.get(entity);
            collisive = CollisiveComponent.MAPPER.get(entity);

            Vector2 size = collisive.box.getSize(new Vector2());
            Vector2 min = collisive.box.getPosition(new Vector2());

            this.shapeRenderer.setColor(Color.GREEN);
            this.shapeRenderer.rect(min.x, min.y, size.x, size.y);

            MovementComponent movement;
            if ((movement = entity.getComponent(MovementComponent.class)) != null) {
                this.shapeRenderer.setColor(Color.BLUE);

                this.shapeRenderer.line(
                        position.x + size.x / 2,
                        position.y + size.y / 2,
                        position.x + movement.velocityX + size.x / 2,
                        position.y + movement.velocityY + size.y / 2
                );
//
//                if (transformComponent.originCenter) {
//                    this.shapeRenderer.line(
//                            position.x,
//                            position.y,
//                            position.x + rigidBody.velocity.x,
//                            position.y + rigidBody.velocity.y
//                    );
//                } else {
//                    this.shapeRenderer.line(
//                            transformComponent.position.x + transformComponent.width / 2,
//                            transformComponent.position.y + transformComponent.height / 2,
//                            transformComponent.position.x + rigidBodyComponent.velocity.x + transformComponent.width / 2,
//                            transformComponent.position.y + rigidBodyComponent.velocity.y + transformComponent.height / 2
//                    );
//                }
            }

//            if (rigidBodyMapper.has(entity)) {
//                RigidBodyComponent rigidBodyComponent = rigidBodyMapper.get(entity);
//

//            }
        }

        this.shapeRenderer.end();
    }
}