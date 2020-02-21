package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.client.entity.components.AnimateStateComponent;
import dev.game.test.client.entity.components.VisualComponent;
import dev.game.test.core.entity.components.PositionComponent;
import dev.game.test.core.entity.components.StateComponent;

public class AnimateStateSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<VisualComponent> visualMapper = ComponentMapper.getFor(VisualComponent.class);
    private ComponentMapper<StateComponent> stateMapper = ComponentMapper.getFor(StateComponent.class);
    private ComponentMapper<AnimateStateComponent> animateStateMapper = ComponentMapper.getFor(AnimateStateComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(
                AnimateStateComponent.class,
                VisualComponent.class
        ).get());
    }

    @Override
    public void update(float deltaTime) {
        VisualComponent visual;
        AnimateStateComponent animateState;
        StateComponent state;


        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            animateState = animateStateMapper.get(entity);
            state = stateMapper.get(entity);
            
            Animation<TextureRegion> animation = animateState.animations.get(state.state.getCurrentState());

            if (animation != null) {
                visual = visualMapper.get(entity);

                visual.region = animation.getKeyFrame(state.time, state.isLooping);
            }

            state.time += deltaTime;
        }
    }
}
