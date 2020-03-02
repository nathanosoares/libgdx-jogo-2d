package dev.game.test.client.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
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
    public boolean checkProcessing() {
        return this.game.getConnectionHandler().getConnectionManager() != null
                && this.game.getConnectionHandler().getConnectionManager().getState() == PacketConnectionState.State.INGAME;
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
