package dev.game.test.core.entity.player.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.fsm.State;
import dev.game.test.api.IClientGame;
import dev.game.test.api.IServerGame;
import dev.game.test.api.entity.IEntity;
import dev.game.test.api.net.packet.server.PacketEntityState;
import dev.game.test.core.Game;
import dev.game.test.core.entity.components.StateComponent;

import java.util.Objects;

public class PlayerStateSystem extends IteratingSystem {

    private final Game game;

    public PlayerStateSystem(Game game) {
        super(Family.all(StateComponent.class).get());

        this.game = game;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (this.game instanceof IClientGame) {
            if (!Objects.equals(entity, ((IClientGame) this.game).getClientManager().getPlayer())) {
                return;
            }
        }

        StateComponent stateComponent = StateComponent.MAPPER.get(entity);

        State old = stateComponent.machine.getCurrentState();

        stateComponent.machine.update();

        if (old != stateComponent.machine.getCurrentState()) {

            stateComponent.changedAt = System.currentTimeMillis();

            if (game instanceof IServerGame) {
                ((IServerGame) game).getConnectionHandler().broadcastPacket(new PacketEntityState(
                        ((IEntity) entity).getId(), stateComponent.machine.getCurrentState()
                ));
            }
        }
    }
}
