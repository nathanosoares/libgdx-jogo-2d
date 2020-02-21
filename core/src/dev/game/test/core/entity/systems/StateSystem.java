package dev.game.test.core.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class StateSystem extends IteratingSystem {

    public StateSystem() {
        super(Family.all().get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
