package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.IClientGame;
import dev.game.test.client.entity.components.AnimateComponent;
import dev.game.test.client.entity.components.VisualComponent;

import java.util.Map;

public class AnimateStateSystem extends IteratingSystem {

    private final IClientGame game;

    public AnimateStateSystem(IClientGame game) {
        super(Family.all(AnimateComponent.class, VisualComponent.class).get());

        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimateComponent animateComponent = AnimateComponent.MAPPER.get(entity);

        Animation<TextureRegion> animation = animateComponent.animations.entrySet().stream()
                .filter(entry -> entry.getKey().test(entity))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (animation != null) {
            VisualComponent visual = VisualComponent.MAPPER.get(entity);

            visual.region = animation.getKeyFrame(visual.time, visual.isLooping);
            visual.time += deltaTime;
        }
    }
}
