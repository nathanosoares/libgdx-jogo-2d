package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.client.entity.components.AnimateStateComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.StateComponent;

public class AnimateStateSystem extends IteratingSystem {


    public AnimateStateSystem() {
        super(Family.all(
                AnimateStateComponent.class,
                VisualComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimateStateComponent animateState = AnimateStateComponent.MAPPER.get(entity);
        StateComponent state = StateComponent.MAPPER.get(entity);

        Animation<TextureRegion> animation = animateState.animations.get(state.machine.getCurrentState());

        if (animation != null) {
            VisualComponent visual = VisualComponent.MAPPER.get(entity);

            visual.region = animation.getKeyFrame(state.time, state.isLooping);
        }

        state.time += deltaTime;
    }
}
