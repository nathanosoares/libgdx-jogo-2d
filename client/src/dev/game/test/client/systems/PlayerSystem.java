package dev.game.test.client.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import dev.game.test.core.entity.components.NetworkComponent;

public class PlayerSystem extends IteratingSystem {
    public PlayerSystem() {
        super(Family.all(NetworkComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        NetworkComponent.MAPPER.get(entity).packetHandler.processQueue();
    }
}
