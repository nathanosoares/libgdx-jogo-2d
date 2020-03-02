package dev.game.test.client.systems;

import com.badlogic.ashley.core.EntitySystem;
import dev.game.test.api.IClientGame;
import dev.game.test.api.net.packet.handshake.PacketConnectionState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientSystem extends EntitySystem {

    private final IClientGame game;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        game.getConnectionHandler().processQueue();
    }

    @Override
    public boolean checkProcessing() {
        return this.game.getConnectionHandler().getConnectionManager() != null;
    }
}
