package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.IClientGame;
import dev.game.test.client.entity.components.AnimateStateComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.StateComponent;

public class AnimateStateSystem extends IteratingSystem {

    private final IClientGame game;

    public AnimateStateSystem(IClientGame game) {
        super(Family.all(AnimateStateComponent.class, VisualComponent.class).get());

        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimateStateComponent animateStateComponent = AnimateStateComponent.MAPPER.get(entity);
        StateComponent stateComponent = StateComponent.MAPPER.get(entity);

        State state = stateComponent.machine.getCurrentState();

        Animation<TextureRegion> animation = animateStateComponent.animations.get(state);

        if (animation != null) {
            VisualComponent visual = VisualComponent.MAPPER.get(entity);

            visual.region = animation.getKeyFrame(visual.time, visual.isLooping);
            visual.time += deltaTime;
        }
    }
}
