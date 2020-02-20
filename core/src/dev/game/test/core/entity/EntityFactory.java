package dev.game.test.core.entity;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.math.Vector2;
import dev.game.test.core.entity.components.CollidableComponent;
import dev.game.test.core.entity.components.RigidBodyComponent;
import dev.game.test.core.entity.components.StateComponent;
import dev.game.test.core.entity.components.TransformComponent;
import dev.game.test.core.entity.state.PlayerState;

import java.util.function.BiConsumer;

public class EntityFactory {

    private ComponentMapper<TransformComponent> transformMapper;
    private ComponentMapper<RigidBodyComponent> rigidBodyMapper;
    private ComponentMapper<CollidableComponent> collidableMapper;
    private ComponentMapper<StateComponent> stateMapper;

    public static BiConsumer<com.artemis.World, Integer> postCreate;

    public int createPlayer(com.artemis.World artemis, int x, int y) {
        int entityId = artemis.create();

        TransformComponent transformComponent = transformMapper.create(entityId);
        transformComponent.scaleX = 1 / 16f;
        transformComponent.scaleY = 1 / 16f;
        transformComponent.width = 16 / 16f;
        transformComponent.height = 22 / 16f;
        transformComponent.position.set(x, y);

        rigidBodyMapper.create(entityId);

        CollidableComponent collidableComponent = collidableMapper.create(entityId);
        collidableComponent.box.setSize(transformComponent.width, transformComponent.height);
        collidableComponent.box.setCenter(new Vector2(x, y));

        StateComponent<PlayerState>  playerStateStateComponent = stateMapper.create(entityId);
        playerStateStateComponent.state = new DefaultStateMachine<>(artemis.getEntity(entityId), PlayerState.IDLE);

        if (postCreate != null) {
            postCreate.accept(artemis, entityId);
        }

        return entityId;
    }
}
