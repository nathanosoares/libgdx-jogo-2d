package dev.game.test.core.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import dev.game.test.core.entity.components.CollidableComponent;
import dev.game.test.core.entity.components.RigidBodyComponent;
import dev.game.test.core.entity.components.TransformComponent;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<RigidBodyComponent> rigidBodyMapper;
    private ComponentMapper<CollidableComponent> collidableMapper;

    public MovementSystem() {
        super(Aspect.all(TransformComponent.class, RigidBodyComponent.class));
    }

    @Override
    protected void process(int entityId) {

        TransformComponent transformComponent = transformMapper.get(entityId);
        RigidBodyComponent rigidBodyComponent = rigidBodyMapper.get(entityId);

        float delta = super.world.getDelta();

        transformComponent.position.mulAdd(rigidBodyComponent.velocity, delta);

        if (collidableMapper.has(entityId)) {
            CollidableComponent collidableComponent = collidableMapper.get(entityId);
            if (transformComponent.originCenter) {
                collidableComponent.box.setCenter(transformComponent.position);
            } else {
                collidableComponent.box.setPosition(transformComponent.position);
            }
        }
    }
}
