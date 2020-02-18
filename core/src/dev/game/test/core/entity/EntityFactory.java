package dev.game.test.core.entity;

import com.artemis.ComponentMapper;
import dev.game.test.core.entity.components.RigidBodyComponent;
import dev.game.test.core.entity.components.TransformComponent;

import java.util.function.BiConsumer;

public class EntityFactory {

    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<RigidBodyComponent> rigidBodyMapper;

    public static BiConsumer<com.artemis.World, Integer> postCreate;

    public int createPlayer(com.artemis.World artemis, int x, int y) {
        int entityId = artemis.create();

        TransformComponent transformComponent = transformMapper.create(entityId);
        transformComponent.scaleX = 1 / 16f;
        transformComponent.scaleY = 1 / 16f;

        rigidBodyMapper.create(entityId);


        if (postCreate != null) {
            postCreate.accept(artemis, entityId);
        }

        return entityId;
    }
}
