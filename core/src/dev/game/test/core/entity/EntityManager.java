package dev.game.test.core.entity;

import com.artemis.ComponentMapper;
import dev.game.test.core.entity.components.IdentifiableComponent;

import java.util.UUID;

public class EntityManager {

    private ComponentMapper<IdentifiableComponent> identifiableMapper;

    public UUID getId(int entityId) {
        if (identifiableMapper.has(entityId)) {
            return identifiableMapper.get(entityId).uuid;
        }

        return null;
    }
}
